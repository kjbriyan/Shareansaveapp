package com.kjbriyan.shareansaveapp

interface MainView {
    fun onShowLoading()
    fun onHideLoading()
    fun onDataloaded(results : List<MemesItem>?)
    fun onDataeror (message : Throwable)
}