package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.recipes.Parse.CustomParseAdapter;
import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Alexander on 4/5/2015.
 */
public class HomeActivity extends ActionBarActivity {


    private Context mContext = this;

    //nav drawer stuff
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    //end nav drawer

    //list view
    private ListView mListView;
    CustomParseAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkParseUser();
        setContentView(R.layout.activity_home);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_spinner);
        mListView = (ListView) findViewById(R.id.list_view_home);
        setupNavDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            ParseUser.logOut();
            navigateToLogin();
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new CustomParseAdapter(this, RecipeListActivity.RECENT_RECIPES);
        mAdapter.setPaginationEnabled(false);
        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseRecipe>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoaded(List<ParseRecipe> parseRecipes, Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseRecipe recipe = (ParseRecipe) mListView.getItemAtPosition(position);
                String recipeId = recipe.getObjectId();
                Intent i = new Intent(mContext, RecipeActivity.class);
                i.putExtra(RecipeActivity.RECIPE_ID, recipeId);
                startActivity(i);
            }
        });

    }

    private void checkParseUser() {
        //Boot user if null
        if (ParseUser.getCurrentUser() == null) {
            navigateToLogin();
        }
    }

    private void navigateToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
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
}
