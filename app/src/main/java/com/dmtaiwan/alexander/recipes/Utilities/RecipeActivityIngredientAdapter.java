package com.dmtaiwan.alexander.recipes.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 4/5/2015.
 */
public class RecipeActivityIngredientAdapter extends RecyclerView.Adapter<RecipeActivityIngredientAdapter.ViewHolder> {
    private List<Ingredient> mIngredients;
    private Context mContext;

    public RecipeActivityIngredientAdapter(List<Ingredient> ingredients, Context context) {
        this.mIngredients = ingredients;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_ingredient_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Ingredient ingredient = mIngredients.get(position);
        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            holder.quantity.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.fraction.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.unit.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));

        }else{

            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.activity_background));
            holder.quantity.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.fraction.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.unit.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
            holder.name.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
        }
        //Clear out the view
        holder.quantity.setText("");
        holder.fraction.setText("");
        holder.unit.setText("");
        holder.name.setText("");

        //Load up ingredient values if not a new ingredient item
        if(ingredient.getQuantity()!=0) {
            holder.quantity.setText(String.valueOf(ingredient.getQuantity()) + " ");
        }

        if (ingredient.getFraction()!=null) {
            holder.fraction.setText(ingredient.getFraction() + " ");
        }

        if (ingredient.getUnits()!=null) {
            holder.unit.setText(ingredient.getUnits() + " ");
        }
        holder.name.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        }else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout layout;
        protected TextView quantity;
        protected TextView fraction;
        protected TextView unit;
        protected TextView name;


        public ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.linear_layout_ingredient_list);
            quantity = (TextView) view.findViewById(R.id.text_view_ingredient_list_quantity);
            fraction = (TextView) view.findViewById(R.id.text_view_ingredient_list_fraction);
            unit = (TextView) view.findViewById(R.id.text_view_ingredient_list_unit);
            name = (TextView) view.findViewById(R.id.text_view_ingredient_list_name);

        }
    }
}
