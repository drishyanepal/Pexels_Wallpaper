package com.example.mywallpaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywallpaper.Adapters.WallpaperAdapter;
import com.example.mywallpaper.Interfaces.CuratedResponseListener;
import com.example.mywallpaper.Interfaces.OnRecyclerViewClickedListener;
import com.example.mywallpaper.Interfaces.SearchResponseListener;
import com.example.mywallpaper.Models.Photo;
import com.example.mywallpaper.Models.ResponseModel;
import com.example.mywallpaper.Models.SearchResponseModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRecyclerViewClickedListener {
    RequestManager requestManager;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    Button bt_prev, bt_next;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        bt_prev = findViewById(R.id.bt_previous);
        bt_next = findViewById(R.id.bt_next);

        requestManager = new RequestManager(this);
        requestManager.getCuratedWallpapers(listener, "1");


        progressDialog.show();

        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (page > 1) {
                    progressDialog.show();
                    requestManager.getCuratedWallpapers(listener, String.valueOf(page - 1));
                }
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                requestManager.getCuratedWallpapers(listener, String.valueOf(page + 1));
            }
        });

    }

    private CuratedResponseListener listener = new CuratedResponseListener() {
        @Override
        public void onFetch(ResponseModel responseModel, String message) {
            if (responseModel.getPhotos().isEmpty()) {
                Toast.makeText(MainActivity.this, "No image found", Toast.LENGTH_SHORT).show();
                return;
            }
            page = responseModel.getPage();
            progressDialog.dismiss();
            showPhotos(responseModel.getPhotos());
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showPhotos(ArrayList<Photo> photos) {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new WallpaperAdapter(this, photos, this));
    }

    @Override
    public void onClick(Photo photo) {
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra("photo", photo);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestManager.searchWallpapers(searchResponseListener,"1",query);
                progressDialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private final SearchResponseListener searchResponseListener = new SearchResponseListener() {
        @Override
        public void onFetch(SearchResponseModel searchResponseModel, String message) {
            if (searchResponseModel.getPhotos().isEmpty()) {
                Toast.makeText(MainActivity.this, "No image found!", Toast.LENGTH_SHORT).show();
                return;
            }
            showPhotos(searchResponseModel.getPhotos());
            page = searchResponseModel.getPage();
            progressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };
}