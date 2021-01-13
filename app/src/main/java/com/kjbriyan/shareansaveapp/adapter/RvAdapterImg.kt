package com.kjbriyan.shareansaveapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kjbriyan.shareansaveapp.detail.DetailActivity
import com.kjbriyan.shareansaveapp.MemesItem
import com.kjbriyan.shareansaveapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_img.view.*


class RvAdapterImg(var data: List<MemesItem>?) :
    RecyclerView.Adapter<RvAdapterImg.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_img, parent, false)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(data?.get(position))
    }

    override fun getItemCount(): Int = data?.size ?: 0


    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(get: MemesItem?) {

//            Picasso.get().load(InitRetrofit().getInstance()+data).into(itemView.imageView)

            Picasso.get().load(get?.url).into(itemView.imageView)
            Log.d("link","a "+get?.name?.get(0))
            itemView.ctl.setOnClickListener {
                val i = Intent(itemView.context, DetailActivity::class.java)
//                i.putExtra("id",get?.id)
                i.putExtra("img",get?.url.toString())
//                i.putExtra("ket",get?.keterangan)
                itemView.context.startActivity(i)
            }


        }
    }
}
