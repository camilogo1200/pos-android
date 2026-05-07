package com.hawk.transactions.data.datasources.remote

import com.hawk.transactions.data.dto.CreateTransactionRequestDto
import com.hawk.transactions.data.dto.TransactionsListResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface TransactionsApi {
    @GET
    suspend fun getTransactions(
        @Url url: String
    ): Response<TransactionsListResponseDto>

    @POST
    suspend fun createTransaction(
        @Url url: String,
        @Body body: CreateTransactionRequestDto
    ): Response<Unit>
}
