package com.example.re_estate.activity.auth;

import static com.example.re_estate.misc.FirebaseUtil.userDoc;
import static com.example.re_estate.misc.FirebaseUtil.userId;
import static com.example.re_estate.misc.FirebaseUtil.userStorageRef;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.re_estate.R;
import com.example.re_estate.activity.MainScreen;
import com.example.re_estate.database.User;
import com.example.re_estate.databinding.ActivityCompleteProfileScreenBinding;
import com.example.re_estate.misc.Utilities;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class CompleteProfileScreen extends AppCompatActivity {
    ActivityCompleteProfileScreenBinding binding;
    String name, email, pass, image, gender, phone, ipAddress;
    GeoPoint geoPoint;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this, SystemBarStyle.light(
                getColor(R.color.white), getColor(R.color.white)
        ));
        binding = ActivityCompleteProfileScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.main);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(32, systemBars.top, 32, 32);
            return insets;
        });

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        image = "";
        gender = "";
        phone = "";
        ipAddress = "";
        geoPoint = new GeoPoint(0, 0);

        binding.countryCode.setDialogBackgroundColor(R.color.off_white);
        binding.countryCode.setDialogTextColor(R.color.black);
        binding.countryCode.registerCarrierNumberEditText(binding.phone);
        binding.name.setText(name);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.selectImage.setOnClickListener(v -> openBottomSheet());
        binding.gender.setOnClickListener(v -> openGenderDialog());
        binding.btnDropdown.setOnClickListener(v -> openGenderDialog());
        binding.btnComplete.setOnClickListener(v -> {
            phone = binding.countryCode.getFullNumberWithPlus();
            if (gender.isEmpty()) {
                Utilities.sendMessage(this, "Please select your gender");
                return;
            }
            if (phone.isEmpty()) {
                Utilities.sendMessage(this, "Please enter your phone number");
                return;
            }

            Utilities.setInProgress(binding.btnComplete, binding.progressBar, true);
            if (ipAddress.isEmpty()) {
                getUserIpAddress();
            } else {
                if (image.isEmpty()) {
                    saveUser(image);
                } else {
                    saveUserProfile(image);
                }
            }
        });

    }

    private void getUserIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        ipAddress =  inetAddress.getHostAddress();
                    }
                }
            }

            if (ipAddress.isEmpty()) {
                Utilities.setInProgress(binding.btnComplete, binding.progressBar, false);
                Utilities.sendMessage(this, "Something went wrong");
            } else {
                if (image.isEmpty()) {
                    saveUser(image);
                } else {
                    saveUserProfile(image);
                }
            }
        } catch (SocketException ex) {
            Utilities.sendMessage(this, ex.getMessage());
        }
    }

    private void saveUserProfile(String image) {
        userStorageRef(userId()).putFile(Uri.parse(image)).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    saveUser(task.getResult().toString());
                } else {
                    Utilities.setInProgress(binding.btnComplete, binding.progressBar, false);
                    Utilities.sendMessage(this, "Something went wrong");
                }
            });
        });
    }

    private void saveUser(String image) {
        User user = new User(name, email, pass, userId(), image, gender,
                phone, ipAddress, geoPoint, new ArrayList<>(), Timestamp.now(), Timestamp.now());

        userDoc(userId()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utilities.setInProgress(binding.btnComplete, binding.progressBar, false);
                Utilities.sendMessage(this, "Profile created successfully");
                startActivity(new Intent(this, LocationScreen.class));
                finish();
            } else {
                Utilities.setInProgress(binding.btnComplete, binding.progressBar, false);
                Utilities.sendMessage(this, "Something went wrong");
            }
        });
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

    private void openGenderDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.gender_option);
        dialog.show();

        dialog.findViewById(R.id.male).setOnClickListener(v -> {
            gender = "Male";
            binding.gender.setText(gender);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.female).setOnClickListener(v -> {
            gender = "Female";
            binding.gender.setText(gender);
            dialog.dismiss();
        });

        dialog.findViewById(R.id.binary).setOnClickListener(v -> {
            gender = "Binary";
            binding.gender.setText(gender);
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
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            image = imageUri.toString();
            Glide.with(this).load(image).into(binding.profileImage);
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