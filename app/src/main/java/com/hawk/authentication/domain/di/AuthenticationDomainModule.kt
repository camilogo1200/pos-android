package com.hawk.authentication.domain.di

import com.hawk.authentication.domain.usecases.impl.ValidateLoginEmailUseCaseImpl
import com.hawk.authentication.domain.usecases.impl.ValidateLoginPasswordUseCaseImpl
import com.hawk.authentication.domain.usecases.impl.ValidatePasswordConfirmationUseCaseImpl
import com.hawk.authentication.domain.usecases.impl.AuthenticateUserUseCaseImpl
import com.hawk.authentication.domain.usecases.interfaces.AuthenticateUserUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginEmailUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginPasswordUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidatePasswordConfirmationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthenticationDomainModule {

    @Binds
    abstract fun bindValidateLoginEmailUseCase(
        impl: ValidateLoginEmailUseCaseImpl
    ): ValidateLoginEmailUseCase

    @Binds
    abstract fun bindValidateLoginPasswordUseCase(
        impl: ValidateLoginPasswordUseCaseImpl
    ): ValidateLoginPasswordUseCase

    @Binds
    abstract fun bindAuthenticateUserUseCase(
        impl: AuthenticateUserUseCaseImpl
    ): AuthenticateUserUseCase

    @Binds
    abstract fun bindValidatePasswordConfirmationUseCase(
        impl: ValidatePasswordConfirmationUseCaseImpl
    ): ValidatePasswordConfirmationUseCase
}
