package com.lifo.cowboy.service;

import com.lifo.cowboy.modele.Pseudo;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * Created by Benoît Sauvère on 28/01/16.
 */
public class JoueurService {

    public void setDernierPseudoUtilise(String pseudo) {
        Pseudo dernierUtilisateur = getEntreePseudo();
        if (dernierUtilisateur == null) {
            dernierUtilisateur = new Pseudo(pseudo);
        } else {
            dernierUtilisateur.dernierPseudo = pseudo;
        }
        dernierUtilisateur.save();
    }

    public String getDernierPseudoUtilise() {
        Pseudo dernierUtilisateur = getEntreePseudo();
        if (dernierUtilisateur != null) {
            return dernierUtilisateur.dernierPseudo;
        } else {
            return null;
        }
    }


    //
    //  PRIVATE
    //

    private Pseudo getEntreePseudo() {
        return SQLite.select().from(Pseudo.class).querySingle();
    }

    //
    //  SINGLETON
    //

    private static final JoueurService __instance = new JoueurService();

    private JoueurService() {}

    public static JoueurService getInstance() {
        return __instance;
    }
}
