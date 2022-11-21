package edu.bluejack22_1.kofi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.CoffeeShop;

public class CoffeeShopAdapter extends RecyclerView.Adapter<CoffeeShopAdapter.CoffeeViewHolder>{

    private ArrayList<CoffeeShop> coffeeShops;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;

    public CoffeeShopAdapter(Context context, ArrayList<CoffeeShop> coffeeShops, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.coffeeShops = coffeeShops;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_coffee_shop_item,parent,false);

        return new CoffeeViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        CoffeeShop cf = coffeeShops.get(position);
        holder.name.setText(cf.getShopName());
        holder.address.setText(cf.getShopAddress());
    }

    @Override
    public int getItemCount() {
        return coffeeShops.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        public CoffeeViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.card_shop_name);
            address = itemView.findViewById(R.id.card_shop_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
