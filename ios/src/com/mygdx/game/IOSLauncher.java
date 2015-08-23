package com.mygdx.game;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.dugga.game.IActivityRequestHandler;
import com.dugga.game.MyGdxGame;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler{
    private static final Logger log = new Logger(IOSLauncher.class.getName(), Application.LOG_DEBUG);
    private static final boolean USE_TEST_DEVICES = true;
    //private GADBannerView adview;
    private boolean adsInitialized = false;
    private IOSApplication iosApplication;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape=false;
        config.orientationPortrait=true;

        return new IOSApplication(new MyGdxGame(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showAds(adState show) {

    }
}