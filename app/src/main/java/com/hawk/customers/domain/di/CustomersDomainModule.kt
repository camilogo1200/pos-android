package com.hawk.customers.domain.di

import com.hawk.customers.domain.usecases.impl.CreateCustomersUseCaseImpl
import com.hawk.customers.domain.usecases.impl.GetCustomersUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerBirthDateUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerDocumentNumberUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerFirstNameUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerLastNameUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerEmailUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerPhoneUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerNationalityUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerCountryUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerCityUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerAddressLineOneUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerPreferredContactChannelUseCaseImpl
import com.hawk.customers.domain.usecases.impl.ValidateCustomerAcquisitionSourceUseCaseImpl
import com.hawk.customers.domain.usecases.interfaces.CreateCustomersUseCase
import com.hawk.customers.domain.usecases.interfaces.GetCustomersUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerBirthDateUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerDocumentNumberUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerFirstNameUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerLastNameUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerEmailUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPhoneUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerNationalityUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCountryUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCityUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAddressLineOneUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPreferredContactChannelUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAcquisitionSourceUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CustomersDomainModule {

    @Binds
    abstract fun bindGetCustomersUseCase(
        impl: GetCustomersUseCaseImpl
    ): GetCustomersUseCase

    @Binds
    abstract fun bindCreateCustomersUseCase(
        impl: CreateCustomersUseCaseImpl
    ): CreateCustomersUseCase

    @Binds
    abstract fun bindValidateCustomerFirstNameUseCase(
        impl: ValidateCustomerFirstNameUseCaseImpl
    ): ValidateCustomerFirstNameUseCase

    @Binds
    abstract fun bindValidateCustomerLastNameUseCase(
        impl: ValidateCustomerLastNameUseCaseImpl
    ): ValidateCustomerLastNameUseCase

    @Binds
    abstract fun bindValidateCustomerBirthDateUseCase(
        impl: ValidateCustomerBirthDateUseCaseImpl
    ): ValidateCustomerBirthDateUseCase

    @Binds
    abstract fun bindValidateCustomerDocumentNumberUseCase(
        impl: ValidateCustomerDocumentNumberUseCaseImpl
    ): ValidateCustomerDocumentNumberUseCase

    @Binds
    abstract fun bindValidateCustomerEmailUseCase(
        impl: ValidateCustomerEmailUseCaseImpl
    ): ValidateCustomerEmailUseCase

    @Binds
    abstract fun bindValidateCustomerPhoneUseCase(
        impl: ValidateCustomerPhoneUseCaseImpl
    ): ValidateCustomerPhoneUseCase

    @Binds
    abstract fun bindValidateCustomerNationalityUseCase(
        impl: ValidateCustomerNationalityUseCaseImpl
    ): ValidateCustomerNationalityUseCase

    @Binds
    abstract fun bindValidateCustomerCountryUseCase(
        impl: ValidateCustomerCountryUseCaseImpl
    ): ValidateCustomerCountryUseCase

    @Binds
    abstract fun bindValidateCustomerCityUseCase(
        impl: ValidateCustomerCityUseCaseImpl
    ): ValidateCustomerCityUseCase

    @Binds
    abstract fun bindValidateCustomerAddressLineOneUseCase(
        impl: ValidateCustomerAddressLineOneUseCaseImpl
    ): ValidateCustomerAddressLineOneUseCase

    @Binds
    abstract fun bindValidateCustomerPreferredContactChannelUseCase(
        impl: ValidateCustomerPreferredContactChannelUseCaseImpl
    ): ValidateCustomerPreferredContactChannelUseCase

    @Binds
    abstract fun bindValidateCustomerAcquisitionSourceUseCase(
        impl: ValidateCustomerAcquisitionSourceUseCaseImpl
    ): ValidateCustomerAcquisitionSourceUseCase
}
