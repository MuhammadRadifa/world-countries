package com.dicoding.worldcountries.data.network.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("info?returns=flag")
    suspend fun getAllCountries(): CountryResponse

    @FormUrlEncoded
    @POST("cities")
    suspend fun getCityByCountry(
        @Field("country") country: String
    ): CityResponse
}