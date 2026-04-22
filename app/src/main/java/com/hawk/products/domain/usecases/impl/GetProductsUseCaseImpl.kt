package com.hawk.products.domain.usecases.impl

import com.hawk.products.data.repository.interfaces.ProductsRepository
import com.hawk.products.domain.entities.Product
import com.hawk.products.domain.usecases.interfaces.GetProductsUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetProductsUseCaseImpl @Inject constructor(
    private val productsRepository: ProductsRepository
) : GetProductsUseCase {

    override fun invoke(): Flow<Result<List<Product>>> = productsRepository.getProducts()
}
