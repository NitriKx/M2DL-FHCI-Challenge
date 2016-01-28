package com.lifo.cowboy.modele;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by Stav on 28/01/2016.
 */
@Table(database = BaseDeDonneeLocale.class)
public class Score extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public float temps;

    @Column
    public String pseudo;

    @Column
    public Date date;

    public Score() {
        super();
    }

    public Score(String pseudo, float temps, Date date) {
        super();
        this.pseudo = pseudo;
        this.temps = temps;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Score)) return false;

        Score score = (Score) o;

        if (id != score.id) return false;
        if (Float.compare(score.temps, temps) != 0) return false;
        if (pseudo != null ? !pseudo.equals(score.pseudo) : score.pseudo != null) return false;
        return !(date != null ? !date.equals(score.date) : score.date != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (temps != +0.0f ? Float.floatToIntBits(temps) : 0);
        result = 31 * result + (pseudo != null ? pseudo.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
