package com.hawk.products.data.repository

import com.hawk.products.data.datasources.remote.interfaces.ProductsRemoteDataSource
import com.hawk.products.data.mappers.toDomain
import com.hawk.products.data.repository.interfaces.ProductsRepository
import com.hawk.products.domain.entities.Product
import com.hawk.utils.coroutines.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductsRepositoryImpl @Inject constructor(
    private val productsRemoteDataSource: ProductsRemoteDataSource,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ProductsRepository {

    override fun getProducts(): Flow<Result<List<Product>>> = flow {
        productsRemoteDataSource.getProducts().collect { result ->
            emit(result.map { products -> products.map { it.toDomain() } })
        }
    }.flowOn(coroutineDispatcher)
}
