package com.lifo.cowboy;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lifo.cowboy.service.TempsService;
import com.lifo.cowboy.util.DansLaPoche;
import com.lifo.cowboy.util.DansLaPocheListener;
import com.lifo.cowboy.util.FontsOverride;
import com.lifo.cowboy.util.RandomInt;

public class GameActivity extends AppCompatActivity implements DansLaPocheListener {

    TextView tv;
    DansLaPoche dansLaPoche;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "DEFAULT", "duality.ttf");
        setContentView(R.layout.activity_game);
    }

    public void onClickJouer(View view) {
        dansLaPoche = new DansLaPoche(this);
        dansLaPoche.ajoutListener(this);

        setContentView(R.layout.content_game_message);
        tv = (TextView)findViewById(R.id.textView);
        tv.setText("Mettez le téléphone dans votre poche pour commencer à jouer.");
        if (mp != null) {
            mp.release();
        }
        mp = MediaPlayer.create(this, R.raw.jeusoundtrack);
        mp.start();
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
    public void misDansLaPoche() {
        tv.setText("Préparez-vous !");
        TempsService.getInstance().executeApresDelaiAleatoire(5000, 10000, new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            TempsService.getInstance().demarrerTimerTempsReaction();
                            tv.setText("Dégainez !");
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);
                            mp.release();
                            mp = MediaPlayer.create(GameActivity.this, R.raw.degainer);
                            mp.start();
                        } catch (IllegalStateException e) {
                            Log.d("GameActivity", e.getMessage());
                        }
                    }
                });

            }
        });
    }

    @Override
    public void sortiDeLaPoche() {
        if (TempsService.getInstance().getIsJeuEnCours()) {
            setContentView(R.layout.content_game_cible);
            animationCible();
        } else {
            finirJeu(false, 0);
            dansLaPoche.retirerListener(this);
        }
    }

    public void onClickCible(View view) {
        long score;

        try {
            score = TempsService.getInstance().arreterTimerTempsReaction();
            finirJeu(true, score);

        } catch (IllegalStateException e) {
            Log.d("GameActivity", e.getMessage());
            finirJeu(false, 0);
        }

        dansLaPoche.retirerListener(this);
    }

    public void animationCible() {
        final Button cible = (Button)findViewById(R.id.cible);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x-100;
        final int height = size.y-100;
        final int dureeAnim = 600;


        cible.animate().translationX(RandomInt.getRandomInt(0,width)).translationY(RandomInt.getRandomInt(0,height)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cible.animate().translationX(RandomInt.getRandomInt(0,width)).translationY(RandomInt.getRandomInt(0,height)).setListener(this).setDuration(dureeAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).setDuration(dureeAnim);
    }

    public void finirJeu(boolean gagne, long score) {

        if (!gagne) {
            TempsService.getInstance().stopperExecutionAvecDelai();
            tv.setText("Vous avez dégainé trop tôt !");
        } else {
            setContentView(R.layout.activity_game);

            // Démarrage de l'activité de sauvegarde du score
            float scoreSecondes = score / 1000.0F;
            Intent i = new Intent(this, NouveauScoreActivity.class);
            i.putExtra("score", scoreSecondes);
            startActivity(i);
        }
        mp.pause();
    }
}
