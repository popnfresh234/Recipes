package com.dmtaiwan.alexander.recipes.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 4/6/2015.
 */
public class RecipeActivityDirectionAdapter extends RecyclerView.Adapter<RecipeActivityDirectionAdapter.ViewHolder> {
    private List<Direction> mDirections;
    private Context mContext;

    public RecipeActivityDirectionAdapter(List<Direction> directions, Context context) {
        this.mDirections = directions;
        this.mContext = context;
    }

    @Override
    public RecipeActivityDirectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_direction_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeActivityDirectionAdapter.ViewHolder holder, int position) {
        final Direction direction = mDirections.get(position);
        holder.step.setText(String.valueOf(position+1)+".");
        holder.step.setTextColor(mContext.getResources().getColor(R.color.black_87_title));
        holder.direction.setText(direction.getDirection());
        holder.direction.setTextColor(mContext.getResources().getColor(R.color.app_compat_light_text));
    }

    @Override
    public int getItemCount() {
        if (mDirections != null) {
            return mDirections.size();
        }else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView step;
        protected TextView direction;


        public ViewHolder(View view) {
            super(view);

            step = (TextView) view.findViewById(R.id.text_view_direction_list_step);
            direction = (TextView) view.findViewById(R.id.text_view_direction_list_direction);


        }
    }
}
