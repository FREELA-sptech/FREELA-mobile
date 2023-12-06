package com.example.freela.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.freela.R

class ViewAdapterOrder(
    private val context: Context,
    private val imageUrls: ArrayList<Uri>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.show_images_items, container, false)
        val imageView = view.findViewById<ImageView>(R.id.uploadImage)
        Glide.with(context).load(imageUrls[position]).into(imageView)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
