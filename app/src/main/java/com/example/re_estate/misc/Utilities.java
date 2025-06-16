package com.example.re_estate.misc;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Utilities {

    public static void setInProgress(Button button, ProgressBar progressBar, boolean inProgress) {
        if (inProgress) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public static void sendMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean checkPermission(String permission, Context context) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static String formatPrice(int price) {
        if (price >= 1_000_000_000) {
            return String.format(price % 1_000_000_000 == 0 ? "%.0fB" : "%.1fB", (double) price / 1_000_000_000);
        } else if (price >= 1_000_000) {
            return String.format(price % 1_000_000 == 0 ? "%.0fM" : "%.1fM", (double) price / 1_000_000);
        } else if (price >= 1_000) {
            return String.format(price % 1_000 == 0 ? "%.0fK" : "%.1fK", (double) price / 1_000);
        } else {
            return String.valueOf(price);
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
