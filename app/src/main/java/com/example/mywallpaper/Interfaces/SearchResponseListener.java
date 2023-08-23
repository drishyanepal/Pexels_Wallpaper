package com.example.mywallpaper.Interfaces;

import com.example.mywallpaper.Models.SearchResponseModel;

public interface SearchResponseListener {
    void onFetch(SearchResponseModel searchResponseModel, String message);

    void onError(String message);
}
