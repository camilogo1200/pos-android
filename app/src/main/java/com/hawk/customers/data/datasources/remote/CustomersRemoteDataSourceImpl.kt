package com.hawk.customers.data.datasources.remote

import android.icu.util.GregorianCalendar
import com.hawk.common.environment.AppEnvironment
import com.hawk.customers.data.datasources.remote.interfaces.CustomersRemoteDataSource
import com.hawk.customers.data.dto.CreateCustomersRequestDto
import com.hawk.customers.data.dto.CustomersListResponseDto
import com.hawk.customers.data.dto.CustomersPageDto
import com.hawk.customers.data.dto.CustomersRequestContextDto
import com.hawk.customers.domain.errors.CustomerReadException
import com.hawk.customers.domain.errors.CustomerWriteException
import com.hawk.utils.coroutines.IoDispatcher
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CustomersRemoteDataSourceImpl @Inject constructor(
    private val customersApi: CustomersApi,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : CustomersRemoteDataSource {

    override fun getCustomers(): Flow<Result<CustomersListResponseDto>> = flow {
        try {
            val response = customersApi.getCustomers(url = AppEnvironment.customersListPath)
            if (!response.isSuccessful) {
                emit(Result.failure(CustomerReadException.RequestFailed(response.code())))
                return@flow
            }

            val body = response.body()
            if (body == null) {
                emit(Result.failure(CustomerReadException.RequestFailed(response.code())))
                return@flow
            }

            //provisional change
            var dtoWrapper = CustomersListResponseDto(
                CustomersRequestContextDto(), body,
                CustomersPageDto()
            )

            emit(Result.success(dtoWrapper))
        } catch (exception: IOException) {
            emit(Result.failure(CustomerReadException.RequestFailed()))
        } catch (exception: Exception) {
            emit(Result.failure(CustomerReadException.RequestFailed()))
        }
    }.flowOn(coroutineDispatcher)

    @OptIn(ExperimentalUuidApi::class)
    override fun createCustomers(
        request: CreateCustomersRequestDto
    ): Flow<Result<Unit>> = flow {
        try {
            val id = Uuid.random().toString()
            var customer = request.customers[0]
            customer = customer.copy(
                customerId = id,
                customerCode = id,
                companyId = "company-hawk-001"
            )
            val response = customersApi.createCustomers(
                url = AppEnvironment.customersCreatePath,
                body = customer
            )
            if (!response.isSuccessful) {
                emit(Result.failure(CustomerWriteException.RequestFailed(response.code())))
                return@flow
            }

            emit(Result.success(Unit))
        } catch (exception: IOException) {
            emit(Result.failure(CustomerWriteException.RequestFailed()))
        } catch (exception: Exception) {
            emit(Result.failure(CustomerWriteException.RequestFailed()))
        }
    }.flowOn(coroutineDispatcher)
}
