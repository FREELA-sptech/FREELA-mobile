package com.example.freela.adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.freela.R
import com.example.freela.model.Order
import com.example.freela.view.UserDetailsActivity
import de.hdodenhof.circleimageview.CircleImageView
import android.util.Base64
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.model.Session.user
import java.io.ByteArrayInputStream

class ProposalAdapter(private val proposals: MutableList<Proposals>) : RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder>() {
    var onItemClick : ((Proposals, String) -> Unit)? = null
    inner class ProposalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val textTitleUser: TextView = itemView.findViewById(R.id.user)
        private val textPrize: TextView = itemView.findViewById(R.id.prize)
        private val textDeadline: TextView = itemView.findViewById(R.id.deadlineValue)
        private val txtIcon: TextView = itemView.findViewById(R.id.userDetailsWithoutPhoto)
        private val action: ConstraintLayout = itemView.findViewById(R.id.action)

        fun bind(proposals: Proposals) {
            if(proposals.user?.name == Session.user?.name){
                textTitleUser.text = proposals.order?.title
            }else{
                textTitleUser.text = proposals.user?.name
                action.visibility = View.GONE
            }
            textPrize.text = "R$${proposals.value}"
            textDeadline.text = proposals.deadline

            if (proposals.user?.photo == "") {
                txtIcon.text = proposals.user?.name?.first().toString()
                txtIcon.background = itemView.oval(Color.parseColor("#274C77"))
            }else{
                proposals.user?.photo?.let { photoString ->
                    val userDetailsImageView = itemView.findViewById<CircleImageView>(R.id.userDetails)
                    userDetailsImageView?.visibility = View.VISIBLE
                    val byteArray = Base64.decode(photoString, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
                    txtIcon.visibility = View.GONE
                    userDetailsImageView?.setImageBitmap(bitmap)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProposalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.proposals_item,parent,false)
        return ProposalViewHolder(view)
    }

    override fun getItemCount(): Int = proposals.size

    override fun onBindViewHolder(holder: ProposalViewHolder, position: Int) {
        holder.bind(proposals[position])
        holder.itemView.setOnClickListener{
            onItemClick?.invoke(proposals[position], "itemClick")
        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.action).setOnClickListener{
            onItemClick?.invoke(proposals[position], "actionClick")
        }

    }
}

fun View.oval(@ColorInt color: Int): ShapeDrawable? {
    val oval = ShapeDrawable(OvalShape())
    with(oval){
        intrinsicHeight = height
        intrinsicWidth = width
        paint.color = color
    }
    return oval
}
