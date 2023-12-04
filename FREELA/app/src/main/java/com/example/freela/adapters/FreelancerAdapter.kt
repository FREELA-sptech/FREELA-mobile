package com.example.freela.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order
import com.example.freela.model.User
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

class FreelancerAdapter(private val freelancers: List<User>) : RecyclerView.Adapter<FreelancerAdapter.FreelancerViewHolder>() {
    var onItemClick : ((User) -> Unit)? = null

    inner class FreelancerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textTitleUser: TextView = itemView.findViewById(R.id.user)
        private val textDescription: TextView = itemView.findViewById(R.id.description)
        private val txtIcon: TextView = itemView.findViewById(R.id.userDetailsWithoutPhoto)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view_subcategories)

        fun bind(freelancer: User) {
            textTitleUser.text = freelancer.name
            textDescription.text = freelancer.description
            if (freelancer.photo == "") {
                txtIcon.text = freelancer.name.first().toString()
                txtIcon.background = itemView.oval(Color.parseColor("#274C77"))
            }else{
                freelancer.photo.let { photoString ->
                    val userDetailsImageView = itemView.findViewById<CircleImageView>(R.id.userDetails)
                    userDetailsImageView?.visibility = View.VISIBLE
                    val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                    txtIcon.visibility = View.GONE
                    userDetailsImageView?.setImageBitmap(bitmap)
                }
            }

            val subCategories = freelancer.subCategories // Suponha que cada freelancer tenha uma lista de subcategorias associadas
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ListSubCategoryAdapter(subCategories)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreelancerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.freelancers_item,parent,false)
        return FreelancerViewHolder(view)
    }

    override fun getItemCount(): Int = freelancers.size

    override fun onBindViewHolder(holder: FreelancerViewHolder, position: Int) {
        holder.bind(freelancers[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(freelancers[position])
        }
    }

}