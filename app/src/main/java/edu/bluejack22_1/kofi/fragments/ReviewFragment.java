package edu.bluejack22_1.kofi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.CommentAdapter;
import edu.bluejack22_1.kofi.controller.CommentController;
import edu.bluejack22_1.kofi.controller.ReviewController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CommentListener;
import edu.bluejack22_1.kofi.interfaces.listeners.ReviewListener;
import edu.bluejack22_1.kofi.model.Comment;
import edu.bluejack22_1.kofi.model.Review;

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
    private ImageView userImg, backImg;
    private EditText commentTxt;
    private Button commentBtn;
    private String shopID, reviewID;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> replies;
    private CommentController commentController;
    View view;
    public ReviewFragment() {
        // Required empty public constructor
        reviewController = new ReviewController();
        commentController = new CommentController();
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
        userName = view.findViewById(R.id.user_name_review_detail);
        userImg = view.findViewById(R.id.user_image_review_detail);
        reviewTxt = view.findViewById(R.id.content_review_detail);
        ratingTxt = view.findViewById(R.id.rating_review_detail);
        backImg = view.findViewById(R.id.back_review_detail);
        commentTxt = view.findViewById(R.id.text_comment);
        commentBtn = view.findViewById(R.id.comment_btn);
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
                addComment();
            }
        });
        return view;
    }

    private void addComment(){
        commentController.addComment(shopID, reviewID, commentTxt.getText().toString(), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView  = view.findViewById(R.id.reply_recycler);
        replies = new ArrayList<>();
        commentAdapter = new CommentAdapter(this.getContext(), replies, this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        commentController.getReplies(shopID, reviewID, this);
    }

    @Override
    public void onCompleteReview(DocumentSnapshot docSnap) {
        if (docSnap.exists()) {
            Review review = docSnap.toObject(Review.class);
            userName.setText(review.getUser().getFullName());
            // URL load failed
//            Log.d("IMAGE", review.getUser().getImageUrl());
//            Glide.with(view)
//                    .load(review.getUser().getImageUrl())
//                    .placeholder(R.drawable.itemplaceholder)
//                    .into(userImg);
            reviewTxt.setText(review.getContent());
            ratingTxt.setText(review.getRating() + " / 5");
        }
    }

    @Override
    public void onCompleteReviewCollection(QuerySnapshot querySnap) {}

    @Override
    public void onSuccessUpdateReview(Review review) {}

    @Override
    public void onSuccessReview() {}

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
            replies.add(comment);
        }
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessComment() {
        returnFragment();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClickDelete(int position) {
        CommentController controller = new CommentController();
        controller.deleteCommment(shopID, reviewID, replies.get(position).getCommentId(), this);
    }
}