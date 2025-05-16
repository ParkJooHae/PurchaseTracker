package kr.jhp.purchtrac.ui.state.product.detail

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class ProductDetailEvent : UiEvent {
    data class ShowToast(val message: String) : ProductDetailEvent()
    object ProductSaved : ProductDetailEvent()
    object ProductDeleted : ProductDetailEvent()
    object NavigateBack : ProductDetailEvent()
}