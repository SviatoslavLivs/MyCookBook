package com.cookbook.livs.mycookbook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context context;
    private int resourceId;
    private List<Recipe> data = null;

    public RecipeAdapter(Context context, int resourceId, List<Recipe> data) {
        super(context, resourceId, data);
        this.resourceId = resourceId;
        this.context = context;
        this.data = data;
    }




    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecipeHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            holder = new RecipeHolder();
            holder.imgIcon = (ImageView) row.findViewById(com.cookbook.livs.cookbook.R.id.imageFood);
            holder.txtTitle = (TextView) row.findViewById(com.cookbook.livs.cookbook.R.id.textTitle);
            holder.txtPublisher = (TextView) row.findViewById(com.cookbook.livs.cookbook.R.id.textPublisher);

            row.setTag(holder);
        } else {
            holder = (RecipeHolder) row.getTag();
        }
        Recipe recipe = data.get(position);
        holder.txtTitle.setText(recipe.getTitle());
        holder.txtPublisher.setText(recipe.getPublisher());
        holder.imgIcon.setImageBitmap(recipe.getBitmap());
        return row;
    }

    static class RecipeHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtPublisher;
    }

    public List<Recipe> getData() {
        return data;
    }
}
