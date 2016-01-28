package com.lifo.cowboy.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Benoît Sauvère on 28/01/16.
 */
public class TempsService {

    private long jeuTempsDebut;

    private long jeuTempsFin;

    private boolean isJeuEnCours = false;

    private Timer timer = new Timer();

    //
    //  SERVICES
    //

    public void executeApresDelaiAleatoire(int minTime, int maxTime, final Runnable runnable) {

        SecureRandom randomizer = new SecureRandom(Long.toString(new Date().getTime()).getBytes());

        int delaiAvantExecution = minTime + randomizer.nextInt(maxTime - minTime);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, delaiAvantExecution);
    }


    public synchronized void demarrerTimerTempsReaction() {
        if (isJeuEnCours) {
            throw new IllegalStateException("Impossible de redémarrer le time vu que le jeu est déjà en cours");
        }
        this.isJeuEnCours = true;
        this.jeuTempsDebut = new Date().getTime();
    }

    public synchronized long arreterTimerTempsReaction() {
        if (isJeuEnCours == false) {
            throw new IllegalStateException("Impossible d'arrêter le jeu car il n'est pas lancé");
        }
        this.jeuTempsFin = new Date().getTime();
        return this.jeuTempsFin - this.jeuTempsDebut;
    }

    //
    //  SINGLETON
    //

    private static final TempsService __instance = new TempsService();

    private TempsService() {}

    public static TempsService getInstance() {
        return __instance;
    }

}
