package com.kjbriyan.shareansaveapp

import com.kjbriyan.shareansaveapp.network.InitRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter (private val view: MainView){

    fun getdata() {
        view.onShowLoading()
        InitRetrofit().getInstance().getImage().enqueue(object : Callback<ResponseImage> {
            override fun onResponse(
                call: Call<ResponseImage>,
                response: Response<ResponseImage>
            ) {
                view.onDataloaded(response.body()?.data?.memes)
                view.onHideLoading()
            }

            override fun onFailure(call: Call<ResponseImage>, t: Throwable) {
                view.onHideLoading()
                view.onDataeror(t)
            }

        })
    }

}