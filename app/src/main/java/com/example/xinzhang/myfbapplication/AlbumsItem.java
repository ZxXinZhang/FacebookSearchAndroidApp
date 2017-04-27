package com.example.xinzhang.myfbapplication;

/**
 * Created by xinzhang on 4/17/17.
 */

public class AlbumsItem {

    String albumsName;
    String photo1ID;
    String photo2ID;
    public AlbumsItem(String albumsName, String photo1ID, String photo2ID){
        this.albumsName = albumsName;
        this.photo1ID = photo1ID;
        this.photo2ID = photo2ID;
    }

    public String getAlbumsName(){
        return albumsName;
    }
    public String getPhoto1ID(){
        return photo1ID;
    }
    public String getPhoto2ID(){
        return photo2ID;
    }
}
