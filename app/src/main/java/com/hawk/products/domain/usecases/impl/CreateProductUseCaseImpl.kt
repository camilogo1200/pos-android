package com.hawk.products.domain.usecases.impl

import com.hawk.products.domain.entities.CreateProductDraft
import com.hawk.products.domain.repository.interfaces.ProductsRepository
import com.hawk.products.domain.usecases.interfaces.CreateProductUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CreateProductUseCaseImpl @Inject constructor(
    private val productsRepository: ProductsRepository
) : CreateProductUseCase {

    override fun invoke(product: CreateProductDraft): Flow<Result<Unit>> =
        productsRepository.createProduct(product)
}
