package com.dmtaiwan.alexander.recipes.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Alexander on 4/4/2015.
 */
@ParseClassName("Recipe")
public class ParseRecipe extends ParseObject{

    public void setGsonRecipe(String jsonRecipe) {
        put(ParseConstants.KEY_GSON_RECIPE,jsonRecipe);
    }

    public String getGsonRecipe() {
        return getString(ParseConstants.KEY_GSON_RECIPE);
    }

    public void setTitle (String title){
        put(ParseConstants.KEY_TITLE, title);
    }

    public String getTitle() {
        return getString(ParseConstants.KEY_TITLE);
    }
}
