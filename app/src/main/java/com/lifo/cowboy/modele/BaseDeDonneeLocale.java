package com.lifo.cowboy.modele;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Benoît Sauvère on 25/01/16.
 */
@Database(name = BaseDeDonneeLocale.NAME, version = BaseDeDonneeLocale.VERSION)
public class BaseDeDonneeLocale {
    public static final String NAME = "CowboyDatabase";
    public static final int VERSION = 1;
}
