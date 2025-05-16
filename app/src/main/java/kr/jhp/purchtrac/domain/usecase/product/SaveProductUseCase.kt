package kr.jhp.purchtrac.domain.usecase.product

import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.repository.ProductRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Long {
        return if (product.id == 0L) {
            productRepository.insertProduct(product)
        } else {
            productRepository.updateProduct(product)
            product.id
        }
    }
}