package com.hawk.products.data.datasources.remote

import com.hawk.common.environment.AppEnvironment
import com.hawk.products.data.datasources.remote.interfaces.ProductsRemoteDataSource
import com.hawk.products.data.dto.CreateProductRequestDto
import com.hawk.products.data.dto.ProductDto
import com.hawk.products.domain.errors.ProductWriteException
import com.hawk.utils.coroutines.IoDispatcher
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val productsApi: ProductsApi,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ProductsRemoteDataSource {

    override fun getProducts(): Flow<Result<List<ProductDto>>> = flow {
        try {
            val response = productsApi.getProducts(url = AppEnvironment.productsListPath)
            if (!response.isSuccessful) {
                emit(Result.failure(IOException("Products request failed with code ${response.code()}.")))
                return@flow
            }

            val products = response.body()
            if (products == null) {
                emit(Result.failure(IOException("Products service returned an empty body.")))
                return@flow
            }

            emit(Result.success(products))
        } catch (exception: IOException) {
            emit(Result.failure(exception))
        } catch (exception: Exception) {
            emit(Result.failure(IOException("Unable to load the products response.", exception)))
        }
    }.flowOn(coroutineDispatcher)

    override fun createProduct(product: CreateProductRequestDto): Flow<Result<ProductDto>> = flow {
        try {
            val response = productsApi.createProduct(
                url = AppEnvironment.productsCreatePath,
                body = product
            )

            if (!response.isSuccessful) {
                emit(Result.failure(ProductWriteException.RequestFailed(response.code())))
                return@flow
            }

            val createdProduct = response.body()
            if (createdProduct == null) {
                emit(Result.failure(ProductWriteException.RequestFailed(response.code())))
                return@flow
            }

            emit(Result.success(createdProduct))
        } catch (exception: IOException) {
            emit(Result.failure(ProductWriteException.RequestFailed()))
        } catch (exception: Exception) {
            emit(Result.failure(ProductWriteException.RequestFailed()))
        }
    }.flowOn(coroutineDispatcher)
}
