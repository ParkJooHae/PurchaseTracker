package kr.jhp.purchtrac.ui.screens.product.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.domain.usecase.product.DeleteProductUseCase
import kr.jhp.purchtrac.domain.usecase.product.GetProductByIdUseCase
import kr.jhp.purchtrac.domain.usecase.product.SaveProductUseCase
import kr.jhp.purchtrac.ui.state.product.detail.ProductDetailEvent
import kr.jhp.purchtrac.ui.state.product.detail.ProductDetailIntent
import kr.jhp.purchtrac.ui.state.product.detail.ProductDetailState
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProductDetailEvent>()
    val event: SharedFlow<ProductDetailEvent> = _event.asSharedFlow()

    init {
        // 상품 ID가 있으면 상품 로드
        savedStateHandle.get<Long>("productId")?.let { productId ->
            if (productId != -1L) {
                processIntent(ProductDetailIntent.LoadProduct(productId))
            } else {
                _state.update { it.copy(isNewProduct = true) }
            }
        }
    }

    fun processIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> loadProduct(intent.productId)
            is ProductDetailIntent.UpdateName -> updateName(intent.name)
            is ProductDetailIntent.UpdateDescription -> updateDescription(intent.description)
            is ProductDetailIntent.UpdatePrice -> updatePrice(intent.price)
            is ProductDetailIntent.UpdateSiteName -> updateSiteName(intent.siteName)
            is ProductDetailIntent.UpdateSiteUrl -> updateSiteUrl(intent.siteUrl)
            is ProductDetailIntent.UpdateImageUrl -> updateImageUrl(intent.imageUrl)
            is ProductDetailIntent.UpdateReleaseDate -> updateReleaseDate(intent.date)
            is ProductDetailIntent.UpdatePurchaseDate -> updatePurchaseDate(intent.date)
            is ProductDetailIntent.UpdateReminderEnabled -> updateReminderEnabled(intent.enabled)
            is ProductDetailIntent.UpdateReminderTime -> updateReminderTime(intent.time)
            is ProductDetailIntent.UpdateStatus -> updateStatus(intent.status)
            is ProductDetailIntent.SaveProduct -> saveProduct()
            is ProductDetailIntent.DeleteProduct -> deleteProduct()
        }
    }

    private fun loadProduct(productId: Long?) {
        if (productId == null || productId <= 0) {
            _state.update { it.copy(isNewProduct = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val product = getProductByIdUseCase(productId)
                if (product != null) {
                    _state.update { state ->
                        state.copy(
                            productId = product.id,
                            userId = product.userId,
                            name = product.name,
                            description = product.description ?: "",
                            price = product.price.toString(),
                            priceValue = product.price,
                            siteName = product.siteName,
                            siteUrl = product.siteUrl ?: "",
                            imageUrl = product.imageUrl ?: "",
                            releaseDate = product.releaseDate,
                            purchaseDate = product.purchaseDate,
                            reminderEnabled = product.reminderEnabled,
                            reminderTime = product.reminderTime,
                            status = product.status,
                            isLoading = false,
                            isNewProduct = false,
                            error = null
                        )
                    }
                } else {
                    // 해당 ID의 상품을 찾지 못함 (새 상품으로 처리)
                    _state.update { it.copy(
                        isLoading = false,
                        isNewProduct = true,
                        error = "상품을 찾을 수 없습니다"
                    ) }
                    _event.emit(ProductDetailEvent.ShowToast("상품을 찾을 수 없습니다"))
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(ProductDetailEvent.ShowToast("오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updateDescription(description: String) {
        _state.update { it.copy(description = description) }
    }

    private fun updatePrice(price: String) {
        try {
            val priceValue = if (price.isBlank()) 0.0 else price.toDouble()
            _state.update { it.copy(price = price, priceValue = priceValue) }
        } catch (e: NumberFormatException) {
            _state.update { it.copy(price = price) }
        }
    }

    private fun updateSiteName(siteName: String) {
        _state.update { it.copy(siteName = siteName) }
    }

    private fun updateSiteUrl(siteUrl: String) {
        _state.update { it.copy(siteUrl = siteUrl) }
    }

    private fun updateImageUrl(imageUrl: String) {
        _state.update { it.copy(imageUrl = imageUrl) }
    }

    private fun updateReleaseDate(date: Long) {
        _state.update { it.copy(releaseDate = date) }
    }

    private fun updatePurchaseDate(date: Long?) {
        _state.update { it.copy(purchaseDate = date) }
    }

    private fun updateReminderEnabled(enabled: Boolean) {
        _state.update { it.copy(reminderEnabled = enabled) }
    }

    private fun updateReminderTime(time: Long?) {
        _state.update { it.copy(reminderTime = time) }
    }

    private fun updateStatus(status: ProductStatus) {
        _state.update { it.copy(status = status) }
    }

    private fun saveProduct() {
        val currentState = _state.value

        // 필수 입력값 확인
        if (currentState.name.isBlank() || currentState.siteName.isBlank()) {
            viewModelScope.launch {
                _event.emit(ProductDetailEvent.ShowToast("상품명과 사이트 이름은 필수 입력 항목입니다"))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            try {
                val product = Product(
                    id = currentState.productId ?: 0,
                    userId = currentState.userId,
                    name = currentState.name,
                    description = currentState.description.ifBlank { null },
                    price = currentState.priceValue,
                    siteName = currentState.siteName,
                    siteUrl = currentState.siteUrl.ifBlank { null },
                    imageUrl = currentState.imageUrl.ifBlank { null },
                    releaseDate = currentState.releaseDate,
                    purchaseDate = currentState.purchaseDate,
                    reminderEnabled = currentState.reminderEnabled,
                    reminderTime = currentState.reminderTime,
                    status = currentState.status,
                    created = System.currentTimeMillis(),
                    updated = System.currentTimeMillis()
                )

                val savedId = saveProductUseCase(product)
                _state.update { it.copy(
                    productId = savedId,
                    isSaving = false,
                    isNewProduct = false
                ) }

                _event.emit(ProductDetailEvent.ProductSaved)
                _event.emit(ProductDetailEvent.ShowToast("상품이 저장되었습니다"))
                _event.emit(ProductDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isSaving = false,
                    error = e.message
                ) }
                _event.emit(ProductDetailEvent.ShowToast("저장 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun deleteProduct() {
        val productId = _state.value.productId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                deleteProductUseCase(productId)
                _state.update { it.copy(isLoading = false) }
                _event.emit(ProductDetailEvent.ProductDeleted)
                _event.emit(ProductDetailEvent.ShowToast("상품이 삭제되었습니다"))
                _event.emit(ProductDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(ProductDetailEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }
}