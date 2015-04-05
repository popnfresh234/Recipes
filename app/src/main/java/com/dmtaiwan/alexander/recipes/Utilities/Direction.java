package com.dmtaiwan.alexander.recipes.Utilities;

/**
 * Created by Alexander on 4/3/2015.
 */
public class Direction {
    protected String direction;
    protected String number;

    public static Direction newInstance(){
        Direction d = new Direction();
        d.direction = null;
        return d;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return this.number;
    }
}
