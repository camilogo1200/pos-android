package com.hawk.home.navigation

import android.net.Uri

object HawkDestinations {
    const val login = "login"
    const val forgotPassword = "forgot_password"
    const val checkEmail = "check_email/{email}"
    const val setNewPassword = "set_new_password"
    const val passwordResetSuccess = "password_reset_success"
    const val connectionError = "connection_error"
    const val products = "products"
    const val createProduct = "create_product"

    fun checkEmail(email: String): String = "check_email/${Uri.encode(email)}"
}
