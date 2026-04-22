package com.hawk.products.data.datasources.remote

import com.hawk.common.environment.AppEnvironment
import com.hawk.products.data.datasources.remote.interfaces.ProductsRemoteDataSource
import com.hawk.products.data.dto.ProductDto
import com.hawk.products.data.mappers.toProductDtos
import com.hawk.utils.coroutines.IoDispatcher
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val json: Json,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ProductsRemoteDataSource {

    override fun getProducts(): Flow<Result<List<ProductDto>>> = flow {
        try {
            val response = productsApi.getProducts(url = AppEnvironment.productsListUrl)
            if (!response.isSuccessful) {
                emit(Result.failure(IOException("Products request failed with code ${response.code()}.")))
                return@flow
            }

            val payload = response.body()?.string().orEmpty()
            if (payload.isBlank()) {
                emit(Result.failure(IOException("Products service returned an empty body.")))
                return@flow
            }

            val products = json.parseToJsonElement(payload).jsonArray.toProductDtos()
            emit(Result.success(products))
        } catch (exception: IOException) {
            emit(Result.failure(exception))
        } catch (exception: Exception) {
            emit(Result.failure(IOException("Unable to parse the products response.", exception)))
        }
    }.flowOn(coroutineDispatcher)
}
