package com.example.re_estate.fragment;

import static com.example.re_estate.misc.FirebaseUtil.chatCol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.database.Chatroom;
import com.example.re_estate.databinding.FragmentChatBinding;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    public ChatFragment() {
        // Required empty public constructor
    }

    private FragmentChatBinding binding;
    private String agent = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void getChats() {
        Query query = chatCol().orderBy("last_message_time", Query.Direction.DESCENDING);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                if (snapshots != null && !snapshots.isEmpty()) {
                    List<Chatroom> chatrooms = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : snapshots) {
                        Chatroom chatroom = snapshot.toObject(Chatroom.class);
                        if (agent.isEmpty()) {
                            chatrooms.add(chatroom);
                        } else {
                            if (chatroom.getParticipants().get(0).toLowerCase(Locale.getDefault())
                                    .contains(agent.toLowerCase(Locale.getDefault()))) {
                                chatrooms.add(chatroom);
                            }
                        }
                    }
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.noChat.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}