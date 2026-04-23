package com.hawk.customers.data.datasources.remote

import com.hawk.customers.data.dto.CreateCustomerDto
import com.hawk.customers.data.dto.CreateCustomersRequestDto
import com.hawk.customers.data.dto.CustomerDto
import com.hawk.customers.data.dto.CustomersListResponseDto
import kotlinx.serialization.json.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface CustomersApi {
    @GET
    suspend fun getCustomers(
        @Url url: String
    ): Response<List<CustomerDto>>

    @POST
    suspend fun createCustomers(
        @Url url: String,
        @Body body: CreateCustomerDto
    ): Response<CustomerDto>
}
