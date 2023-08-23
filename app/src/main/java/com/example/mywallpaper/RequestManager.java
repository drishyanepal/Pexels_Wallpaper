package com.example.mywallpaper;

import android.content.Context;
import android.widget.Toast;

import com.example.mywallpaper.Interfaces.CuratedResponseListener;
import com.example.mywallpaper.Interfaces.SearchResponseListener;
import com.example.mywallpaper.Models.ResponseModel;
import com.example.mywallpaper.Models.SearchResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    private interface ApiService {
        @Headers({
                "Accept: application/json",//desired resource representation(HTML/JSON/XML)
                "Authorization: 563492ad6f91700001000001163bdbaeadb54ea69702ed9645931431"
        })
        @GET("curated/")
        Call<ResponseModel> getWallpapers(
                @Query("page") String pageNo,
                @Query("per_page") String per_page
        );
    }

    private interface ApiServiceSearch {
        @Headers({
                "Authorization: 563492ad6f91700001000001163bdbaeadb54ea69702ed9645931431"
        })
        @GET("search")
        Call<SearchResponseModel> getWallpapers(
                @Query("query") String query,
                @Query("page") String pageNo,
                @Query("per_page") String per_page
        );
    }

    public void getCuratedWallpapers(CuratedResponseListener curatedResponseListener, String page) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseModel> call = apiService.getWallpapers(page, "40");
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Failed Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                curatedResponseListener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                curatedResponseListener.onError(t.getMessage());
            }
        });
    }

    public void searchWallpapers(SearchResponseListener searchResponseListener, String page, String query) {
        ApiServiceSearch apiService = retrofit.create(ApiServiceSearch.class);
        Call<SearchResponseModel> call = apiService.getWallpapers(query, page, "40");
        call.enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Failed Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchResponseListener.onFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                searchResponseListener.onError(t.getMessage());
            }
        });
    }
}