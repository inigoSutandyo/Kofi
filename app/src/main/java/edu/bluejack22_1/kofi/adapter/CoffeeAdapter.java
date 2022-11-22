package edu.bluejack22_1.kofi.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.Coffee;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>{

    private ArrayList<Coffee> coffees;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public CoffeeAdapter(){}

    public CoffeeAdapter(Context context, ArrayList<Coffee> coffees, RecyclerViewInterface recyclerViewInterface) {
        this.coffees = coffees;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_coffee, parent, false);
        return new CoffeeAdapter.CoffeeViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        Coffee c = coffees.get(position);
        holder.name.setText(c.getName());
        holder.price.setText(c.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return coffees.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        public CoffeeViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.card_coffee_name);
            price = itemView.findViewById(R.id.card_coffee_price);
        }
    }

}
