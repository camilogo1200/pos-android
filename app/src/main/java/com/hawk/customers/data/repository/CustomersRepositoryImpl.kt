package com.hawk.customers.data.repository

import com.hawk.customers.data.datasources.local.CustomerEntity
import com.hawk.customers.data.datasources.local.interfaces.CustomersLocalDataSource
import com.hawk.customers.data.datasources.remote.interfaces.CustomersRemoteDataSource
import com.hawk.customers.data.mappers.toCreateCustomersRequestDto
import com.hawk.customers.data.mappers.toCustomerDirectory
import com.hawk.customers.data.mappers.toDomain
import com.hawk.customers.data.mappers.toEntities
import com.hawk.customers.domain.entities.CreateCustomerDraft
import com.hawk.customers.domain.entities.CustomerDirectory
import com.hawk.customers.domain.errors.CustomerReadException
import com.hawk.customers.domain.errors.CustomerWriteException
import com.hawk.customers.domain.repository.interfaces.CustomersRepository
import com.hawk.utils.coroutines.ApplicationScopeDispatcher
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.network.NetworkManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class CustomersRepositoryImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val customersLocalDataSource: CustomersLocalDataSource,
    private val customersRemoteDataSource: CustomersRemoteDataSource,
    @ApplicationScopeDispatcher private val applicationScope: CoroutineScope,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CustomersRepository {

    override fun getCustomers(): Flow<Result<CustomerDirectory>> = flow {
        if (networkManager.isNetworkAvailable()) {
            emitAll(loadCustomersFromRemote())
        } else {
            emitAll(loadCustomersFromLocal())
        }
    }.flowOn(coroutineDispatcher)

    private fun loadCustomersFromRemote(): Flow<Result<CustomerDirectory>> =
        customersRemoteDataSource.getCustomers()
            .map { result ->
                result.map { response ->
                    response.toEntities()
                        .also(::upsertCustomersAsync)
                    response.toDomain()
                }
            }
            .flowOn(coroutineDispatcher)

    private fun loadCustomersFromLocal(): Flow<Result<CustomerDirectory>> =
        customersLocalDataSource.getCustomers()
            .map { result ->
                result.mapCatching { entities ->
                    if (entities.isEmpty()) {
                        throw CustomerReadException.NoConnection
                    }
                    entities.toCustomerDirectory()
                }
            }
            .flowOn(coroutineDispatcher)

    override fun createCustomers(
        customers: List<CreateCustomerDraft>
    ): Flow<Result<Unit>> = flow {
        if (!networkManager.isNetworkAvailable()) {
            emit(Result.failure(CustomerWriteException.NoConnection))
        } else {
            emitAll(
                customersRemoteDataSource.createCustomers(
                    request = customers.toCreateCustomersRequestDto()
                )
            )
        }
    }.flowOn(coroutineDispatcher)

    private fun upsertCustomersAsync(customers: List<CustomerEntity>) {
        if (customers.isEmpty()) {
            return
        }

        applicationScope.launch(coroutineDispatcher) {
            runCatching {
                customersLocalDataSource.upsertCustomers(customers)
            }
        }
    }
}
