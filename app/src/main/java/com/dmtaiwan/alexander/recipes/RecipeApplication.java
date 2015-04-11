package com.dmtaiwan.alexander.recipes;

import android.app.Application;

import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

/**
 * Created by Alexander on 4/4/2015.
 */
public class RecipeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ParseRecipe.class);
        Parse.initialize(this, "BRwRgPFshJOyczl2wdkFeAuu3Nu1zI1pANJvSOB3", "ETloQRRasWnS7Qg6S4ICUWdpiZa8iAZpGWRSCqiy");
        ParseFacebookUtils.initialize(this);
    }
}
