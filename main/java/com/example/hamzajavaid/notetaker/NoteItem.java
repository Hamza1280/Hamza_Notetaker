package com.example.hamzajavaid.notetaker;


import android.graphics.Bitmap;
import android.media.Image;

public class NoteItem {
    private int id;
    private String title;
    private  String description;
    private Bitmap image;


    public void setImage(Bitmap image) {
        this.image = image;
    }



    public Bitmap getImage() {
        return image;
    }


    public NoteItem(int id, String title, String description,Bitmap image){
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
}
