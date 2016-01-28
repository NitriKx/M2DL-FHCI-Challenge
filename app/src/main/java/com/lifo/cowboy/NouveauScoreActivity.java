package com.lifo.cowboy;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    private View mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_score_pseudo);
        mContentView = findViewById(R.id.resultat_dernier_jeu_fullscreen_content);

        // Récupère le score envoyé à l'Activity
        score = getIntent().getFloatExtra("score", -1F);

        hideMenuBars();

        showFormulaireEnregistrement();
    }

    public void enregistrerScore(View v) {
        String pseudo = getPseudoEntre();
        ScoreService.getInstance().enregistrerScore(this.score, pseudo);
        JoueurService.getInstance().setDernierPseudoUtilise(pseudo);
        Toast.makeText(this, "Score enregistré", Toast.LENGTH_SHORT);
        this.retourAuJeu(v);
    }

    public void retourAuJeu(View v) {
        this.finish();
    }

    //
    //   METHODES AFFICHAGE
    //

    public void showFormulaireEnregistrement() {
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

    /**
     *
     * @param v
     */
    public void showLeaderboard(View v) {
        Intent i = new Intent(this, LeaderboardActivity.class);
        startActivity(i);
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
