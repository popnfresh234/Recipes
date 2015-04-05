package com.dmtaiwan.alexander.recipes.Utilities;

/**
 * Created by Alexander on 4/2/2015.
 */
public class Ingredient {
    protected double quantity;
    protected String fraction;
    protected String units;
    protected String name;

    public static Ingredient newInstance(){
        Ingredient i = new Ingredient();
        i.quantity = 0;
        i.fraction = null;
        i.units = null;
        i.name = null;
        return i;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public void setFraction (String fraction){
        this.fraction = fraction;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public String getFraction() {
        return this.fraction;
    }

    public String getUnits() {
        return this.units;
    }

    public String getName() {
        return this.name;
    }
}
