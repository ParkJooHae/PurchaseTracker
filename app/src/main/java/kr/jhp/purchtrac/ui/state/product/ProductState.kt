package kr.jhp.purchtrac.ui.state.product

import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.state.UiState

data class ProductState(
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedStatus: ProductStatus? = null // null은 모든 상태를 표시
) : UiState