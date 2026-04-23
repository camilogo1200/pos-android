package com.hawk.products.data.repository

import com.hawk.products.data.datasources.remote.interfaces.ProductsRemoteDataSource
import com.hawk.products.data.mappers.toCreateProductRequestDto
import com.hawk.products.data.mappers.toDomain
import com.hawk.products.domain.entities.CreateProductDraft
import com.hawk.products.domain.entities.Product
import com.hawk.products.domain.errors.ProductWriteException
import com.hawk.products.domain.repository.interfaces.ProductsRepository
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.network.NetworkManager
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ProductsRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val productsRemoteDataSource: ProductsRemoteDataSource,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ProductsRepository {

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        if (!networkManager.isNetworkAvailable()) {
            emit(Result.failure(IOException("No internet connection. Check your connection and try again.")))
        } else {
            emitAll(loadProductsFromRemote())
        }
    }.flowOn(coroutineDispatcher)

    override fun createProduct(product: CreateProductDraft): Flow<Result<Unit>> = flow {
        if (!networkManager.isNetworkAvailable()) {
            emit(Result.failure(ProductWriteException.NoConnection))
        } else {
            emitAll(createProductRemotely(product))
        }
    }.flowOn(coroutineDispatcher)

    private fun loadProductsFromRemote(): Flow<Result<List<Product>>> =
        productsRemoteDataSource.getProducts()
            .map { result -> result.map { products -> products.map { it.toDomain() } } }
            .flowOn(coroutineDispatcher)

    private fun createProductRemotely(product: CreateProductDraft): Flow<Result<Unit>> =
        productsRemoteDataSource.createProduct(product.toCreateProductRequestDto())
            .map { result -> result.map { Unit } }
            .flowOn(coroutineDispatcher)
}
