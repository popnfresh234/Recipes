package com.dmtaiwan.alexander.recipes.Utilities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dmtaiwan.alexander.recipes.R;

import java.util.List;

/**
 * Created by Alexander on 4/3/2015.
 */
public class RecipeEditActivityDirectionAdapter extends RecyclerView.Adapter<RecipeEditActivityDirectionAdapter.ViewHolder> {
    private static final String TAG = "Adapter";
    private List<Direction> mDirections;
    private AdapterListener mListener;


    public RecipeEditActivityDirectionAdapter(List<Direction> directions, AdapterListener listener) {
        this.mDirections = directions;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_direction_edit_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Direction direction = mDirections.get(position);

        //Listeners which interface with RecipeActivity
        holder.moveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMoveUpDirectionClicked(position);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDirectionCardViewClicked(position);
            }
        });

        //Clear out the view
        holder.number.setText("");
        holder.direction.setText("");

        //Load up ingredient values if not a new ingredient item


        holder.number.setText("Step " + String.valueOf(position + 1));


        if (direction.getDirection() != null) {
            holder.direction.setText(direction.getDirection());
        }
    }

    @Override
    public int getItemCount() {
        if (mDirections != null) {
            return mDirections.size();
        } else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected CardView cardView;
        protected TextView number;
        protected Button moveUpButton;
        protected TextView direction;


        public ViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.text_view_direction_number);
            moveUpButton = (Button) view.findViewById(R.id.button_move_up_direction);
            direction = (TextView) view.findViewById(R.id.text_view_direction);
            cardView = (CardView) view.findViewById(R.id.direction_card_view);
        }
    }
}
