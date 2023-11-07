package com.example.freela.network

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freela.api.AuthService
import com.example.freela.model.User
import com.example.freela.model.dto.request.UpdateUserRequest
import com.example.freela.model.dto.response.LoginResponse
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserClient  {

    private val client = RetrofitClient.getInstance().create(AuthService::class.java);
    public fun userDetails(){
        client.userDetails()
    }

    public fun updateUser(updateUserRequest: UpdateUserRequest){
//
//        client.updateUser(updateUserRequest).enqueue(object : Callback<User>{
//            override fun onResponse(
//                call: Call<LoginResponse>,
//                response: Response<User>
//        }){
//            if (response.isSuccessful) {
//
//            }else{
//            }
//        }
    }
}