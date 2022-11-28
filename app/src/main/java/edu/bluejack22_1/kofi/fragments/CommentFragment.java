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

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.adapter.ReplyAdapter;
import edu.bluejack22_1.kofi.controller.ReplyController;
import edu.bluejack22_1.kofi.interfaces.FragmentInterface;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.ReplyListener;
import edu.bluejack22_1.kofi.model.Reply;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentFragment extends Fragment implements
        ReplyListener,
        RecyclerViewInterface,
        FragmentInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private ReplyAdapter replyAdapter;
    private ArrayList<Reply> replies;
    private ReplyController replyController;

    private String path, name, comment, image;
    private TextView userName, commentTxt;
    private ImageView userImg, backImg;
    private EditText replyTxt;
    private Button replyBtn;
    private String shopId, reviewId;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentFragment() {
        // Required empty public constructor
        replies = new ArrayList<>();
        replyController = new ReplyController();
    }

    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        userImg = view.findViewById(R.id.user_image_comment_detail);
        userName = view.findViewById(R.id.user_name_comment_detail);
        commentTxt = view.findViewById(R.id.comment_content_detail);
        replyTxt = view.findViewById(R.id.reply_text);
        replyBtn = view.findViewById(R.id.reply_btn);
        backImg = view.findViewById(R.id.back_comment_detail);

        Bundle args = getArguments();
        path = args.getString("PATH");
        comment = args.getString("COMMENT");
        name = args.getString("NAME");
        image = args.getString("IMAGE");
        shopId = args.getString("SHOP_ID");
        reviewId = args.getString("REVIEW_ID");
        userName.setText(name);
        commentTxt.setText(comment);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnFragment();
            }
        });
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReply();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        replyController.getReplies(path, this);
        replyAdapter = new ReplyAdapter(this.getContext(), replies, this);
        recyclerView = view.findViewById(R.id.reply_recycler);
        recyclerView.setAdapter(replyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void createReply(){
        ReplyController replycontroller = new ReplyController();
        replycontroller.addReply(path, replyTxt.getText().toString(), this);
    }

    @Override
    public void onCompleteReplyCollection(QuerySnapshot querySnapshot) {
        for (QueryDocumentSnapshot documentSnapshot: querySnapshot) {
            Reply reply = documentSnapshot.toObject(Reply.class);
            replies.add(reply);
        }
        replyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessReply() {
        Bundle bundle = new Bundle();
        bundle.putString("REVIEW_ID",reviewId);
        bundle.putString("SHOP_ID", shopId);
        bundle.putString("PATH", path);
        bundle.putString("COMMENT", comment);
        bundle.putString("NAME", name);
        bundle.putString("IMAGE", image);
        Fragment commentFragment = new CommentFragment();
        commentFragment.setArguments(bundle);
        replaceFragment(commentFragment);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClickDelete(int position) {

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void returnFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("REVIEW_ID",reviewId);
        bundle.putString("SHOP_ID", shopId);
        Fragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundle);
        replaceFragment(reviewFragment);
    }
}