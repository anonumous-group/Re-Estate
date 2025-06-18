package com.example.re_estate.misc;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {

    public static String userId() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static CollectionReference userCol() {
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference userDoc(String uid) {
        return userCol().document(uid);
    }

    public static CollectionReference favCol() {
        return userDoc(userId()).collection("favorites");
    }

    public static DocumentReference favDoc(String fid) {
        return favCol().document(fid);
    }

    public static CollectionReference propertyCol() {
        return FirebaseFirestore.getInstance().collection("properties");
    }

    public static DocumentReference propertyDoc(String uid) {
        return propertyCol().document(uid);
    }

    public static StorageReference storageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference userStorageRef(String uid) {
        return storageRef().child("users").child(uid);
    }
}
