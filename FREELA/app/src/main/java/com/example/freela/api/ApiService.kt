import com.example.freela.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // USUARIOS

    @POST("/user")
    fun register(@Body userData: User): Call<User>

    @POST("/user/login")
    fun login(@Body userData: User): Call<User>

    @GET("/user/details")
    fun userDetails(): Call<User>

    @GET("/user/details-by-id/{id}")
    fun userDetailsById(@Path("id") id: Int): Call<User>

//    @POST("/user/upload-image")
//    fun uploadPicture(@Part file: MultipartBody.Part): Call<User>

    @PATCH("/user")
    fun updateUser(@Body newUser: User): Call<User>

    @GET("/user/by-subcategories")
    fun getFreelancersByInterests(): Call<User>

    @GET("/proposals/user")
    fun getProposalsByUser(): Call<User>

    @GET("/proposals/user-id/{id}")
    fun getProposalsByUserId(@Path("id") id: Int): Call<User>

    // PEDIDOS

    @GET("/orders")
    fun getOrders(@Query("orderType") orderType: String): Call<ResponseType>

    @GET("/orders/get-all-orders")
    fun getAllOrders(): Call<ResponseType>

    @GET("/orders/by-user")
    fun getOrdersByUser(): Call<ResponseType>

    @GET("/orders/by-user-id/{id}")
    fun getOrdersByUserId(@Path("id") id: Int): Call<ResponseType>

    @POST("/orders")
    fun createOrder(@Body formData: RequestBody): Call<ResponseType>

    @POST("/orders/upload-pictures/{orderId}")
    fun updatePicture(@Part formData: MultipartBody.Part, @Path("orderId") orderId: Int): Call<ResponseType>

    @PUT("/orders/update-pictures/{orderId}")
    fun updatePictures(@Part formData: MultipartBody.Part, @Path("orderId") orderId: Int): Call<ResponseType>

    @GET("/orders/edit/{orderId}")
    fun detailsOrder(@Path("orderId") orderId: Int): Call<ResponseType>

    @DELETE("/orders/{orderId}")
    fun deleteOrder(@Path("orderId") orderId: Int): Call<ResponseType>

    @GET("/orders/edit/{orderId}")
    fun getOrdersById(@Path("orderId") orderId: Int): Call<ResponseType>

    @PUT("/orders/update/{orderId}")
    fun updateOrderById(@Path("orderId") orderId: Int, @Body formData: RequestBody): Call<ResponseType>

    @POST("/orders/{orderId}/{proposalsId}")
    fun aceptProposals(@Path("orderId") orderId: Int, @Path("proposalsId") proposalsId: Int): Call<ResponseType>

    @GET("/orders/by-title/{filter}/{title}")
    fun findByTitle(@Path("filter") filter: String, @Path("title") title: String): Call<ResponseType>

    @GET("/orders/extrato")
    fun extract(): Call<ResponseType>

    // PROPOSTAS

    @POST("/proposals/{orderId}")
    fun sendProposals(@Path("orderId") orderId: Int, @Body formData: RequestBody): Call<ResponseType>

    @DELETE("/proposals/{proposalsId}")
    fun deleteProposals(@Path("proposalsId") proposalsId: Int): Call<ResponseType>

    @PUT("/proposals/update/{proposalsId}")
    fun updateProposals(@Path("proposalsId") proposalsId: Int, @Body formData: RequestBody): Call<ResponseType>

    @PUT("/proposals/refuse-propose/{proposalsId}")
    fun refuseProposal(@Path("proposalsId") proposalsId: Int): Call<ResponseType>

    // Categorias

    @GET("/categories")
    fun getCategories(): Call<ResponseType>

    // Endpoints relacionados a Subcategorias
    @GET("/sub-categories")
    fun getSubCategories(): Call<ResponseType>

    @POST("/sub-categories/txt")
    fun uploadTxt(@Body formData: RequestBody): Call<ResponseType>

    // CHAT

    @GET("/chats")
    fun getChats(): Call<ResponseType>

    @POST("/chats")
    fun createChat(@Body formData: RequestBody): Call<ResponseType>

    @GET("/chats/messages/{id}")
    fun getMessagesById(@Path("id") id: Int): Call<ResponseType>


}
