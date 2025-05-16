package kr.jhp.purchtrac.ui.screens.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.domain.usecase.product.DeleteProductUseCase
import kr.jhp.purchtrac.domain.usecase.product.GetProductsUseCase
import kr.jhp.purchtrac.domain.usecase.product.ToggleProductReminderUseCase
import kr.jhp.purchtrac.ui.state.product.ProductEvent
import kr.jhp.purchtrac.ui.state.product.ProductIntent
import kr.jhp.purchtrac.ui.state.product.ProductState
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val toggleProductReminderUseCase: ToggleProductReminderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProductEvent>()
    val event: SharedFlow<ProductEvent> = _event.asSharedFlow()

    init {
        processIntent(ProductIntent.LoadProducts)
    }

    fun processIntent(intent: ProductIntent) {
        when (intent) {
            is ProductIntent.LoadProducts -> loadProducts()
            is ProductIntent.SearchProducts -> searchProducts(intent.query)
            is ProductIntent.FilterProductsByStatus -> filterByStatus(intent.status)
            is ProductIntent.DeleteProduct -> deleteProduct(intent.productId)
            is ProductIntent.ToggleReminderEnabled -> toggleReminder(intent.productId)
            is ProductIntent.ClearSearchQuery -> clearSearchQuery()
            is ProductIntent.UpdateProduct -> updateProductList(intent.product)
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getProductsUseCase().catch { e ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }.collect { products ->
                updateProductList(products)
                _state.update { state ->
                    state.copy(isLoading = false, error = null)
                }
            }
        }
    }

    private fun searchProducts(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun clearSearchQuery() {
        _state.update { it.copy(searchQuery = "") }
        applyFilters()
    }

    private fun filterByStatus(status: ProductStatus?) {
        _state.update { it.copy(selectedStatus = status) }
        applyFilters()
    }

    private fun deleteProduct(productId: Long) {
        viewModelScope.launch {
            try {
                deleteProductUseCase(productId)
                _event.emit(ProductEvent.ShowToast("상품이 삭제되었습니다"))
            } catch (e: Exception) {
                _event.emit(ProductEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun toggleReminder(productId: Long) {
        viewModelScope.launch {
            try {
                toggleProductReminderUseCase(productId)
                _event.emit(ProductEvent.ShowToast("알림 설정이 변경되었습니다"))
            } catch (e: Exception) {
                _event.emit(ProductEvent.ShowToast("알림 설정 변경 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateProductList(products: List<Product>) {
        _state.update { it.copy(products = products) }
        applyFilters()
    }

    private fun updateProductList(product: Product) {
        val currentProducts = _state.value.products.toMutableList()
        val index = currentProducts.indexOfFirst { it.id == product.id }

        if (index != -1) {
            currentProducts[index] = product
        } else {
            currentProducts.add(product)
        }

        _state.update { it.copy(products = currentProducts) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _state.value
        val filteredList = state.products.filter { product ->
            // 검색어 필터
            val matchesSearch = state.searchQuery.isEmpty() ||
                    product.name.contains(state.searchQuery, ignoreCase = true) ||
                    product.siteName.contains(state.searchQuery, ignoreCase = true)

            // 상태 필터
            val matchesStatus = state.selectedStatus == null || product.status == state.selectedStatus

            matchesSearch && matchesStatus
        }

        _state.update { it.copy(filteredProducts = filteredList) }
    }

    fun navigateToDetail(productId: Long?) {
        viewModelScope.launch {
            _event.emit(ProductEvent.NavigateToProductDetail(productId))
        }
    }
}