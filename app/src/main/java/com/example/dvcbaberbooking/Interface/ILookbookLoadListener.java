package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
