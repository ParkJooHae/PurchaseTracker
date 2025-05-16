package kr.jhp.purchtrac.domain.usecase.product

import kr.jhp.purchtrac.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(productId: Long) {
        productRepository.deleteProduct(productId)
    }
}