package com.cookbook.livs.mycookbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RecipeFragment extends DialogFragment {


    private SharedPreferences sharedPreferences;

    static RecipeFragment newInstance(Recipe obj) {
        RecipeFragment f = new RecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable("Recipe", obj);
        f.setArguments(args);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(com.cookbook.livs.cookbook.R.string.Food);
        View v = inflater.inflate(com.cookbook.livs.cookbook.R.layout.recipes_layout, null);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        TextView title = (TextView) v.findViewById(com.cookbook.livs.cookbook.R.id.textTitle);
        TextView lable = (TextView) v.findViewById(com.cookbook.livs.cookbook.R.id.textRank);
        TextView publisher = (TextView) v.findViewById(com.cookbook.livs.cookbook.R.id.textPublisher);

        ImageView image = (ImageView) v.findViewById(com.cookbook.livs.cookbook.R.id.imageFood);

        TextView ingredients = (TextView) v.findViewById(com.cookbook.livs.cookbook.R.id.edittext);

        Recipe recipe = (Recipe) getArguments().getSerializable("Recipe");
        if (recipe != null) {
            title.setText(recipe.getTitle());
            publisher.setText(recipe.getPublisher());
            image.setImageBitmap(recipe.getBitmap());
            lable.setText("Rank = " +recipe.getRank());
            StringBuilder str = new StringBuilder();
            if (recipe.getIngredients() != null) {
                for (String ingredient : recipe.getIngredients()) {
                    str.append("*  " +ingredient);
                    str.append('\n');
                }
                ingredients.setText(str.toString());
            }
        }
        return v;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}