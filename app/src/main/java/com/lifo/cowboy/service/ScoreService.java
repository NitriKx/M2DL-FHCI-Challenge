package com.lifo.cowboy.service;

import com.lifo.cowboy.modele.Score;
import com.lifo.cowboy.modele.Score_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.List;

/**
 * Created by Benoît Sauvère on 28/01/16.
 */
public class ScoreService {


    //
    //  SCORES
    //

    public Score enregistrerScore(float temps, String pseudo){
        Score nouveauScore = new Score(pseudo, temps, new Date());
        nouveauScore.save();
        return nouveauScore;
    }

    public List<Score> getMeilleursScores(int limit) {
        return SQLite.select().from(Score.class).orderBy(Score_Table.temps, true).limit(limit).queryList();
    }

    //
    //   SINGLETON
    //

    private static final ScoreService __instance = new ScoreService();

    private ScoreService() {}

    public static ScoreService getInstance() {
        return __instance;
    }
}
