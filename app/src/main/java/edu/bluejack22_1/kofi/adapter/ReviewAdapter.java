package edu.bluejack22_1.kofi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private ArrayList<Review> reviews;
    private Context context;
    private RecyclerViewInterface recyclerViewInterface;
    private ReviewListener reviewListener;
    private ReviewController controller;

    public ReviewAdapter() {

    }

    public ReviewAdapter(Context context, ArrayList<Review> reviews,
                         RecyclerViewInterface recyclerViewInterface, ReviewListener reviewListener) {
        this.reviews = reviews;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
        this.reviewListener = reviewListener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_review,parent,false);

        return new ReviewAdapter.ReviewViewHolder(view, recyclerViewInterface, reviewListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        if (review == null) return;
        holder.rating.setText(Double.toString(review.getRating()));
        holder.name.setText(review.getUser().getFullName());
        holder.content.setText(review.getContent());
        String userid = User.getCurrentUser().getUserId();
        if(!review.getUser().getUserId().equals(userid)){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
        if(review.getLikers().contains(userid)){
            holder.likeBtn.setImageResource(R.drawable.ic_baseline_thumb_up_24);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView name, rating, content;
        ImageView deleteBtn, likeBtn;

        public ReviewViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, ReviewListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.card_review_name);
            rating = itemView.findViewById(R.id.card_review_rating);
            content = itemView.findViewById(R.id.card_review_content);
            deleteBtn = itemView.findViewById(R.id.card_review_delete);
            likeBtn = itemView.findViewById(R.id.card_review_like);

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onLikedReview(pos);
                    }
                }
            });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onClickDelete(pos);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            });
        }
    }
}
