package com.example.re_estate.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.util.List;

public class Chatroom implements Parcelable {
    String chatroom_id, last_message, last_message_sender;
    List<String> participants, participants_id;
    Timestamp last_message_time;

    public Chatroom() {
    }

    public Chatroom(String chatroom_id, String last_message, String last_message_sender,
                    List<String> participants, List<String> participants_id, Timestamp last_message_time) {
        this.chatroom_id = chatroom_id;
        this.last_message = last_message;
        this.last_message_sender = last_message_sender;
        this.participants = participants;
        this.participants_id = participants_id;
        this.last_message_time = last_message_time;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_message_sender() {
        return last_message_sender;
    }

    public void setLast_message_sender(String last_message_sender) {
        this.last_message_sender = last_message_sender;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants_id() {
        return participants_id;
    }

    public void setParticipants_id(List<String> participants_id) {
        this.participants_id = participants_id;
    }

    public Timestamp getLast_message_time() {
        return last_message_time;
    }

    public void setLast_message_time(Timestamp last_message_time) {
        this.last_message_time = last_message_time;
    }

    protected Chatroom(Parcel in) {
        chatroom_id = in.readString();
        last_message = in.readString();
        last_message_sender = in.readString();
        participants = in.createStringArrayList();
        last_message_time = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Chatroom> CREATOR = new Creator<>() {
        @Override
        public Chatroom createFromParcel(Parcel in) {
            return new Chatroom(in);
        }

        @Override
        public Chatroom[] newArray(int size) {
            return new Chatroom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(chatroom_id);
        dest.writeString(last_message);
        dest.writeString(last_message_sender);
        dest.writeStringList(participants);
        dest.writeParcelable(last_message_time, flags);
    }
}
