package com.lifo.cowboy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lifo.cowboy.modele.Score;
import com.lifo.cowboy.service.ScoreService;

import java.text.DecimalFormat;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private static final DecimalFormat scoreFormatter = new DecimalFormat("##.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
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
}
