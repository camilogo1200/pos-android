package com.hawk.authentication.data.datasources.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface AuthenticationApi {
    @FormUrlEncoded
    @POST
    suspend fun authenticate(
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ResponseBody>
}
