package com.cookbook.livs.mycookbook;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Resati extends AsyncTask<String, String, List<Recipe>> {;
    private Activity activity;
    private FragmentManager fragmentManager;

    private final String RECIPE_ID = "recipe_id";
    private final String IMG_URL = "image_url";
    private final String ARRAY_NAME = "recipes";
    private final String PUBLISHER = "publisher";
    private final String INGREDIENTS = "ingredients";
    private final String SOCIAL_RANK = "social_rank";
    private final String TITLE = "title";
    public Resati(Activity activity) {
        this.activity = activity;
    }
    public Resati(Activity activity, FragmentManager fm) {
        this.activity = activity;
        this.fragmentManager = fm;
    }
    @Override
    protected List<Recipe> doInBackground(String... params) {
        BufferedReader reader = null;
        JSONObject jsonResponse;
        List<Recipe> recipeArray = new ArrayList<>();
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), " Please wait download " , Toast.LENGTH_SHORT).show();
                }
            });

            URL url = new URL(params[0]);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(params[1]);
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "");
            }
            jsonResponse = new JSONObject(sb.toString());

            if (jsonResponse.has(ARRAY_NAME)) {
                recipeArray = parseJSONArray(jsonResponse);
            } else {

            recipeArray.add(0, parseJSONObject(jsonResponse.optJSONObject("recipe")));
             recipeArray.add(1, null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return recipeArray;
    }
    protected void onPostExecute(List<Recipe> result) {
        GridView gridView = (GridView) activity.findViewById(com.cookbook.livs.cookbook.R.id.gridView);
        RecipeAdapter list;
        if (!result.isEmpty()) {
            if (result.size() > 1 && result.get(1) != null) {
                list = new RecipeAdapter(activity, com.cookbook.livs.cookbook.R.layout.column_layout, result);
                gridView.setAdapter(list);
            } else {
                Recipe r = (Recipe) result.get(0);
                RecipeFragment rf = new RecipeFragment().newInstance(r);
                rf.show(fragmentManager, "MyRecipeFragment");
            }}
    }
    private Recipe parseJSONObject(JSONObject obj) {
        Recipe result = new Recipe(
                obj.optString(TITLE), obj.optString(RECIPE_ID), obj.optString(SOCIAL_RANK),
                obj.optString(PUBLISHER), obj.optString(IMG_URL));
        if (obj.has(INGREDIENTS)) {
            JSONArray arr = obj.optJSONArray(INGREDIENTS);
            ArrayList<String> s = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                s.add(arr.optString(i));
            }
            result.setIngredients(s);
        }
        try {
            URL url = new URL(result.getImgURL());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            result.setBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private List<Recipe> parseJSONArray(JSONObject obj) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity.getApplicationContext(), " Loading photos and text", Toast.LENGTH_SHORT).show();
                }
            });
            JSONArray jsonArray = obj.getJSONArray(ARRAY_NAME);
            for (int i = 0; i < Integer.parseInt(obj.getString("count")); i++) {
                recipes.add(parseJSONObject(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}