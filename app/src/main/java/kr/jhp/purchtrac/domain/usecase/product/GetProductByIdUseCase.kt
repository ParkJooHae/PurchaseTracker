package kr.jhp.purchtrac.domain.usecase.product

import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Long): Product? {
        return productRepository.getProductById(id)
    }
}