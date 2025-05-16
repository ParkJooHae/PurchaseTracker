package kr.jhp.purchtrac.ui.state.product.detail

import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.state.UiState

data class ProductDetailState(
    val productId: Long? = null,
    val userId: Long = 1L, // 기본값 설정 (실제로는 사용자 선택 또는 현재 사용자에서 가져와야 함)
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val priceValue: Double = 0.0,
    val siteName: String = "",
    val siteUrl: String = "",
    val imageUrl: String = "",
    val releaseDate: Long = System.currentTimeMillis(), // 기본값은 현재 시간
    val purchaseDate: Long? = null,
    val reminderEnabled: Boolean = true,
    val reminderTime: Long? = null,
    val status: ProductStatus = ProductStatus.PLANNED,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isNewProduct: Boolean = true,
    val error: String? = null,
    val showDatePicker: Boolean = false,
    val datePickerFor: String = "" // "release" 또는 "purchase"
) : UiState