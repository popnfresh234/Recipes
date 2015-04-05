package com.dmtaiwan.alexander.recipes.Utilities;

import java.util.List;

/**
 * Created by Alexander on 4/4/2015.
 */
public class JsonRecipe {
    protected List<Ingredient> ingredients;
    protected List<Direction> directions;
    protected String title;

    public static JsonRecipe newInstance() {
        JsonRecipe jsonRecipe = new JsonRecipe();
        jsonRecipe.ingredients = null;
        jsonRecipe.directions = null;
        jsonRecipe.title = null;
        return jsonRecipe;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<Direction> getDirections() {
        return this.directions;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
