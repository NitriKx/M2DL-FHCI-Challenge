package com.lifo.cowboy.modele;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by benoit on 28/01/16.
 */
@Table(database = BaseDeDonneeLocale.class)
public class Pseudo extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    public int id;

    @Column
    public String dernierPseudo;

    public Pseudo(String dernierPseudo) {
        super();
        this.dernierPseudo = dernierPseudo;
    }

    public Pseudo() {
        super();
    }

}
