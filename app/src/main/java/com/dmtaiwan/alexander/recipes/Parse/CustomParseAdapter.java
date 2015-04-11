package com.dmtaiwan.alexander.recipes.Parse;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;
import com.dmtaiwan.alexander.recipes.RecipeListActivity;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Alexander on 4/4/2015.
 */
public class CustomParseAdapter extends ParseQueryAdapter<ParseRecipe> {
    public CustomParseAdapter(Context context, final String queryCode) {

// Use the QueryFactory to construct a PQA that will show all restaurants

        super(context, new ParseQueryAdapter.QueryFactory<ParseRecipe>() {
            public ParseQuery create() {
                ParseQuery<ParseRecipe> query = new ParseQuery<ParseRecipe>("Recipe");
                Log.i("WHAT", queryCode);
                if (queryCode.equals(RecipeListActivity.DRUNKEN_MONKEY_RECIPES)) {
                    query.whereExists(ParseConstants.KEY_CREATED_BY_DM);
                    query.addAscendingOrder(ParseConstants.KEY_TITLE_LOWERCASE);

                } else if (queryCode.equals(RecipeListActivity.MY_RECIPES)) {
                    query.whereEqualTo(ParseConstants.KEY_AUTHOR, ParseUser.getCurrentUser());
                    query.addAscendingOrder(ParseConstants.KEY_TITLE_LOWERCASE);
                }
                return query;

            }

        });
    }


    @Override
    public View getItemView(final ParseRecipe recipe, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.parse_list_item, null);
        }
        super.getItemView(recipe, v, parent);

        TextView recipeTitle = (TextView) v.findViewById(R.id.text_view_parse_list_item_title);
        recipeTitle.setText(recipe.getTitle());

        return v;
    }
}
