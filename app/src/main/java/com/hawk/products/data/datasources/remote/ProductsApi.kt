package com.hawk.products.data.datasources.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ProductsApi {
    @GET
    suspend fun getProducts(
        @Url url: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): Response<ResponseBody>
}
