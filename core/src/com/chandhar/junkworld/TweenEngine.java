package com.chandhar.junkworld;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Tommy on 11/03/2018.
 */

public final class TweenEngine
{
    public static final String TAG = TweenEngine.class.getSimpleName();
    public static TweenEngine instance = new TweenEngine();

    public static TweenEngine getInstance() {
        return instance;
    }

    private static TweenManager tweenManager = new TweenManager();



}
