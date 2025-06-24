package com.example.re_estate.activity;

import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;
import static com.example.re_estate.misc.FirebaseUtil.userStorageRef;
import static com.example.re_estate.misc.Utilities.sendMessage;
import static com.example.re_estate.misc.Utilities.setInProgress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.re_estate.R;
import com.example.re_estate.database.User;
import com.example.re_estate.databinding.ActivityProfileScreenBinding;
import com.example.re_estate.misc.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileScreen extends AppCompatActivity {
    ActivityProfileScreenBinding binding;
    String name, email, image, gender, phone;
    String newName;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 0);
            return insets;
        });

        setUpUserProfile();

        binding.selectImage.setOnClickListener(v -> openBottomSheet());
        binding.btnUpdate.setOnClickListener(v -> updateUser());

    }

    private void setUpUserProfile() {
        userDoc(userId()).get().addOnSuccessListener(snapshot -> {
            user = snapshot.toObject(User.class);
            if (user != null) {
                name = user.getName();
                email = user.getEmail();
                phone = user.getPhone();
                gender = user.getGender();
                image = user.getImage();

                binding.name.setText(name);
                binding.phone.setText(phone);
                binding.email.setText(email);
                binding.gender.setText(gender);
                Glide.with(this).load(image)
                        .placeholder(R.drawable.profile_image)
                        .error(R.drawable.profile_image).into(binding.profileImage);

                binding.progBar.setVisibility(View.GONE);
                binding.profileView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(e -> {
            sendMessage(this, e.getMessage());
        });
    }

    private void updateUser() {
        newName = binding.name.getText().toString().trim();

        if (!newName.isEmpty()) {
            if (!name.equals(newName)) {
                setInProgress(binding.btnUpdate, binding.progressBar, true);
                userDoc(userId()).update("name", newName).addOnCompleteListener(task -> {
                    setInProgress(binding.btnUpdate, binding.progressBar, false);
                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        sendMessage(this, task.getException().getMessage());
                    }
                });
            }
        }
    }

    private void openBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.image_option);
        dialog.show();

        dialog.findViewById(R.id.camera).setOnClickListener(v -> {
            if (Utilities.checkPermission(Manifest.permission.CAMERA, this)) {
                dispatchTakePictureIntent();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
            dialog.dismiss();
        });

        dialog.findViewById(R.id.gallery).setOnClickListener(v -> {
            String permission = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission = Manifest.permission.READ_MEDIA_IMAGES;
            } else {
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }

            if (Utilities.checkPermission(permission, this)) {
                dispatchPickPictureIntent();
            } else {
                requestPermissions(new String[]{permission}, REQUEST_IMAGE_PICK);
            }

            dialog.dismiss();
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Save the Bitmap to local storage
            File file = new File(getExternalFilesDir(null), "image.jpg");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Get the URL of the saved image
            Uri imageUri = Uri.fromFile(file);
            image = imageUri.toString();

            //show the image
            Glide.with(this).load(image).into(binding.profileImage);

            //update the image database;
            userStorageRef(userId()).putFile(Uri.parse(image)).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userDoc(userId()).update("image", task.getResult().toString());
                        Utilities.sendMessage(this, "Profile image changed");
                    } else {
                        Utilities.sendMessage(this, "Something went wrong");
                    }
                });
            });
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            image = imageUri.toString();
            Glide.with(this).load(image).into(binding.profileImage);

            //update the image database;
            userStorageRef(userId()).putFile(Uri.parse(image)).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userDoc(userId()).update("image", task.getResult().toString());
                        Utilities.sendMessage(this, "Profile image changed");
                    } else {
                        Utilities.sendMessage(this, "Something went wrong");
                    }
                });
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Utilities.sendMessage(this, "Permission Denied");
            }
        } else if (requestCode == REQUEST_IMAGE_PICK){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchPickPictureIntent();
            } else {
                Utilities.sendMessage(this, "Permission Denied");
            }
        }
    }
}