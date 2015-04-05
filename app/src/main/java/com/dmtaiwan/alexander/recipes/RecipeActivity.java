package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.CustomLinearLayoutManager;
import com.dmtaiwan.alexander.recipes.Utilities.Direction;
import com.dmtaiwan.alexander.recipes.Utilities.Ingredient;
import com.dmtaiwan.alexander.recipes.Utilities.JsonRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.RecipeIngredientAdapter;
import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 4/5/2015.
 */
public class RecipeActivity extends ActionBarActivity {
    public static final String RECIPE_ID = "recipe_id";
    private Context mContext;
    private ParseRecipe mRecipe;

    //NavBar items
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    //End NavBar Items

    private TextView mTitleText;
    private RecyclerView mIngredientsRecyclerView;
    private RecipeIngredientAdapter mIngredientAdapter;
    private ListView mDirectionsListView;
    //TODO ADAPTER

    private List<Ingredient> mIngredientList = null;
    private List<Direction> mDirectionList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        mContext = this;
        mIngredientList = new ArrayList<Ingredient>();
        mDirectionList = new ArrayList<Direction>();
        mTitleText = (TextView) findViewById(R.id.text_view_recipe_title);

        //Setup Ingredient RecyclerView
        mIngredientsRecyclerView = (RecyclerView) findViewById(R.id.list_view_ingredients);
        mIngredientsRecyclerView.setHasFixedSize(true);
        CustomLinearLayoutManager ingredientsLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mIngredientAdapter = new RecipeIngredientAdapter(mIngredientList, mContext);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);


        mDirectionsListView = (ListView) findViewById(R.id.list_view_directions);
        //TODO set adapter
        setupNavDrawer();
        queryParse();
    }


    //NAVBAR
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    //NAVBAR
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupNavDrawer() {
        //Find views, set adapter
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.navigation_items));
        leftDrawerList.setAdapter(navigationDrawerAdapter);

        //Setup Toolbar
        if (toolbar != null) {
            toolbar.setTitle("Navigation Drawer");
            setSupportActionBar(toolbar);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Click", String.valueOf(position));
            }
        });
    }

    private void queryParse() {
        if (getIntent().getStringExtra(RECIPE_ID) != null) {
            ParseQuery<ParseRecipe> query = new ParseQuery<ParseRecipe>("Recipe");
            String objectId = getIntent().getStringExtra(RECIPE_ID);
            query.getInBackground(objectId, new GetCallback<ParseRecipe>() {
                @Override
                public void done(ParseRecipe parseRecipe, ParseException e) {
                    mRecipe = parseRecipe;
                    String title = mRecipe.getTitle();
                    mTitleText.setText(title);
                    String gsonRecipe = mRecipe.getGsonRecipe();
                    Gson gson = new Gson();
                    JsonRecipe jsonRecipe = gson.fromJson(gsonRecipe, JsonRecipe.class);
                    List<Ingredient> tempIngredientList = jsonRecipe.getIngredients();
                    if (tempIngredientList.size() > 0) {
                        for (int i = 0; i < tempIngredientList.size(); i++) {
                            mIngredientList.add(tempIngredientList.get(i));
                        }
                        mIngredientsRecyclerView.setVisibility(View.VISIBLE);
                        mIngredientAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
