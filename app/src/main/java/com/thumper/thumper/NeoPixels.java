package com.thumper.thumper;

import com.google.gson.annotations.Expose;

/**
 * Created by Ivan on 10/4/2017.
 */

public class NeoPixels {

    @Expose
    private  int number_of_pixels;
    private int red;
    private int blue;
    private int green;

    public int getNumber_of_pixels() {
        return number_of_pixels;
    }


}
