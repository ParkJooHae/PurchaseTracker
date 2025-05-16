package kr.jhp.purchtrac.domain.usecase.product

import kr.jhp.purchtrac.domain.repository.ProductRepository
import javax.inject.Inject

class ToggleProductReminderUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val getProductByIdUseCase: GetProductByIdUseCase
) {
    suspend operator fun invoke(productId: Long) {
        val product = getProductByIdUseCase(productId) ?: return
        val updatedProduct = product.copy(reminderEnabled = !product.reminderEnabled)
        productRepository.updateProduct(updatedProduct)
    }
}