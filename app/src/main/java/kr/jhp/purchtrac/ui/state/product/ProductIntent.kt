package kr.jhp.purchtrac.ui.state.product

import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.state.UiIntent

sealed class ProductIntent : UiIntent {
    object LoadProducts : ProductIntent()
    data class SearchProducts(val query: String) : ProductIntent()
    data class FilterProductsByStatus(val status: ProductStatus?) : ProductIntent()
    data class DeleteProduct(val productId: Long) : ProductIntent()
    data class UpdateProduct(val product: Product) : ProductIntent()
    object ClearSearchQuery : ProductIntent()
    data class ToggleReminderEnabled(val productId: Long) : ProductIntent()
}