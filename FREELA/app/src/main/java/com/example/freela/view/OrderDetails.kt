package com.example.freela.view

import MyViewAdapter
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.freela.R
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.freela.R
import com.example.freela.adapters.CarouselAdapter
import com.example.freela.adapters.ListSubCategoryAdapter
import com.example.freela.adapters.oval
import com.example.freela.api.OrderService
import com.example.freela.api.ProposalsService
import com.example.freela.databinding.ActivityOrderDetailsBinding
import com.example.freela.model.Order
import com.example.freela.model.Proposals
import com.example.freela.model.Session
import com.example.freela.model.dto.request.ProposalRequest
import com.example.freela.network.RetrofitClient
import com.example.freela.util.helpers.Helpers
import com.example.freela.viewModel.OrderViewModel
import com.example.freela.viewModel.ProposalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayInputStream

class OrderDetails : AppCompatActivity() {
    private val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var myViewPagerAdapter: MyViewAdapter
    private lateinit var proposalViewModel: ProposalViewModel
    private lateinit var orderViewModel: OrderViewModel
    private val helpers = Helpers();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val orderService = RetrofitClient.getInstance().create(OrderService::class.java)
        val proposalsService = RetrofitClient.getInstance().create(ProposalsService::class.java)
        proposalViewModel = ProposalViewModel(proposalsService)
        orderViewModel = OrderViewModel(orderService)

        val orderId = intent.getIntExtra("orderId", -1)

        if (orderId != -1) {
            orderViewModel.getOrderDetails(orderId)
        }
        orderViewModel.orderDetails.observe(this, { orderDetails ->
            updateUIWithOrderDetails(orderDetails)
        })
    }

    private fun showDialog(order: Order) {
        val imageRV: RecyclerView = binding.imgView
        val imageAdapter = CarouselAdapter()
        imageRV.adapter = imageAdapter
        imageAdapter.submitList(order.photos)

        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.activity_create_proposal)
        val close = dialog.findViewById<ImageView>(R.id.btnreturn)
        val register = dialog.findViewById<MaterialButton>(R.id.createProposal)
        val description = dialog.findViewById<TextInputEditText>(R.id.description)
        val deadline = dialog.findViewById<TextInputEditText>(R.id.deadline)
        val value = dialog.findViewById<TextInputEditText>(R.id.value)

        register.setOnClickListener {
            val inputFields = listOf<TextInputEditText>(
                description,
                deadline,
                value,
            )

            if (helpers.isInputValid(inputFields)) {
                if (order != null) {
                    val description = description?.text.toString()
                    val deadline = deadline.text.toString()
                    val value = value.text.toString()
                    val floatValue = value.toFloatOrNull() ?: 0.0f

                    val proposalRequest = ProposalRequest(
                        floatValue,description,deadline
                    )
                    proposalViewModel.createProposal(order.id,proposalRequest)
                    orderViewModel.getOrders()
                }
                onBackPressed()
                dialog.dismiss()
            }

        }

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun updateUIWithOrderDetails(orderDetails: Order) {
        binding.container.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.title.text = orderDetails.title
        binding.description.text = orderDetails.description
        binding.prize.text = "R$${orderDetails.value.toString()}"
        binding.deadlineValue.text = orderDetails.deadline
        binding.user.text = orderDetails.user?.name

        binding.recyclerViewSubcategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewSubcategories.adapter = orderDetails.subCategories?.let { ListSubCategoryAdapter(it) }

        if (orderDetails.user?.photo == "") {
            binding.userDetailsWithoutPhoto.text = orderDetails.user.name.first().toString()
        }else{
            binding.userDetailsWithoutPhoto.visibility = View.GONE
            val userDetailsImageView = binding.userDetails
            userDetailsImageView.visibility = View.VISIBLE
            val byteArray = Base64.decode(orderDetails.user?.photo, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
            userDetailsImageView.setImageBitmap(bitmap)
        }

        val imageRV: RecyclerView = binding.imgView
        val imageAdapter = CarouselAdapter()
        val user = Session.user

        if(user != null){
            if (!orderDetails.photos.isNullOrEmpty()) {
                imageRV.adapter = imageAdapter
                imageAdapter.submitList(orderDetails.photos)
            }

            if(!user.isFreelancer){
                binding.createProposal.visibility = View.GONE
            }

            if(user.id == orderDetails.user?.id){
                Log.i("Esse é o dono", "Dahora legal")

                binding.action.setOnClickListener {
                    showDialogActions(orderDetails)
                }
                binding.createProposal.visibility = View.GONE
                binding.tabLayout.visibility = View.VISIBLE
                binding.viewPager.visibility = View.VISIBLE
                myViewPagerAdapter = MyViewAdapter(this);
                myViewPagerAdapter.setOrder(orderDetails)
                binding.viewPager.adapter = myViewPagerAdapter
                binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        if (tab != null) {
                            binding.viewPager.setCurrentItem(tab.position)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        // Implement your logic here for when a tab is unselected
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        // Implement your logic here for when a tab is reselected
                    }
                })
            }else{
                binding.action.visibility = View.GONE
                binding.createProposal.visibility = View.VISIBLE
            }
        }

        binding.btnReturn.setOnClickListener{
            val intent = Intent(this, BaseAuthenticatedActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.createProposal.setOnClickListener{
            showDialog(orderDetails)
        }
    }

    private fun showDialogActions(order: Order) {
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.actions_proposals)
        val close = dialog.findViewById<ImageView>(R.id.close)
        val edit = dialog.findViewById<RelativeLayout>(R.id.editar)
        val delete = dialog.findViewById<RelativeLayout>(R.id.delete)
        val details = dialog.findViewById<RelativeLayout>(R.id.details)

        close.setOnClickListener {
            dialog.dismiss()
        }

        details.visibility = View.GONE

        edit.setOnClickListener {
            dialog.dismiss()
            Snackbar.make(binding.root, "Editar", Snackbar.LENGTH_SHORT).show()
        }

        delete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Tem certeza de que deseja excluir a proposta?")
                .setCancelable(false)
                .setPositiveButton("Sim") { _, _ ->
//                    orderViewModel.deleteOrder(order.id)
                    Snackbar.make(binding.root, "Proposta deletada com sucesso", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Não") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        dialog.show();
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

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

}