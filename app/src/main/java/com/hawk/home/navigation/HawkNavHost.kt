package com.hawk.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hawk.authentication.ui.screens.AuthenticationRoute
import com.hawk.authentication.ui.screens.CheckEmailScreen
import com.hawk.authentication.ui.screens.ConnectionErrorScreen
import com.hawk.authentication.ui.screens.ForgotPasswordRoute
import com.hawk.authentication.ui.screens.PasswordResetSuccessScreen
import com.hawk.authentication.ui.screens.SetNewPasswordRoute
import com.hawk.customers.ui.screens.CreateCustomerRoute
import com.hawk.customers.ui.screens.CustomersRoute
import com.hawk.designsystem.composables.layout.HawkWorkspaceSection
import com.hawk.products.ui.screens.CreateProductRoute
import com.hawk.products.ui.screens.ProductsRoute

@Composable
fun HawkNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HawkDestinations.login
    ) {
        composable(HawkDestinations.login) {
            AuthenticationRoute(
                onForgotPasswordClicked = {
                    navController.navigate(HawkDestinations.forgotPassword)
                },
                onProductsAuthenticated = {
                    navController.navigate(HawkDestinations.products) {
                        popUpTo(HawkDestinations.login) {
                            inclusive = true
                        }
                    }
                },
                onConnectionFailure = {
                    navController.navigate(HawkDestinations.connectionError)
                }
            )
        }

        composable(HawkDestinations.forgotPassword) {
            ForgotPasswordRoute(
                onBackToLogin = {
                    navController.popBackStack(HawkDestinations.login, false)
                },
                onNavigateToCheckEmail = { email ->
                    navController.navigate(HawkDestinations.checkEmail(email))
                }
            )
        }

        composable(
            route = HawkDestinations.checkEmail,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email").orEmpty()
            CheckEmailScreen(
                email = email,
                onOpenEmailApp = {
                    navController.navigate(HawkDestinations.setNewPassword)
                },
                onBackToLogin = {
                    navController.popBackStack(HawkDestinations.login, false)
                }
            )
        }

        composable(HawkDestinations.setNewPassword) {
            SetNewPasswordRoute(
                onPasswordResetSuccess = {
                    navController.navigate(HawkDestinations.passwordResetSuccess)
                },
                onBackToLogin = {
                    navController.popBackStack(HawkDestinations.login, false)
                }
            )
        }

        composable(HawkDestinations.passwordResetSuccess) {
            PasswordResetSuccessScreen(
                onBackToLogin = {
                    navController.navigate(HawkDestinations.login) {
                        popUpTo(HawkDestinations.login) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(HawkDestinations.connectionError) {
            ConnectionErrorScreen(
                onRetry = {
                    navController.popBackStack(HawkDestinations.login, false)
                },
                onBackToLogin = {
                    navController.popBackStack(HawkDestinations.login, false)
                }
            )
        }

        composable(HawkDestinations.products) {
            ProductsRoute(
                onCreateProductClicked = {
                    navController.navigate(HawkDestinations.createProduct)
                },
                onWorkspaceSectionSelected = { section ->
                    navigateToWorkspaceSection(navController, section)
                }
            )
        }

        composable(HawkDestinations.customers) {
            CustomersRoute(
                onCreateCustomerClicked = {
                    navController.navigate(HawkDestinations.createCustomer)
                },
                onWorkspaceSectionSelected = { section ->
                    navigateToWorkspaceSection(navController, section)
                }
            )
        }

        composable(HawkDestinations.createProduct) {
            CreateProductRoute(
                onBackToProducts = {
                    navController.navigate(HawkDestinations.products) {
                        popUpTo(HawkDestinations.products) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onWorkspaceSectionSelected = { section ->
                    navigateToWorkspaceSection(navController, section)
                }
            )
        }

        composable(HawkDestinations.createCustomer) {
            CreateCustomerRoute(
                onBackToCustomers = {
                    navController.navigate(HawkDestinations.customers) {
                        popUpTo(HawkDestinations.customers) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onWorkspaceSectionSelected = { section ->
                    navigateToWorkspaceSection(navController, section)
                }
            )
        }
    }
}

private fun navigateToWorkspaceSection(
    navController: NavHostController,
    section: HawkWorkspaceSection
) {
    when (section) {
        HawkWorkspaceSection.Products -> {
            navController.navigate(HawkDestinations.products) {
                popUpTo(HawkDestinations.products) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        HawkWorkspaceSection.Customers -> {
            navController.navigate(HawkDestinations.customers) {
                popUpTo(HawkDestinations.customers) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        HawkWorkspaceSection.Logout -> {
            navController.navigate(HawkDestinations.login) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }

        else -> Unit
    }
}
