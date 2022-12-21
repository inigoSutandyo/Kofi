package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CommentAdapter;
import edu.bluejack22_1.kofi.controller.CommentController;
import edu.bluejack22_1.kofi.controller.NotificationController;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CommentListener;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Comment;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment implements
        ReviewListener,
        FragmentInterface,
        CommentListener,
        RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ReviewController reviewController;
    private TextView userName, ratingTxt, reviewTxt;
    private EditText updateReview;
    private RatingBar ratingBar;
    private ImageView userImg, backImg, ratingStar;
    private EditText commentTxt;
    private Button commentBtn, editBtn;
    private String shopID, reviewID, userID;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comments;
    private CommentController commentController;
    private NotificationController notificationController;
    private Review review;

    View view;
    public ReviewFragment() {
        // Required empty public constructor
        reviewController = new ReviewController();
        commentController = new CommentController();
        notificationController = new NotificationController();
    }

    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review, container, false);
        Bundle bundle = getArguments();
        shopID = bundle.getString("SHOP_ID");
        reviewID = bundle.getString("REVIEW_ID");
        userID = bundle.getString("USER_ID");
//        Log.d("UserID", userID);
        userName = view.findViewById(R.id.user_name_review_detail);
        userImg = view.findViewById(R.id.user_image_review_detail);
        ratingBar = view.findViewById(R.id.edit_rating);
        ratingStar = view.findViewById(R.id.rating_star);
        reviewTxt = view.findViewById(R.id.content_review_detail);
        ratingTxt = view.findViewById(R.id.rating_review_detail);
        backImg = view.findViewById(R.id.back_review_detail);
        commentTxt = view.findViewById(R.id.text_comment);
        commentBtn = view.findViewById(R.id.comment_btn);
        editBtn = view.findViewById(R.id.edit_review_btn);
        updateReview = view.findViewById(R.id.update_review_detail);
        reviewController.getReview(shopID, reviewID, this);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment coffeeShopFragment = new CoffeeShopFragment();
                Bundle args = new Bundle();
                args.putString("SHOP_ID", shopID);
                coffeeShopFragment.setArguments(args);
                replaceFragment(coffeeShopFragment);
            }
        });
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentTxt.getText().toString().isEmpty()){
                    addComment();
                }
            }
        });

        reviewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userID.equals(User.getCurrentUser().getUserId())){
                    ratingStar.setVisibility(View.INVISIBLE);
                    ratingTxt.setVisibility(View.INVISIBLE);
                    ratingBar.setVisibility(View.VISIBLE);
                    reviewTxt.setVisibility(View.INVISIBLE);
                    updateReview.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                    updateReview.setText(reviewTxt.getText());
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReview(updateReview.getText().toString());
            }
        });
        return view;
    }

    private void addComment(){
        commentController.addComment(shopID, review, commentTxt.getText().toString(), this);
    }
    private void updateReview(String content){
        reviewController.updateReview(content, ratingBar.getRating(), shopID, review, this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView  = view.findViewById(R.id.comment_recycler);
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this.getContext(), comments, this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        commentController.getComments(shopID, reviewID, this);
    }

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {
        if (docSnap.exists()) {
            review = docSnap.toObject(Review.class);
            userName.setText(review.getUser().getFullName());
            Glide.with(view)
                    .load(review.getUser().getImageUrl())
                    .placeholder(R.drawable.item_place_holder)
                    .into(userImg);
            reviewTxt.setText(review.getContent());
            ratingTxt.setText(review.getRating() + " / 5");
        }
    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateReview(Review review) {}

    @Override
    public void onLikedReview(int position) {}

    @Override
    public void onSuccessReview() {
        returnFragment();
    }


    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
        );
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {

        Fragment reviewFragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString("SHOP_ID", shopID);
        args.putString("REVIEW_ID", reviewID);
        reviewFragment.setArguments(args);
        replaceFragment(reviewFragment);
    }

    @Override
    public void onCompleteCommentCollection(QuerySnapshot querySnap) {
        for (QueryDocumentSnapshot document : querySnap) {
            Comment comment = document.toObject(Comment.class);
            comments.add(comment);
        }
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteComment(DocumentSnapshot docSnap) {}

    @Override
    public void onSuccessComment() {
        Log.d("NOTIFICATION", review.getContent() + " " + review.getUser().getFullName());
        notificationController.addNotification(review.getUser().getUserId(), "has commented on your review");
        returnFragment();
    }

    @Override
    public void onItemClick(int position) {
        Comment comment = comments.get(position);
        Fragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        String path = shopID+"/reviews/"+reviewID+"/comments/"+comment.getCommentId();
        args.putString("PATH", path);
        args.putString("COMMENT_ID", comment.getCommentId());
        args.putString("NAME", comment.getUser().getFullName());
        args.putString("IMAGE", comment.getUser().getImageUrl());
        args.putString("SHOP_ID", shopID);
        args.putString("REVIEW_ID", reviewID);
        args.putString("USER_ID", userID);
        commentFragment.setArguments(args);
        replaceFragment(commentFragment);
    }

    @Override
    public void onClickDelete(int position) {
        CommentController controller = new CommentController();
        controller.deleteComment(shopID, reviewID, comments.get(position).getCommentId(), this);
    }
}