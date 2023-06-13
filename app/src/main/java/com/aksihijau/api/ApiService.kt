package com.aksihijau.api

import com.aksihijau.api.campaignresponse.CampaignDetailsResponse
import com.aksihijau.api.campaignresponse.CampaignResponse
import com.aksihijau.api.campaignresponse.DonaturResponse
import com.aksihijau.api.campaignresponse.MyCampaignsResponse
import com.aksihijau.api.campaignresponse.ReportResponse
import com.aksihijau.api.campaignresponse.SoilsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("users")
    fun register(@Field("name") name: String,
                 @Field("email") email: String,
                 @Field("password") password: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("sessions")
    fun login(
                 @Field("email") email: String,
                 @Field("password") password: String,
    ): Call<LoginResponse>

    @GET("campaigns")
    fun getCampaigs() : Call<CampaignResponse>

    @GET("campaigns/{slug}")
    fun getCampaignDetails(
        @Path("slug") slug: String
    ): Call<CampaignDetailsResponse>

    @GET("campaigns/{slug}/donations")
    fun getDonatur(
        @Path("slug") slug: String
    ): Call<DonaturResponse>

    @GET("campaigns/{slug}/reports")
    fun getReports(
        @Path("slug") slug: String
    ): Call<ReportResponse>

    @GET("soils/{id}")
    fun getSoil(
        @Path("id") id: Int
    ): Call<SoilsResponse>



    @GET("payments")
    fun getPayments(
    ): Call<PaymentsResponse>

    @GET("donations")
    fun getDonationHistories(
        @Header("Authorization") token : String,
        @Header("x-refresh") refreshToken: String,
        ) : Call<DonationHistoriesResponse>

    @FormUrlEncoded
    @POST("campaigns/{slug}/donations")
    fun createDonation(
        @Header("Authorization") token: String,
        @Header("x-refresh") refreshToken: String,
        @Path("slug") slug: String,
        @Field("amount") amount: Int,
        @Field("paymentType") paymentType: String,
        @Field("paymentMethod") paymentMethod: String
        ) : Call<CreateDonationResponse>

    @GET("donations/{invoice}")
    fun getHistoryDetail(
        @Header("Authorization") token : String,
        @Header("x-refresh") refreshToken: String,
        @Path("invoice") invoice: String
    ) : Call<HistoryDetailResponse>

    @GET("donations/{invoice}/instructions")
    fun getDetailInstruksi(
        @Header("Authorization") token : String,
        @Header("x-refresh") refreshToken: String,
        @Path("invoice") invoice: String
    ) : Call<DetailInstruksiResponse>

    @GET("user")
    fun getUser(
        @Header("Authorization") token : String,
        @Header("x-refresh") refreshToken: String,
    ) : Call<UserResponse>

    @GET("my-campaigns")
    fun getMyCampaigns(
        @Header("Authorization") token : String,
        @Header("x-refresh") refreshToken: String,
    ): Call<MyCampaignsResponse>

}

class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rational-text-381300.et.r.appspot.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}