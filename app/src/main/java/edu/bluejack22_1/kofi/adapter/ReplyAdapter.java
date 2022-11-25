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
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.model.Reply;
import edu.bluejack22_1.kofi.model.Review;
import edu.bluejack22_1.kofi.model.User;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>{

    private Context context;
    private ArrayList<Reply> replies;
    private RecyclerViewInterface recyclerViewInterface;

    public ReplyAdapter(Context context, ArrayList<Reply> replies, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.replies = replies;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_reply,parent,false);

        return new ReplyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        Reply reply = replies.get(position);
        holder.contentTxt.setText(reply.getContent());
        holder.nameTxt.setText(reply.getUser().getFullName());
        if(!reply.getUser().getUserId().equals(User.getCurrentUser().getUserId())){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, contentTxt;
        ImageView deleteBtn;
        public ReplyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.card_reply_name);
            contentTxt = itemView.findViewById(R.id.card_reply_content);
            deleteBtn = itemView.findViewById(R.id.card_reply_delete);
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
