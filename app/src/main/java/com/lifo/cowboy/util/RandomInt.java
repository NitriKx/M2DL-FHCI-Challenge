package com.lifo.cowboy.util;

import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by Stav on 28/01/2016.
 */
public class RandomInt {

    public static int getRandomInt(int inf, int sup) {
        SecureRandom randomizer = new SecureRandom(Long.toString(new Date().getTime()).getBytes());

        return inf + randomizer.nextInt(sup - inf);
    }
}
