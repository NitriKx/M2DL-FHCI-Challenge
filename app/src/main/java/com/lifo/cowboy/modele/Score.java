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
    public int hashCode() {
        return (temps != +0.0f ? Float.floatToIntBits(temps) : 0);
    }
}
