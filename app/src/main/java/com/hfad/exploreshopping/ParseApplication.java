package com.hfad.exploreshopping;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(PurchaseItem.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("DPIvDaNRinPNwVJNaPSKb8Gq00XoT9tUQ7Rr9FlL")
                .clientKey("uWd0UMir256IYwXxcw94K61vGfsYwkzDgz5X1vpD")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
