package kr.jhp.purchtrac.ui.state.product.detail

import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.state.UiIntent

sealed class ProductDetailIntent : UiIntent {
    data class LoadProduct(val productId: Long?) : ProductDetailIntent()
    data class UpdateName(val name: String) : ProductDetailIntent()
    data class UpdateDescription(val description: String) : ProductDetailIntent()
    data class UpdatePrice(val price: String) : ProductDetailIntent()
    data class UpdateSiteName(val siteName: String) : ProductDetailIntent()
    data class UpdateSiteUrl(val siteUrl: String) : ProductDetailIntent()
    data class UpdateImageUrl(val imageUrl: String) : ProductDetailIntent()
    data class UpdateReleaseDate(val date: Long) : ProductDetailIntent()
    data class UpdatePurchaseDate(val date: Long?) : ProductDetailIntent()
    data class UpdateReminderEnabled(val enabled: Boolean) : ProductDetailIntent()
    data class UpdateReminderTime(val time: Long?) : ProductDetailIntent()
    data class UpdateStatus(val status: ProductStatus) : ProductDetailIntent()
    object SaveProduct : ProductDetailIntent()
    object DeleteProduct : ProductDetailIntent()
}