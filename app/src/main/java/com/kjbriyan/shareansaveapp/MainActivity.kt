package com.kjbriyan.shareansaveapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kjbriyan.shareansaveapp.adapter.RvAdapterImg
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pb_bar.visibility = View.GONE
        val presenter = MainPresenter(this)
        swipeRefreshLayout.setOnRefreshListener {
            presenter.getdata()
        }
    }

    override fun onShowLoading() {
//       pb_bar.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = true
    }

    override fun onHideLoading() {
//        pb_bar.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onDataloaded(results: List<MemesItem>?) {
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        rv_img.setLayoutManager(gridLayoutManager)
        rv_img.adapter = RvAdapterImg(results)
    }

    override fun onDataeror(t: Throwable) {
        Toasty.error(this, t.cause.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        val presenter = MainPresenter(this)
        presenter.getdata()
    }
}