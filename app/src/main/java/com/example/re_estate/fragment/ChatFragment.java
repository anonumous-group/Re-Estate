package com.example.re_estate.fragment;

import static com.example.re_estate.misc.FirebaseUtil.chatCol;
import static com.example.re_estate.misc.Utilities.sendMessage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.re_estate.R;
import com.example.re_estate.adapter.ChatroomAdapter;
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
    private boolean chatLoaded = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);

        getChats();

        binding.btnSearch.setOnClickListener(v -> {
            agent = binding.searchBar.getText().toString();

            if (chatLoaded) {
                if (!agent.isEmpty()) {
                    binding.progBar.setVisibility(View.VISIBLE);
                    binding.noChat.setVisibility(View.GONE);
                    binding.chatRv.setAdapter(null);
                    binding.chatRv.setVisibility(View.GONE);
                    getChats();
                }
            } else {
                sendMessage(getContext(), "Database in progress");
            }
        });

        binding.searchCloseBtn.setOnClickListener(v -> {

            if (chatLoaded) {
                binding.searchBar.setText("");
                agent = "";
                binding.searchCloseBtn.setVisibility(View.GONE);
                binding.progBar.setVisibility(View.VISIBLE);
                binding.noChat.setVisibility(View.GONE);
                binding.chatRv.setAdapter(null);
                binding.chatRv.setVisibility(View.GONE);
                binding.searchBar.clearFocus();
                getChats();
            } else {
                sendMessage(getContext(), "Database in progress");
            }
        });

        return binding.getRoot();
    }

    private void getChats() {
        Query query = chatCol().orderBy("last_message_time", Query.Direction.DESCENDING);
        chatLoaded = false;
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshots = task.getResult();
                chatLoaded = true;
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
                    if (chatrooms.isEmpty()) {
                        binding.noChat.setVisibility(View.VISIBLE);
                    } else {
                        binding.noChat.setVisibility(View.GONE);
                        ChatroomAdapter adapter = new ChatroomAdapter(getContext(), chatrooms, (chatroom, position) -> {});
                        binding.chatRv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    binding.progBar.setVisibility(View.GONE);
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.noChat.setVisibility(View.VISIBLE);
                }
            } else {
                binding.progBar.setVisibility(View.GONE);
                binding.noChat.setVisibility(View.VISIBLE);
            }
        });
    }
}