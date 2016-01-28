package com.lifo.cowboy.modele;

import java.util.Date;

/**
 * Created by Stav on 28/01/2016.
 */
public class Score {
    float temps;
    String pseudo;
    Date date;

    public float getTemps() {
        return temps;
    }

    public void setTemps(float temps) {
        this.temps = temps;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
