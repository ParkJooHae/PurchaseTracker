package kr.jhp.purchtrac.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    // 메모 관련 화면
    object MemoList : Screen(
        route = "memo_list",
        label = "메모",
        icon = Icons.Default.Create
    )

    object MemoDetail : Screen(
        route = "memo_detail",
        label = "메모 상세",
        icon = Icons.Default.Create
    ) {
        fun createRoute(memoId: Long): String = "$route/$memoId"
    }

    // 예약 구매 관련 화면
    object ProductList : Screen(
        route = "product_list",
        label = "예약 구매",
        icon = Icons.Default.ShoppingCart
    )

    object ProductDetail : Screen(
        route = "product_detail",
        label = "구매 상세",
        icon = Icons.Default.ShoppingCart
    ) {
        fun createRoute(productId: Long): String = "$route/$productId"
    }

    // 계정 관련 화면
    object AccountList : Screen(
        route = "account_list",
        label = "계정",
        icon = Icons.Default.AccountCircle
    )

    object AccountDetail : Screen(
        route = "account_detail",
        label = "계정 상세",
        icon = Icons.Default.AccountCircle
    ) {
        fun createRoute(accountId: Long): String = "$route/$accountId"
    }
}