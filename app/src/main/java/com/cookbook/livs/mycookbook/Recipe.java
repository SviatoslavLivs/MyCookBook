package com.cookbook.livs.mycookbook;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


public class Recipe implements Serializable {

    private String title;
    private String publisher;
    private ArrayList<String> ingredients;
    private Bitmap bitmap;
    private String recipeID;
    private String imgURL;
    private String rank;
    private static final long serialVersionUID = 42424242;


    public Recipe() {
    }

    public Recipe(String title, String recipeID, String socialRank, String publisher, String imgURL) {

        this.title = title;
        this.recipeID = recipeID;
        this.rank = socialRank;
        this.publisher = publisher;

        this.imgURL = imgURL;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}