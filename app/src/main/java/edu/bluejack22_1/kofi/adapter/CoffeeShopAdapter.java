package edu.bluejack22_1.kofi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import edu.bluejack22_1.kofi.controller.LikeController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.CoffeeShop;
import edu.bluejack22_1.kofi.model.User;

public class CoffeeShopAdapter extends RecyclerView.Adapter<CoffeeShopAdapter.CoffeeViewHolder> {

    private ArrayList<CoffeeShop> coffeeShops;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private CoffeeShopController coffeeShopController;
    private LikeController likeController;

    public CoffeeShopAdapter(Context context, ArrayList<CoffeeShop> coffeeShops, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.coffeeShops = coffeeShops;
        this.recyclerViewInterface = recyclerViewInterface;
        this.coffeeShopController = new CoffeeShopController();
        this.likeController = new LikeController();
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
            holder.shopDeleteImage.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(cf.getShopName());
        holder.address.setText(cf.getShopAddress());
        Glide.with(holder.itemView).load(cf.getImageUrl()).placeholder(R.drawable.item_place_holder).into(holder.shopImage);

        holder.shopDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coffeeShopController.deleteCoffeeShop(coffeeShops.get(position).getShopId(), ((FragmentActivity)view.getContext()));
            }
        });

        String userId = User.getCurrentUser().getUserId();
        if (cf.getUserFavorites().contains(userId)) {
            holder.favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            holder.favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.favoriteImage.setOnClickListener(view -> {
            if (cf.getUserFavorites().contains(userId)) {
                likeController.removeShopFromFavorite(cf.getShopId(), holder.favoriteImage, position, cf, this);
            } else {
                likeController.addShopToFavorite(cf.getShopId(), holder.favoriteImage, position, cf, this);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return coffeeShops.size();
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        ImageView shopImage, shopDeleteImage, favoriteImage;

        public CoffeeViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.card_shop_name);
            address = itemView.findViewById(R.id.card_shop_address);
            shopImage = itemView.findViewById(R.id.card_shop_image);
            shopDeleteImage = itemView.findViewById(R.id.card_shop_delete);
            favoriteImage = itemView.findViewById(R.id.card_shop_favorite);
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
