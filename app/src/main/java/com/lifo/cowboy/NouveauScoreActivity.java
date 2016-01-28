package com.lifo.cowboy;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lifo.cowboy.modele.Score;
import com.lifo.cowboy.service.JoueurService;
import com.lifo.cowboy.service.ScoreService;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Benoît Sauvère
 */
public class NouveauScoreActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private static final DecimalFormat scoreFormatter = new DecimalFormat("##.###");


    private float score;

    private boolean isLeaderboardAffiche = false;

    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_score_pseudo);
        mContentView = findViewById(R.id.resultat_dernier_jeu_fullscreen_content);

        // Récupère le score envoyé à l'Activity
        score = getIntent().getFloatExtra("score", -1F);

        hideMenuBars();

        isLeaderboardAffiche = true;
        showFormulaireEnregistrement();
    }

    public void enregistrerScore(View v) {
        ScoreService.getInstance().enregistrerScore(this.score, getPseudoEntre());
        Toast.makeText(this, "Score enregistré", Toast.LENGTH_SHORT);
        this.retourAuJeu(v);
    }

    public void retourAuJeu(View v) {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (isLeaderboardAffiche) {
            showFormulaireEnregistrement();
        } else {
            super.onBackPressed();
        }
    }

    //
    //   METHODES AFFICHAGE
    //

    public void showFormulaireEnregistrement() {
        if (isLeaderboardAffiche == false) {
            return;
        }
        isLeaderboardAffiche = false;
        setContentView(R.layout.activity_nouveau_score_pseudo);
        updatePseudoDefaut();
        updateScore();
    }

    private void updateScore() {
        String scoreFormatte = scoreFormatter.format(score);
        TextView tv = (TextView) findViewById(R.id.leaderboard_nouveau_score_result_text);
        tv.setText(scoreFormatte);
    }

    private String getPseudoEntre() {
        EditText champsPseudo = (EditText) findViewById(R.id.editText_nouveau_score_pseudo);
        String contenuChamps = champsPseudo.getText().toString();
        if (contenuChamps == null) {
            contenuChamps = "";
        }
        return contenuChamps;
    }

    private void updatePseudoDefaut() {
        String dernierPseudoUtilise = JoueurService.getInstance().getDernierPseudoUtilise();
        if (dernierPseudoUtilise != null) {
            EditText champsPseudo = (EditText) findViewById(R.id.editText_nouveau_score_pseudo);
            champsPseudo.setText(dernierPseudoUtilise);
        }
    }

    public void showLeaderboard(View v) {
        if (isLeaderboardAffiche) {
            return;
        }
        isLeaderboardAffiche = true;
        setContentView(R.layout.activity_nouveau_score_leader_board);
        dessinerTableauLeaderBoard();
    }

    private void dessinerTableauLeaderBoard() {

        // On récupère les scores
        List<Score> meilleursScores = ScoreService.getInstance().getMeilleursScores(10);

        TableLayout rowContainer = (TableLayout) findViewById(R.id.leaderboard_table_values);
        // Si on a pas de score enregistrés, on affiche un message pour dire qu'il n'y a pas de scores
        if (meilleursScores.size() <= 0) {
            TextView pasDeScoreMessage = new TextView(this);
            pasDeScoreMessage.setText(getString(R.string.leaderboard_pas_de_score_enregistre));
            rowContainer.addView(pasDeScoreMessage);
            return;
        }

        // On dessine le tableau
        int rang = 1;
        TableRow.LayoutParams tableRowLayoutParameters = new TableRow.LayoutParams();
        tableRowLayoutParameters.height = TableRow.LayoutParams.WRAP_CONTENT;
        tableRowLayoutParameters.width = TableRow.LayoutParams.WRAP_CONTENT;
        tableRowLayoutParameters.gravity = Gravity.CENTER;
        TableRow.LayoutParams tableRowItemsLayoutParameters = new TableRow.LayoutParams();
        tableRowItemsLayoutParameters.height = TableRow.LayoutParams.WRAP_CONTENT;
        tableRowItemsLayoutParameters.width = TableRow.LayoutParams.WRAP_CONTENT;

        for (Score score: meilleursScores) {
            TableRow row = new TableRow(this);
            if (rang % 2 == 0) {
                row.setBackgroundColor(Color.parseColor("#e6e6e6"));
            }

            TextView textViewRang = new TextView(this);
            textViewRang.setText(Integer.toString(rang));
            row.addView(textViewRang, tableRowItemsLayoutParameters);

            TextView textViewPseudo = new TextView(this);
            textViewPseudo.setText(score.pseudo);
            row.addView(textViewPseudo, tableRowItemsLayoutParameters);

            TextView textViewScore = new TextView(this);
            textViewScore.setText(scoreFormatter.format(score.temps));
            row.addView(textViewScore, tableRowItemsLayoutParameters);

            rowContainer.addView(row, tableRowLayoutParameters);
            rang++;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Re-masque les barres lorsque le clavier est fermé
        if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
            hideMenuBars();
        }
    }


    private void hideMenuBars() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
}
