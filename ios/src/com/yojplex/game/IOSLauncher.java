package com.yojplex.game;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;

import java.util.ArrayList;
import java.util.Arrays;

import admob.src.org.robovm.bindings.admob.GADAdSize;
import admob.src.org.robovm.bindings.admob.GADBannerView;
import admob.src.org.robovm.bindings.admob.GADBannerViewDelegateAdapter;
import admob.src.org.robovm.bindings.admob.GADRequest;
import admob.src.org.robovm.bindings.admob.GADRequestError;

public class IOSLauncher extends IOSApplication.Delegate implements IActivityRequestHandler{
    private static final boolean USE_TEST_DEVICES = false;
    private GADBannerView adview;
    private IOSApplication iosApplication;
    private UIViewController viewController;
    private UIWindow window;
    private float bannerWidth;
    private float bannerHeight;
    public double screenWidth;
    private double screenHeight;
    public double adWidth;
    public double adHeight;
    private GADRequest request;
    private boolean adInitialized=false;

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        iosApplication = new IOSApplication(new MyGdxGame(this), config);
        config.orientationLandscape=false;
        config.orientationPortrait=true;
        viewController=new UIViewController();

        initializeAds();

        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        screenWidth = screenSize.getWidth();
        screenHeight=screenSize.getHeight();

        final CGSize adSize = adview.getBounds().getSize();
        adWidth = adSize.getWidth();
        adHeight = adSize.getHeight();

        bannerWidth = (float) screenWidth;
        bannerHeight = (float) (bannerWidth / adWidth * adHeight);

        window=new UIWindow(new CGRect((screenWidth / 2) - adWidth / 2, screenHeight-adHeight, bannerWidth, bannerHeight));
        window.setRootViewController(viewController);
        window.setBackgroundColor(UIColor.clear());

        adview.setFrame(window.getBounds());

        MyGdxGame.setOs(MyGdxGame.OS.IOS);

        return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }


    public void initializeAds() {
        if (!adInitialized) {
            adview = new GADBannerView(GADAdSize.smartBannerPortrait());
            adview.setAdUnitID("ca-app-pub-4743789296025031/7095109830"); //put your secret key here
            adview.setRootViewController(viewController);
            viewController.getView().addSubview(adview);

            request = GADRequest.create();

            if (USE_TEST_DEVICES) {
                final ArrayList<String> testDevices = new ArrayList<String>(Arrays.asList(GADRequest.GAD_SIMULATOR_ID));
                request.setTestDevices(testDevices);
            }

            adview.setDelegate(new GADBannerViewDelegateAdapter() {
                @Override
                public void didReceiveAd(GADBannerView view) {
                    super.didReceiveAd(view);
                }

                @Override
                public void didFailToReceiveAd(GADBannerView view, GADRequestError error) {
                    super.didFailToReceiveAd(view, error);
                }
            });

            adview.loadRequest(request);
            adInitialized=true;
        }
     }

    @Override
    public void showAds(adState show) {

        switch (show){
            case LOAD:
                break;
            case SHOW:
                window.makeKeyAndVisible();
                break;
            case HIDE:
                iosApplication.getUIWindow().makeKeyAndVisible();
                break;
        }
    }

}