package com.dmtaiwan.alexander.recipes.Utilities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 3/31/2015.
 */
public class MainActivityIngredientAdapter extends RecyclerView.Adapter<MainActivityIngredientAdapter.ViewHolder>{

    private List<String> mIngredients;

    public MainActivityIngredientAdapter(List<String> ingredients) {
        this.mIngredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_list_item, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String ingredient = mIngredients.get(position);
        holder.vTitle.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if(mIngredients!=null ){
            return mIngredients.size();
        }else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;

        public ViewHolder(View view) {
            super(view);
            vTitle = (TextView)view.findViewById(R.id.card_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("test", vTitle.getText().toString());
                }
            });
        }
    }
}