package com.dmtaiwan.alexander.recipes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dmtaiwan.alexander.recipes.Parse.ParseRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.AdapterListener;
import com.dmtaiwan.alexander.recipes.Utilities.CustomLinearLayoutManager;
import com.dmtaiwan.alexander.recipes.Utilities.Direction;
import com.dmtaiwan.alexander.recipes.Utilities.Ingredient;
import com.dmtaiwan.alexander.recipes.Utilities.JsonRecipe;
import com.dmtaiwan.alexander.recipes.Utilities.RecipeEditActivityDirectionAdapter;
import com.dmtaiwan.alexander.recipes.Utilities.RecipeEditActivityIngredientAdapter;
import com.google.gson.Gson;
import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 4/4/2015.
 */
public class RecipeEditActivity extends ActionBarActivity implements AdapterListener {
    public static final String GSON_RECIPE = "gson_recipe";
    public static final String RECIPE_ID = "recipe_id";
    public static final String DRUNKENMOKEYTW_ID = "uZq0TrDbkj";

    private Context mContext = this;
    private Toolbar mToolbar;

    private ParseRecipe mRecipe;

    private Button mAddIngredientButton;
    private Button mAddDirectionStepButton;
    private Button mSaveButton;

    private EditText mTitleEditText;
    private Ingredient mIngredient;
    private Direction mDirection;
    private Boolean mNewIngredient = false;
    private Boolean mNewDirection = false;

    private RecyclerView mIngredientsRecyclerView;
    private RecipeEditActivityIngredientAdapter mIngredientAdapter;


    private RecyclerView mDirectionsRecyclerView;
    private RecipeEditActivityDirectionAdapter mDirectionAdapter;

    //Ingredient Dialog Views
    private EditText mDialogQuantityEditText;
    private EditText mDialogIngredientNameEditText;
    private Spinner mDialogFractionSpinner;
    private Spinner mDialogUnitSpinner;
    private Button mDialogAddIngredientButton;
    private Button mDialogCancelIngredientButton;
    private Button mDialogRemoveIngredientButton;

    //Direction Dialog Views

    private EditText mDialogDirectionEditText;
    private Button mDialogAddDirectionButton;
    private Button mDialogCancelDirectionButton;
    private Button mDialogRemoveDirectionButton;

    private InputMethodManager mImm;

    //Setup ArrayList of Ingredients and Direction
    private List<Ingredient> mIngredientList = null;
    private List<Direction> mDirectionList = null;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_recipe_edit);

        //Get input method manager
        mImm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);

        //Setup toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //end toolbar

        mIngredientList = new ArrayList<Ingredient>();
        mDirectionList = new ArrayList<Direction>();
        mTitleEditText = (EditText) findViewById(R.id.edit_text_title);
        mAddIngredientButton = (Button) findViewById(R.id.dialog_button_add_ingredient);
        mAddDirectionStepButton = (Button) findViewById(R.id.button_add_direction_step);
        mSaveButton = (Button) findViewById(R.id.button_save);

        //Setup Recycler Views

        mIngredientsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_ingredients);
        mIngredientsRecyclerView.setHasFixedSize(true);
        CustomLinearLayoutManager ingredientsLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mIngredientAdapter = new RecipeEditActivityIngredientAdapter(mIngredientList, this);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        mDirectionsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_directions);
        mDirectionsRecyclerView.setHasFixedSize(true);
        CustomLinearLayoutManager directionsLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mDirectionsRecyclerView.setLayoutManager(directionsLayoutManager);
        mDirectionAdapter = new RecipeEditActivityDirectionAdapter(mDirectionList, this);
        mDirectionsRecyclerView.setAdapter(mDirectionAdapter);

        mAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateIngredientDialog(null, -1);
            }
        });

        mAddDirectionStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDirectionDialog(null, -1);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitleEditText.getText().toString().trim().length() > 0) {
                    if (mRecipe == null) {
                        Log.i("NewRecipe", "CreatingNewRecipe");
                        mRecipe = new ParseRecipe();
                    }
                    String title = mTitleEditText.getText().toString().trim();
                    JsonRecipe jsonRecipe = JsonRecipe.newInstance();
                    jsonRecipe.setTitle(title);
                    jsonRecipe.setIngredients(mIngredientList);
                    jsonRecipe.setDirections(mDirectionList);
                    Gson gson = new Gson();
                    String gsonRecipe = gson.toJson(jsonRecipe);
                    mRecipe.setGsonRecipe(gsonRecipe);
                    mRecipe.setTitle(title);
                    mRecipe.setTitleLowerCase(title.toLowerCase());
                    mRecipe.setAuthor();
                    //If current user is drunkenmonkeytw set flag
                    String userId = ParseUser.getCurrentUser().getObjectId().toString();
                    if (userId.equals(DRUNKENMOKEYTW_ID)) {
                        mRecipe.setCreatedByDm(true);
                    }
                    mRecipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(mContext, "Please enter a title for your ingredient", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Load up GSON recipe and ParseRecipe if passed in with intent
        if (getIntent().getStringExtra(RECIPE_ID) != null) {
            queryParse();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_signout) {
            ParseUser.logOut();
            navigateToLogin();
            return true;
        } else if (id == R.id.action_delete) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_spinner);
            progressBar.setVisibility(View.VISIBLE);
            mRecipe.deleteInBackground(new DeleteCallback() {
                @Override
                public void done(ParseException e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTitleEditText.getText().toString().trim().length() > 0) {

            //TODO save on pause
        }

    }

    private void queryParse() {
        mTitleEditText.setEnabled(false);
        mAddIngredientButton.setEnabled(false);
        mAddDirectionStepButton.setEnabled(false);
        mSaveButton.setEnabled(false);
        String recipeId = getIntent().getStringExtra(RECIPE_ID);
        ParseQuery<ParseRecipe> query = new ParseQuery<ParseRecipe>("Recipe");
        query.getInBackground(recipeId, new GetCallback<ParseRecipe>() {
            @Override
            public void done(ParseRecipe parseRecipe, ParseException e) {
                mTitleEditText.setEnabled(true);
                mAddIngredientButton.setEnabled(true);
                mAddDirectionStepButton.setEnabled(true);
                mSaveButton.setEnabled(true);

                mRecipe = parseRecipe;
                String gsonRecipe = mRecipe.getGsonRecipe();
                Gson gson = new Gson();
                JsonRecipe jsonRecipe = gson.fromJson(gsonRecipe, JsonRecipe.class);
                Log.i("JSON", jsonRecipe.getTitle());

                //Load Ingredients
                List<Ingredient> tempIngredientList = jsonRecipe.getIngredients();
                if (tempIngredientList.size() > 0) {
                    for (int i = 0; i < tempIngredientList.size(); i++) {
                        mIngredientList.add(tempIngredientList.get(i));
                    }
                    mIngredientsRecyclerView.setVisibility(View.VISIBLE);
                    mIngredientAdapter.notifyDataSetChanged();
                }


                //Load Directions
                List<Direction> tempDirectionList = jsonRecipe.getDirections();
                if (tempDirectionList.size() > 0) {
                    for (int i = 0; i < tempDirectionList.size(); i++) {
                        mDirectionList.add(tempDirectionList.get(i));
                    }
                    mDirectionsRecyclerView.setVisibility(View.VISIBLE);
                    mDirectionAdapter.notifyDataSetChanged();

                }

                mDirectionAdapter.notifyDataSetChanged();
                mTitleEditText.setText(jsonRecipe.getTitle());
            }
        });
    }

    private void CreateDirectionDialog(Direction direction, final int position) {
        if (direction == null) {
            mDirection = Direction.newInstance();
            mNewDirection = true;
        } else mDirection = direction;

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_direction);

        //Setup EditText
        mDialogDirectionEditText = (EditText) dialog.findViewById(R.id.dialog_edit_text_direction);


        if (!mNewDirection) mDialogDirectionEditText.setText(mDirection.getDirection());

        //Add Direction Button

        mDialogAddDirectionButton = (Button) dialog.findViewById(R.id.dialog_button_add_direction);
        if (!mNewDirection) mDialogAddDirectionButton.setText("Update");

        mDialogAddDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewDirection = false;

                if (!mDialogDirectionEditText.getText().toString().equals("")) {
                    mDirection.setDirection(mDialogDirectionEditText.getText().toString().trim());
                    //Hide the keyboard
                    mImm.hideSoftInputFromWindow(mDialogDirectionEditText.getWindowToken(), 0);
                    dialog.dismiss();

                    if (position == -1) {
                        mDirectionList.add(mDirection);
                    } else {
                        mDirectionList.set(position, mDirection);
                    }
                    if (mDirectionList.size() > 0) {
                        mDirectionsRecyclerView.setVisibility(View.VISIBLE);
                    }


                    mDirectionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Please enter an ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Remove Ingredient Button
        mDialogRemoveDirectionButton = (Button) dialog.findViewById(R.id.dialog_button_remove_direction);
        if (!mNewDirection) mDialogRemoveDirectionButton.setVisibility(View.VISIBLE);
        mDialogRemoveDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDirectionList.remove(position);
                mDirectionAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        //Setup cancel button
        mDialogCancelDirectionButton = (Button) dialog.findViewById(R.id.dialog_button_cancel_direction);
        mDialogCancelDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void CreateIngredientDialog(Ingredient ingredient, final int position) {

        if (ingredient == null) {
            mIngredient = Ingredient.newInstance();
            mNewIngredient = true;
        } else {
            mIngredient = ingredient;
        }

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ingredient);

        //Setup EditTexts
        mDialogQuantityEditText = (EditText) dialog.findViewById(R.id.dialog_edit_text_quantity);
        if (!mNewIngredient && mIngredient.getQuantity() != 0) {
            mDialogQuantityEditText.setText(String.valueOf(mIngredient.getQuantity()));
        }

        mDialogIngredientNameEditText = (EditText) dialog.findViewById(R.id.dialog_edit_text_ingredient_name);
        if (!mNewIngredient) {
            mDialogIngredientNameEditText.setText(mIngredient.getName());
        }


        //Setup fraction spinner
        mDialogFractionSpinner = (Spinner) dialog.findViewById(R.id.dialog_spinner_fractions);
        ArrayAdapter<CharSequence> fractionsSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.dialog_spinner_fractions, R.layout.dialog_spinner_item);
        fractionsSpinnerAdapter.setDropDownViewResource(R.layout.dialog_spinner_layout);
        mDialogFractionSpinner.setAdapter(fractionsSpinnerAdapter);
        if (!mNewIngredient && mIngredient.getFraction() != null) {
            String compareValue = mIngredient.getFraction();
            if (!compareValue.equals(null)) {
                int spinnerPosition = fractionsSpinnerAdapter.getPosition(compareValue);
                mDialogFractionSpinner.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }


        }

        //Setup unit spinner
        mDialogUnitSpinner = (Spinner) dialog.findViewById(R.id.dialog_spinner_units);
        ArrayAdapter<CharSequence> unitSpinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.dialog_spinner_units, R.layout.dialog_spinner_item);
        unitSpinnerAdapter.setDropDownViewResource(R.layout.dialog_spinner_layout);
        mDialogUnitSpinner.setAdapter(unitSpinnerAdapter);
        if (!mNewIngredient && mIngredient.getUnits() != null) {
            String compareValue = mIngredient.getUnits();
            if (!compareValue.equals(null)) {
                int spinnerPosition = unitSpinnerAdapter.getPosition(compareValue);
                mDialogUnitSpinner.setSelection(spinnerPosition);
                spinnerPosition = 0;
            }
        }

        //Add ingredient button
        mDialogAddIngredientButton = (Button) dialog.findViewById(R.id.dialog_button_add_ingredient);
        if (!mNewIngredient) mDialogAddIngredientButton.setText("Update");

        mDialogAddIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewIngredient = false;
                mImm.hideSoftInputFromWindow(mDialogQuantityEditText.getWindowToken(), 0);
                mImm.hideSoftInputFromWindow(mDialogIngredientNameEditText.getWindowToken(), 0);
                if (!mDialogIngredientNameEditText.getText().toString().equals("")) {

                    if (!mDialogQuantityEditText.getText().toString().equals("")) {
                        mIngredient.setQuantity(Double.valueOf(mDialogQuantityEditText.getText().toString().trim()));
                    }
                    if (mDialogFractionSpinner.getSelectedItemPosition() != 0) {
                        mIngredient.setFraction(mDialogFractionSpinner.getSelectedItem().toString());
                    }
                    if (!mDialogUnitSpinner.getSelectedItem().toString().equals("Units")) {
                        mIngredient.setUnits(mDialogUnitSpinner.getSelectedItem().toString());
                    }
                    if (!mDialogIngredientNameEditText.getText().toString().equals("")) {
                        mIngredient.setName(mDialogIngredientNameEditText.getText().toString().toLowerCase().trim());
                    }

                    dialog.dismiss();

                    if (position == -1) {
                        mIngredientList.add(mIngredient);
                    } else {
                        mIngredientList.set(position, mIngredient);
                    }
                    if (mIngredientList.size() > 0) {
                        mIngredientsRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mIngredientAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Please enter an ingredient", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Remove Ingredient Button
        mDialogRemoveIngredientButton = (Button) dialog.findViewById(R.id.dialog_button_remove);
        if (!mNewIngredient) mDialogRemoveIngredientButton.setVisibility(View.VISIBLE);
        mDialogRemoveIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIngredientList.remove(position);
                mIngredientAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        //Setup cancel button
        mDialogCancelIngredientButton = (Button) dialog.findViewById(R.id.dialog_button_cancel);
        mDialogCancelIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onMoveIngredientUpClicked(int position) {
        int newPosition = position - 1;
        if (newPosition < 0) return;
        Ingredient ingredient = mIngredientList.get(position);
        mIngredientList.remove(position);
        mIngredientAdapter.notifyItemChanged(position);
        mIngredientList.add(newPosition, ingredient);
        mIngredientAdapter.notifyItemChanged(newPosition);
        mIngredientAdapter.notifyItemMoved(position, newPosition);

    }

    @Override
    public void onIngredientCardViewClicked(int position) {
        Ingredient ingredient = mIngredientList.get(position);
        CreateIngredientDialog(ingredient, position);
    }

    @Override
    public void onMoveUpDirectionClicked(int position) {
        int newPosition = position - 1;
        if (newPosition < 0) return;
        Direction direction = mDirectionList.get(position);
        mDirectionList.remove(position);
        mDirectionAdapter.notifyItemChanged(position);
        mDirectionList.add(newPosition, direction);
        mDirectionAdapter.notifyItemChanged(newPosition);
        mDirectionAdapter.notifyItemMoved(position, newPosition);
    }

    @Override
    public void onDirectionCardViewClicked(int position) {
        Direction direction = mDirectionList.get(position);
        CreateDirectionDialog(direction, position);
    }

    private void navigateToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
