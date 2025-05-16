package kr.jhp.purchtrac.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kr.jhp.purchtrac.ui.navigation.Screen
import kr.jhp.purchtrac.ui.screens.account.detail.AccountDetailScreen
import kr.jhp.purchtrac.ui.screens.account.list.AccountListScreen
import kr.jhp.purchtrac.ui.screens.memo.detail.MemoDetailScreen
import kr.jhp.purchtrac.ui.screens.memo.list.MemoListScreen
import kr.jhp.purchtrac.ui.screens.product.detail.ProductDetailScreen
import kr.jhp.purchtrac.ui.screens.product.list.ProductListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        Screen.MemoList,
        Screen.ProductList,
        Screen.AccountList
    )

    val showBottomBar = remember(currentDestination) {
        bottomNavItems.any { screen ->
            currentDestination?.hierarchy?.any { it.route == screen.route } == true
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.label
                                )
                            },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == screen.route
                            } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MemoList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 메모 목록 화면
            composable(Screen.MemoList.route) {
                MemoListScreen(
                    navigateToMemoDetail = { memoId ->
                        navController.navigate(
                            Screen.MemoDetail.createRoute(memoId ?: -1L)
                        )
                    }
                )
            }

            // 메모 상세 화면
            composable(
                route = Screen.MemoDetail.route + "/{memoId}",
                arguments = listOf(
                    navArgument("memoId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val memoId = backStackEntry.arguments?.getLong("memoId") ?: -1L
                MemoDetailScreen(
                    memoId = if (memoId == -1L) null else memoId,
                    navigateBack = { navController.popBackStack() }
                )
            }

            // 예약 구매 목록 화면
            composable(Screen.ProductList.route) {
                ProductListScreen(
                    navigateToProductDetail = { productId ->
                        navController.navigate(
                            Screen.ProductDetail.createRoute(productId ?: -1L)
                        )
                    }
                )
            }

            // 예약 구매 상세 화면
            composable(
                route = Screen.ProductDetail.route + "/{productId}",
                arguments = listOf(
                    navArgument("productId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId") ?: -1L
                ProductDetailScreen(
                    productId = if (productId == -1L) null else productId,
                    navigateBack = { navController.popBackStack() }
                )
            }

            // 계정 목록 화면
            composable(Screen.AccountList.route) {
                AccountListScreen(
                    navigateToAccountDetail = { accountId ->
                        navController.navigate(
                            Screen.AccountDetail.createRoute(accountId ?: -1L)
                        )
                    }
                )
            }

            // 계정 상세 화면
            composable(
                route = Screen.AccountDetail.route + "/{accountId}",
                arguments = listOf(
                    navArgument("accountId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getLong("accountId") ?: -1L
                AccountDetailScreen(
                    accountId = if (accountId == -1L) null else accountId,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}