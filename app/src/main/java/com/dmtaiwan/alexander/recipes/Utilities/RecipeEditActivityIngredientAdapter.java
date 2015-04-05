package com.dmtaiwan.alexander.recipes.Utilities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 4/2/2015.
 */
public class RecipeEditActivityIngredientAdapter extends RecyclerView.Adapter<RecipeEditActivityIngredientAdapter.ViewHolder> {
    private static final String TAG = "Adapter";
    private List<Ingredient> mIngredients;
    private AdapterListener mListener;


    public RecipeEditActivityIngredientAdapter(List<Ingredient> ingredients, AdapterListener listener) {
        this.mIngredients = ingredients;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_edit_ingredient_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Ingredient ingredient = mIngredients.get(position);

        //Listeners which interface with RecipeActivity
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoveIngredientUpClicked(position);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onIngredientCardViewClicked(position);
            }
        });

        //Clear out the view
        holder.quantity.setText("");
        holder.fraction.setText("");
        holder.unit.setText("");
        holder.name.setText("");

        //Load up ingredient values if not a new ingredient item
        if(ingredient.getQuantity()!=0) {

            holder.quantity.setText(String.valueOf(ingredient.getQuantity()) + " ");
            Log.i(TAG + "QUANTITY", String.valueOf(ingredient.getQuantity()));
        }

        if (ingredient.getFraction()!=null) {
            holder.fraction.setText(ingredient.getFraction() + " ");
            Log.i(TAG + "FRACTION", ingredient.getFraction());
        }

        if (ingredient.getUnits()!=null) {
            holder.unit.setText(ingredient.getUnits()+" ");
            Log.i(TAG + "UNITS", ingredient.getUnits());
        }

        holder.name.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        } else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView quantity;
        protected TextView fraction;
        protected TextView unit;
        protected TextView name;
        protected Button button;
        protected CardView cardView;


        public ViewHolder(View view) {
            super(view);
            quantity = (TextView) view.findViewById(R.id.text_view_ingredient_list_edit_quantity);
            fraction = (TextView) view.findViewById(R.id.text_view_ingredient_list_edit_fraction);
            unit = (TextView) view.findViewById(R.id.text_view_ingredient_list_edit_unit);
            name = (TextView) view.findViewById(R.id.text_view_ingredient_list_edit_name);
            button = (Button) view.findViewById(R.id.button_move_up);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }
}
