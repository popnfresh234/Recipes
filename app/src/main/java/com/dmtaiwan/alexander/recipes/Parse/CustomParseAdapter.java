package com.dmtaiwan.alexander.recipes.Parse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Alexander on 4/4/2015.
 */
public class CustomParseAdapter extends ParseQueryAdapter<ParseRecipe> {
    public CustomParseAdapter(Context context) {

// Use the QueryFactory to construct a PQA that will show all restaurants

        super(context, new ParseQueryAdapter.QueryFactory<ParseRecipe>() {
            public ParseQuery create() {

                ParseQuery<ParseRecipe> query = new ParseQuery<ParseRecipe>("Recipe");
                query.whereExists(ParseConstants.KEY_GSON_RECIPE);
                query.addAscendingOrder(ParseConstants.KEY_GSON_RECIPE);
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
