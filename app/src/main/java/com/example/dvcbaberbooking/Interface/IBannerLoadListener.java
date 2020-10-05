package com.example.dvcbaberbooking.Interface;

import com.example.dvcbaberbooking.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
   void onBannerLoadSuccess(List<Banner> banners);
   void onBannerLoadFailed(String message);

}
