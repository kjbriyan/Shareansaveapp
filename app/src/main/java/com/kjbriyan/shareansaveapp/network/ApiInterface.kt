package com.kjbriyan.shareansaveapp.network

import com.kjbriyan.shareansaveapp.ResponseImage
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("get_memes")
    fun getImage(): Call<ResponseImage>
}