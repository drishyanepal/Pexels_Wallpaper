package com.example.mywallpaper;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mywallpaper.Models.Photo;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class DisplayActivity extends AppCompatActivity {
    Button bt_download, bt_wallpaper;
    ImageView imageView;
    Photo photo;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        bt_download = findViewById(R.id.btn_download);
        bt_wallpaper = findViewById(R.id.btn_wallpaper);
        imageView = findViewById(R.id.imageView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("setting wallpaper");
        progressDialog.setCancelable(false);

        photo = (Photo) getIntent().getSerializableExtra("photo");
        Picasso.get().load(photo.getSrc().getOriginal()).placeholder(R.drawable.placeholder).into(imageView);

        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager downloadManager = null;
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(photo.getSrc().getOriginal());

                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI).setTitle("Wallpaper_" + photo.getId()).setMimeType("image/jpeg").setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "wallpaper_" + photo.getId() + ".jpeg");
                downloadManager.enqueue(request);
                Toast.makeText(DisplayActivity.this, "downloaded successfully", Toast.LENGTH_SHORT).show();
            }
        });

        bt_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(DisplayActivity.this);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(DisplayActivity.this, "Wallpaper Applied", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(DisplayActivity.this, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}