package edu.bluejack22_1.kofi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.controller.ReplyController;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.interfaces.listeners.CommentListener;
import edu.bluejack22_1.kofi.interfaces.listeners.ReplyListener;
import edu.bluejack22_1.kofi.model.Reply;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {

    private Context context;
    private ArrayList<Reply> replies;
    private RecyclerViewInterface recyclerViewInterface;
    private ReplyListener replyListener;
    private Reply reply;
    private String path;
    public ReplyAdapter(Context context, ArrayList<Reply> replies, RecyclerViewInterface recyclerViewInterface,
                        ReplyListener replyListener, String path) {
        this.context = context;
        this.replies = replies;
        this.recyclerViewInterface = recyclerViewInterface;
        this.replyListener = replyListener;
        this.path = path;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_reply,parent,false);

        return new ReplyViewHolder(view, recyclerViewInterface, replyListener, path, replies);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        reply = replies.get(position);
        holder.nameTxt.setText(reply.getUser().getFullName());
        holder.contentTxt.setText(reply.getContent());
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, contentTxt;
        ImageView deleteBtn;
        EditText replyEdit;
        Button editBtn;
        ReplyController replyController;
        String path;
        public ReplyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface,
                               ReplyListener listener, String path, ArrayList<Reply>replies) {
            super(itemView);
            replyController = new ReplyController();
            this.path = path;
            nameTxt = itemView.findViewById(R.id.card_reply_name);
            contentTxt = itemView.findViewById(R.id.card_reply_content);
            deleteBtn = itemView.findViewById(R.id.card_reply_delete);
            replyEdit = itemView.findViewById(R.id.card_edit_reply);
            editBtn = itemView.findViewById(R.id.edit_reply_btn);
            contentTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyEdit.setText(contentTxt.getText().toString());
                    replyEdit.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.VISIBLE);
                    contentTxt.setVisibility(View.INVISIBLE);
                }
            });
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    replyController.editReply(path, replies.get(pos).getReplyId(), replyEdit.getText().toString(),
                            listener);
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

        }
    }
}
