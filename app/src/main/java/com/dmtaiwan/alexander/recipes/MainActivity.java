package com.dmtaiwan.alexander.recipes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.dmtaiwan.alexander.recipes.Utilities.MainActivityIngredientAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<String> mIngredients = new ArrayList<String>();
    private MainActivityIngredientAdapter mRecyclerMainActivityIngredientAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycleList = (RecyclerView) findViewById(R.id.card_list);
        recycleList.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleList.setLayoutManager(manager);

        mRecyclerMainActivityIngredientAdapter = new MainActivityIngredientAdapter(mIngredients);
        recycleList.setAdapter(mRecyclerMainActivityIngredientAdapter);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getIngredients());
        final AutoCompleteTextView mTextView = (AutoCompleteTextView) findViewById(R.id.auto_complete);
        mTextView.setAdapter(adapter);
        mTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TEST", mTextView.getText().toString());
                if (!mIngredients.contains(mTextView.getText().toString())) {
                    mIngredients.add(mTextView.getText().toString());
                    mRecyclerMainActivityIngredientAdapter.notifyDataSetChanged();
                }

                mTextView.setText("");
            }
        });
    }

    private List<String> getIngredients() {
        List<String> ingredients = new ArrayList<String>();
        ingredients.add("Carrots");
        ingredients.add("Tomatoes");
        ingredients.add("Beef");
        ingredients.add("Chocolate");
        ingredients.add("Protein Powder");
        ingredients.add("Zucchini");
        return ingredients;
    }
}
