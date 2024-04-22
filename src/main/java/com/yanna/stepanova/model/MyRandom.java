package com.yanna.stepanova.model;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public final class MyRandom {
    private static MyRandom instance;
    private final Random random;

    private MyRandom() {
        this.random = new Random();
    }

    public static MyRandom getInstance() {
        if (instance == null) {
            instance = new MyRandom();
        }
        return instance;
    }

    public Random getRandom() {
        return random;
    }
}
