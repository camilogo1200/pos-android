package com.hawk.products.data.datasources.remote

import com.hawk.products.data.dto.CreateProductRequestDto
import com.hawk.products.data.dto.ProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ProductsApi {
    @GET
    suspend fun getProducts(
        @Url url: String
    ): Response<List<ProductDto>>

    @POST
    suspend fun createProduct(
        @Url url: String,
        @Body body: CreateProductRequestDto
    ): Response<ProductDto>
}
