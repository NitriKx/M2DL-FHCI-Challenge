package com.lifo.cowboy;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lifo.cowboy.modele.Score;
import com.lifo.cowboy.service.ScoreService;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
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

        updateScore(score);
    }

    private void updateScore(double score) {
        String scoreFormatte = scoreFormatter.format(score);
        TextView tv = (TextView) findViewById(R.id.leaderboard_nouveau_score_result_text);
        tv.setText(scoreFormatte);
    }


    //
    //   METHODES AFFICHAGE
    //

    public void showFormualaireEnregistrement() {
        if (isLeaderboardAffiche == false) {
            return;
        }
        isLeaderboardAffiche = false;
        setContentView(R.layout.activity_nouveau_score_pseudo);
    }

    @Override
    public void onBackPressed() {
        if (isLeaderboardAffiche) {
            showFormualaireEnregistrement();
        } else {
            super.onBackPressed();
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
        LinearLayout rowContainer = (LinearLayout) findViewById(R.id.leaderboard_table_row_container);

        // On récupère les scores
        List<Score> meilleursScores = ScoreService.getInstance().getMeilleursScores(10);

        // Si on a pas de score enregistrés, on affiche un message pour dire qu'il n'y a pas de scores
        if (meilleursScores.size() <= 0) {
            TextView pasDeScoreMessage = new TextView(this);
            pasDeScoreMessage.setText(getString(R.string.leaderboard_pas_de_score_enregistre));
            rowContainer.addView(pasDeScoreMessage);
            return;
        }

        // Trie les score dans l'ordre naturel (croissant)
        Set<Score> sortedScores = new TreeSet<>();
        sortedScores.addAll(meilleursScores);

        // On dessine le tableau
        int rang = 1;
        for (Score score: sortedScores) {
            TableRow row = new TableRow(this);

            TextView textViewRang = new TextView(this);
            textViewRang.setText(Integer.toString(rang));
            row.addView(textViewRang);

            TextView textViewPseudo = new TextView(this);
            textViewPseudo.setText(score.pseudo);
            row.addView(textViewPseudo);

            TextView textViewScore = new TextView(this);
            textViewScore.setText(scoreFormatter.format(score.temps));
            row.addView(textViewScore);

            rowContainer.addView(row);
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
