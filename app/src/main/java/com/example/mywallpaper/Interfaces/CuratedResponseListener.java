package com.example.mywallpaper.Interfaces;

import com.example.mywallpaper.Models.ResponseModel;

public interface CuratedResponseListener {
    void onFetch(ResponseModel responseModel,String message);

    void onError(String message);
}
