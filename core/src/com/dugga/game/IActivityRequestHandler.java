package com.dugga.game;

/**
 * Created by Kent on 8/22/2015.
 */
public interface IActivityRequestHandler {
    public enum adState{
        LOAD, SHOW, HIDE
    }

    public void showAds(adState show);
}
