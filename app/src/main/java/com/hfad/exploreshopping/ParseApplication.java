package com.hfad.exploreshopping;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        ParseObject.registerSubclass(PurchaseItem.class);
        ParseObject.registerSubclass(CartItem.class);
//        Parse.initialize(new Parse.Configuration.Builder(this)
//                .applicationId("DPIvDaNRinPNwVJNaPSKb8Gq00XoT9tUQ7Rr9FlL")
//                .clientKey("uWd0UMir256IYwXxcw94K61vGfsYwkzDgz5X1vpD")
//                .server("https://parseapi.back4app.com")
//                .build()
//        );
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("kdUIsbUAQc2QC9Edn4hGXibANxh8jjOOK16k2JUM")
                .clientKey("QtOefaGRl5nPkL74kRmq3t0JGmwOarGNMCYaAwhI")
                .server("https://parseapi.back4app.com")
                .build()
        );
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);
    }
}
