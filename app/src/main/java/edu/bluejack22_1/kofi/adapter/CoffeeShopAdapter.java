package edu.bluejack22_1.kofi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.CoffeeShopController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.User;

public class CoffeeShopAdapter extends RecyclerView.Adapter<CoffeeShopAdapter.CoffeeViewHolder>{

    private ArrayList<CoffeeShop> coffeeShops;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private CoffeeShopController controller;
    public CoffeeShopAdapter(Context context, ArrayList<CoffeeShop> coffeeShops, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.coffeeShops = coffeeShops;
        this.recyclerViewInterface = recyclerViewInterface;
        this.controller = new CoffeeShopController();
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_coffee_shop_item,parent,false);

        return new CoffeeViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CoffeeShop cf = coffeeShops.get(position);
        if(User.getCurrentUser().getRole().equals("User")){
            holder.shopdeleteImage.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(cf.getShopName());
        holder.address.setText(cf.getShopAddress());
        Glide.with(holder.itemView).load(cf.getImageUrl()).placeholder(R.drawable.itemplaceholder).into(holder.shopImage);
        Log.d("Delete Coffee", coffeeShops.get(position).getShopId());
        holder.shopdeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteCoffeeShop(coffeeShops.get(position).getShopId(), ((FragmentActivity)view.getContext()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return coffeeShops.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        ImageView shopImage, shopdeleteImage;

        public CoffeeViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.card_shop_name);
            address = itemView.findViewById(R.id.card_shop_address);
            shopImage = itemView.findViewById(R.id.card_shop_image);
            shopdeleteImage = itemView.findViewById(R.id.card_shop_delete);
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
