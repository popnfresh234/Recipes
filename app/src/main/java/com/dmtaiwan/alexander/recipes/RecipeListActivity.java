package com.dmtaiwan.alexander.recipes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.recipes.Parse.CustomParseAdapter;
import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Alexander on 4/4/2015.
 */

public class RecipeListActivity extends ActionBarActivity {
    public static final String QUERY_CODE = "query_code";
    public static final String MY_RECIPES = "my_recipes";
    public static final String DRUNKEN_MONKEY_RECIPES = "drunken_monkey_recipes";
    public static final String RECENT_RECIPES = "recent_recipes";
    public static final String ALL_RECIPES = "all_recipes";

    private ListView mListView;
    private Context mContext = this;
    private String mQueryCode;

    CustomParseAdapter mAdapter;

    //NavBar items
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;

    //End NavBar Items


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("Calling new Intent", "NEW INTENT");
        setIntent(intent);
        if (getIntent().getStringExtra(QUERY_CODE) != null) {
            Log.i("CODE?", getIntent().getStringExtra(QUERY_CODE));
            mQueryCode = getIntent().getStringExtra(QUERY_CODE);
            //Call this to update the options menu
            invalidateOptionsMenu();
            mListView.setAdapter(new CustomParseAdapter(this, mQueryCode));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get query code to pass to adapter
        if (getIntent().getStringExtra(QUERY_CODE) != null) {
            mQueryCode = getIntent().getStringExtra(QUERY_CODE);
            Log.i("query", mQueryCode);
        }
        setContentView(R.layout.activity_recipe_list);

        //NavDrawer
        setupNavDrawer();
        //end NavDrawer

        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_spinner);

        mListView = (ListView) findViewById(android.R.id.list);


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

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_list, menu);
        if (!ParseUser.getCurrentUser().getObjectId().toString().equals(RecipeEditActivity.DRUNKENMOKEYTW_ID)&&mQueryCode.equals(DRUNKEN_MONKEY_RECIPES)) {
            menu.findItem(R.id.action_new).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            ParseUser.logOut();
            navigateToLogin();
            return true;
        } else if (id == R.id.action_new) {
            Intent i = new Intent(this, RecipeEditActivity.class);
            startActivity(i);
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "onResume");
        mAdapter = new CustomParseAdapter(this, mQueryCode);
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
        //Setup context mode if viewing my recipes
        if (mQueryCode.equals(MY_RECIPES)) {
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    if (checked) {

                    }
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_recipe_list_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int id = item.getItemId();
                    progressBar.setVisibility(View.VISIBLE);
                    if (id == R.id.action_delete) {
                        for (int i = mAdapter.getCount() - 1; i >= 0; i--) {
                            if (mListView.isItemChecked(i)) {
                                ParseRecipe recipe = (ParseRecipe) mAdapter.getItem(i);
                                recipe.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        mAdapter.notifyDataSetChanged();
                                        mListView.setAdapter(mAdapter);
                                    }
                                });
                            }
                        }
                    }
                    mode.finish();
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });

        } else {
            mListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        }
    }

    private void navigateToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }


}
