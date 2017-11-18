package com.ANUBHAV.INFI_Player;

import android.graphics.Bitmap;

/**
 * Created by ANUBHAV on 01-Jan-17.
 */

public class ItemData {

    String name;
    String path;

    long id;
    String size;
    String length;
    Bitmap img;

   /* public ItemData() {
        this.name = "";
        this.size = "";
        this.length = "0";

        int w = 100, h = 100;
        Bitmap.Config conf = Bitmap.Config.ARGB_4444; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap

        this.img = bmp;
    }
*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}