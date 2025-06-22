package com.example.re_estate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.re_estate.R;
import com.example.re_estate.database.Chatroom;
import com.example.re_estate.interfaces.OnClickListener;
import com.example.re_estate.misc.FirebaseUtil;
import com.example.re_estate.misc.Utilities;

import java.util.List;

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.roomHolder>{

    Context context;
    List<Chatroom> chatrooms;
    OnClickListener listener;
    public ChatroomAdapter(Context context, List<Chatroom> chatrooms, OnClickListener listener) {
        this.context = context;
        this.chatrooms = chatrooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public roomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chat_list, parent, false);
        return new roomHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull roomHolder holder, int position) {
        Chatroom chatroom = chatrooms.get(position);
        holder.agent_name.setText(chatroom.getParticipants().get(0));
        holder.chat_time.setText(Utilities.formatTime(chatroom.getLast_message_time()));
        holder.last_message.setText(chatroom.getLast_message());
        FirebaseUtil.userDoc(chatroom.getParticipants_id().get(0)).get()
                .addOnSuccessListener(documentSnapshot ->{
                    if (documentSnapshot.exists()){
                        Glide.with(context).load(documentSnapshot.get("image"))
                                .error(R.drawable.profile_image).into(holder.profile);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return chatrooms.size();
    }

    public static class roomHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView agent_name, chat_time, last_message;
        public roomHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_image);
            agent_name = itemView.findViewById(R.id.agent_name);
            chat_time = itemView.findViewById(R.id.chat_time);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }
}
