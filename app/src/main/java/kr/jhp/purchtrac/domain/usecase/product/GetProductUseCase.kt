package kr.jhp.purchtrac.domain.usecase.product

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}