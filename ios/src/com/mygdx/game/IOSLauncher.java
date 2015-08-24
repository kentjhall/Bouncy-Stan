package com.mygdx.game;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIScreen;

import admob.src.org.robovm.bindings.admob.GADAdSize;
import admob.src.org.robovm.bindings.admob.GADBannerView;
import admob.src.org.robovm.bindings.admob.GADBannerViewDelegateAdapter;
import admob.src.org.robovm.bindings.admob.GADRequest;
import admob.src.org.robovm.bindings.admob.GADRequestError;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.dugga.game.IActivityRequestHandler;
import com.dugga.game.MyGdxGame;
public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler{
    private static final Logger log = new Logger(IOSLauncher.class.getName(), Application.LOG_DEBUG);
    private static final boolean USE_TEST_DEVICES = true;
    private GADBannerView adview;
    private boolean adsInitialized = false;
    private IOSApplication iosApplication;
    private float bannerWidth;
    private float bannerHeight;
    public double screenWidth;
    public double adWidth;
    public double adHeight;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        iosApplication = new IOSApplication(new MyGdxGame(this), config);
        config.orientationLandscape=false;
        config.orientationPortrait=true;

        //initializeAds();

        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
//        screenWidth = screenSize.getWidth();
//
//        final CGSize adSize = adview.getBounds().getSize();
//        adWidth = adSize.getWidth();
//        adHeight = adSize.getHeight();
//
//        log.debug(String.format("Hidding ad. size[%s, %s]", adWidth, adHeight));
//
//        bannerWidth = (float) screenWidth;
//        bannerHeight = (float) (bannerWidth / adWidth * adHeight);


        return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }


    public void initializeAds() {
        if (!adsInitialized) {
            log.debug("Initalizing ads...");

            adsInitialized = true;

            adview = new GADBannerView(GADAdSize.smartBannerPortrait());
            adview.setAdUnitID("ca-app-pub-4743789296025031/7095109830"); //put your secret key here
            adview.setRootViewController(iosApplication.getUIViewController());
            iosApplication.getUIViewController().getView().addSubview(adview);

            //final GADRequest request = GADRequest.create();
//            if (USE_TEST_DEVICES) {
//                final NSArray<?> testDevices = new NSArray<NSObject>(
//                        new NSString(GADRequest.GAD_SIMULATOR_ID));
//                request.setTestDevices(testDevices);
//                log.debug("Test devices: " + request.getTestDevices());
//            }

            adview.setDelegate(new GADBannerViewDelegateAdapter() {
                @Override
                public void didReceiveAd(GADBannerView view) {
                    super.didReceiveAd(view);
                    log.debug("didReceiveAd");
                }

                @Override
                public void didFailToReceiveAd(GADBannerView view,
                                               GADRequestError error) {
                    super.didFailToReceiveAd(view, error);
                    log.debug("didFailToReceiveAd:" + error);
                }
            });

            adview.loadRequest(new GADRequest());

            log.debug("Initalizing ads complete.");
        }
    }

    @Override
    public void showAds(adState show) {

        switch (show){
            case LOAD:
                adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0, bannerWidth, bannerHeight));
                break;
            case SHOW:
                adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0, bannerWidth, bannerHeight));
                break;
            case HIDE:
                adview.setFrame(new CGRect(0, -bannerHeight, bannerWidth, bannerHeight));
                break;
        }
    }

}