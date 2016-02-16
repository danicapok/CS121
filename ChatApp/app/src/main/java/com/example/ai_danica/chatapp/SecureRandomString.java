package com.example.ai_danica.chatapp;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by luca on 21/1/2016.
 */
public final class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(50, random).toString(10);
    }

}
