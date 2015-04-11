package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.CustomLinearLayoutManager;
import com.dmtaiwan.alexander.recipes.Utilities.Direction;
import com.dmtaiwan.alexander.recipes.Utilities.Ingredient;
import com.dmtaiwan.alexander.recipes.Utilities.JsonRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.OutlineDecoration;
import com.dmtaiwan.alexander.recipes.Utilities.RecipeActivityDirectionAdapter;
import com.dmtaiwan.alexander.recipes.Utilities.RecipeActivityIngredientAdapter;
import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 4/5/2015.
 */
public class RecipeActivity extends ActionBarActivity {
    public static final String RECIPE_ID = "recipe_id";
    private Context mContext;
    private ParseRecipe mRecipe;
    private Menu mMenu;

    //NavBar items
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    //End NavBar Items

    private TextView mTitleText;
    private RecyclerView mIngredientsRecyclerView;
    private RecipeActivityIngredientAdapter mIngredientAdapter;
    private RecyclerView mDirectionsRecyclerView;
    private RecipeActivityDirectionAdapter mDirectionsAdapter;

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
        mIngredientAdapter = new RecipeActivityIngredientAdapter(mIngredientList, mContext);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);
        RecyclerView.ItemDecoration itemDecoration = new OutlineDecoration(mContext, LinearLayoutManager.VERTICAL);
        mIngredientsRecyclerView.addItemDecoration(itemDecoration);

        //Setup Directions RecyclerView
        mDirectionsRecyclerView = (RecyclerView) findViewById(R.id.list_view_directions);
        mDirectionsRecyclerView.setHasFixedSize(true);
        CustomLinearLayoutManager directionsLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mDirectionsRecyclerView.setLayoutManager(directionsLayoutManager);
        mDirectionsAdapter = new RecipeActivityDirectionAdapter(mDirectionList, mContext);
        mDirectionsRecyclerView.setAdapter(mDirectionsAdapter);

        setupNavDrawer();
        queryParse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        mMenu = menu;
        mMenu.findItem(R.id.action_edit).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            String recipeId = getIntent().getStringExtra(RECIPE_ID);
            Intent i = new Intent(this, RecipeEditActivity.class);
            i.putExtra(RECIPE_ID, recipeId);
            startActivity(i);
            finish();
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavDrawer() {
        //Find views, set adapter
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1, getResources().getStringArray(R.array.navigation_items));
        leftDrawerList.setAdapter(navigationDrawerAdapter);

        //Setup Toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                drawerLayout.closeDrawers();
                switch (position) {
                    case 0:
                        Intent homeIntent = new Intent(mContext, HomeActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        break;

                    case 1:
                        Intent dmRecipes = new Intent(mContext, RecipeListActivity.class);
                        dmRecipes.putExtra(RecipeListActivity.QUERY_CODE, RecipeListActivity.DRUNKEN_MONKEY_RECIPES);
                        dmRecipes.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dmRecipes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dmRecipes);
                        break;
                    case 2:
                        Intent myRecipes = new Intent(mContext, RecipeListActivity.class);
                        myRecipes.putExtra(RecipeListActivity.QUERY_CODE, RecipeListActivity.MY_RECIPES);
                        myRecipes.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myRecipes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myRecipes);
                }
            }
        });
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

    private void queryParse() {
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_spinner);
        progressBar.setVisibility(View.VISIBLE);
        if (getIntent().getStringExtra(RECIPE_ID) != null) {
            ParseQuery<ParseRecipe> query = new ParseQuery<ParseRecipe>("Recipe");
            String objectId = getIntent().getStringExtra(RECIPE_ID);
            query.getInBackground(objectId, new GetCallback<ParseRecipe>() {
                @Override
                public void done(ParseRecipe parseRecipe, ParseException e) {
                    progressBar.setVisibility(View.GONE);
                    mRecipe = parseRecipe;
                    String title = mRecipe.getTitle();
                    mTitleText.setText(title);

                    //Get recipe
                    String gsonRecipe = mRecipe.getGsonRecipe();
                    Gson gson = new Gson();
                    JsonRecipe jsonRecipe = gson.fromJson(gsonRecipe, JsonRecipe.class);

                    //get Ingredients
                    List<Ingredient> tempIngredientList = jsonRecipe.getIngredients();
                    if (tempIngredientList.size() > 0) {
                        for (int i = 0; i < tempIngredientList.size(); i++) {
                            mIngredientList.add(tempIngredientList.get(i));
                        }
                        mIngredientsRecyclerView.setVisibility(View.VISIBLE);
                        mIngredientAdapter.notifyDataSetChanged();
                    }

                    //Get directions
                    List<Direction> tempDirectionList = jsonRecipe.getDirections();
                    if (tempDirectionList.size() > 0) {
                        for (int i = 0; i < tempDirectionList.size(); i++) {
                            mDirectionList.add(tempDirectionList.get(i));
                        }
                        mDirectionsRecyclerView.setVisibility(View.VISIBLE);
                        mDirectionsAdapter.notifyDataSetChanged();
                    }

                    //Enable edit if author
                    String currentUserId = ParseUser.getCurrentUser().getObjectId().toString();
                    String authorId = mRecipe.getAuthor().getObjectId().toString();
                    if (currentUserId.equals(authorId)) {
                        mMenu.findItem(R.id.action_edit).setVisible(true);
                    }
                }
            });
        }
    }
}
