package com.ANUBHAV.INFI_Player;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ANUBHAV on 01-Jan-17.
 */

public class FolderData  {
    String name,path;
    List<ItemData> list;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ItemData> getList() {
        return list;
    }

    public void setList(List<ItemData> list) {
        this.list = list;
    }


}
