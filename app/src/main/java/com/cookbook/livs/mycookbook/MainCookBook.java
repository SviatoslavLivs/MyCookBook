package com.cookbook.livs.mycookbook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class MainCookBook extends android.support.v7.app.AppCompatActivity {


    private final String SERVER_SERCH_URL = "http://food2fork.com/api/search";
    private final String SERVER_GET_URL = "http://food2fork.com/api/get";

    private final String API_KEY = "3e9166ad629eca6587a5e501e4e30961";
    private final String BUNDLE_RECIPE_ARRAY = "BundleRecipeArray";


    private GridView gridView;
    private EditText editText;

    private SharedPreferences preferences;
    private int column;
    private String strEdit;
    private Integer pageNumber;
    private List<Recipe> recipeArray = new ArrayList<>();
    private RecipeAdapter recipeAdapter;
    private boolean sort;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cookbook.livs.cookbook.R.layout.main_activity_cook_book);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        column = preferences.getInt(getResources().getString(com.cookbook.livs.cookbook.R.string.column), 1);
        gridView = (GridView) findViewById(com.cookbook.livs.cookbook.R.id.gridView);
        gridView.setNumColumns(column);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                String value = ((Recipe) adapter.getItemAtPosition(position)).getRecipeID();
                get(value);
            }
        });
        Button buttonSerch = (Button) findViewById(com.cookbook.livs.cookbook.R.id.buttonserch);
        Button topButton = (Button) findViewById(com.cookbook.livs.cookbook.R.id.topButton);
        Button trendingButton = (Button) findViewById(com.cookbook.livs.cookbook.R.id.trendingButton);
        editText = (EditText) findViewById(com.cookbook.livs.cookbook.R.id.editText);
        OnClickListener chec = new OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case com.cookbook.livs.cookbook.R.id.topButton:
                        pageNumber = 1;
                        sort = preferences.getBoolean("TOP Rated", true);
                        String j = sort ? "r" : "t";
                        search("", j, pageNumber.toString());
                        break;
                    case com.cookbook.livs.cookbook.R.id.trendingButton:
                        pageNumber = 1;
                        sort = preferences.getBoolean("TOP Rated", false);
                        String n = sort ? "r" : "t";
                        search("", n, pageNumber.toString());
                        break;
                }

            }
        };
        topButton.setOnClickListener(chec);
        trendingButton.setOnClickListener(chec);
        buttonSerch.setOnClickListener(
                new OnClickListener() {

                    public void onClick(View v) {
                        pageNumber = 1;
                        Editable edit = editText.getText();
                        strEdit = edit.toString();
                        sort = preferences.getBoolean("TOP Rated", true);
                        String s = sort ? "r" : "t";
                        search(edit.toString(), s, pageNumber.toString());
                    }
                }
        );
    }

    protected void onRestoreInstanceState(Bundle savedInstanseState) {
        super.onRestoreInstanceState(savedInstanseState);
        try {
            ArrayList<Recipe> arrayList = (ArrayList<Recipe>) savedInstanseState.getSerializable(BUNDLE_RECIPE_ARRAY);
            recipeArray.clear();
            recipeArray.addAll(arrayList);
            recipeAdapter = new RecipeAdapter(this, com.cookbook.livs.cookbook.R.layout.column_layout, recipeArray);
            gridView.setAdapter(recipeAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ((gridView.getAdapter()) != null) {
            recipeArray = ((RecipeAdapter) gridView.getAdapter()).getData();
            outState.putSerializable(BUNDLE_RECIPE_ARRAY, new ArrayList<>(recipeArray));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.cookbook.livs.cookbook.R.menu.menu_cook_book, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.cookbook.livs.cookbook.R.id.action_serch) {
            sort = preferences.getBoolean("TOP Rated", true);
            pageNumber++;
            searchBy(sort, pageNumber);

        }
        if (id == com.cookbook.livs.cookbook.R.id.back) {
            sort = preferences.getBoolean("TOP Rated", true);
            if (pageNumber <= 1) {
                searchBy(sort, pageNumber);
            } else {
                pageNumber--;
                searchBy(sort, pageNumber);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(String q, String s, String p) {
        String data = (("&key=" + API_KEY)+("&q=" + q));

        if (s != null) {
            data += ("&sort=" + s);
        }
        if (p != null) {
            data += ("&page=" + p);
        }
        new Resati(this).execute(SERVER_SERCH_URL, data);
    }

    private void get(String id) {
        String data = (("&key=" + API_KEY)+("&rId=" + id));
        new Resati(this, getSupportFragmentManager()).execute(SERVER_GET_URL, data);
    }

    public String searchBy(boolean sort, Integer page) {
        String s = sort ? "r" : "t";
        search(strEdit, s, page.toString());
        return s;
    }
}