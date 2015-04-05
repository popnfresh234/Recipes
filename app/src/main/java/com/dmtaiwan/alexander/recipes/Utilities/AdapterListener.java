package com.dmtaiwan.alexander.recipes.Utilities;

/**
 * Created by Alexander on 4/3/2015.
 */
public interface AdapterListener {

    public void onMoveIngredientUpClicked(int position);

    public void onIngredientCardViewClicked(int position);

    public void onMoveUpDirectionClicked(int position);

    public void onDirectionCardViewClicked(int position);
}
