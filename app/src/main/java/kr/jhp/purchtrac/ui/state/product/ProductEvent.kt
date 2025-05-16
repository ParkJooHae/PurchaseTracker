package kr.jhp.purchtrac.ui.state.product

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class ProductEvent : UiEvent {
    data class ShowToast(val message: String) : ProductEvent()
    data class NavigateToProductDetail(val productId: Long?) : ProductEvent() // null은 새 상품
    object NavigateBack : ProductEvent()
}