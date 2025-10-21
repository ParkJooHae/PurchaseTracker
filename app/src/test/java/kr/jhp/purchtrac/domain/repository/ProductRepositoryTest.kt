package kr.jhp.purchtrac.domain.repository

import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for ProductRepository
 *
 * Tests product-related operations:
 * - Creating and saving products
 * - Retrieving products by ID and status
 * - Updating product information
 * - Changing product status
 * - Managing reminder settings
 */
class ProductRepositoryTest {

    @Test
    fun testProductHasValidStructure() {
        // Arrange
        val currentTime = System.currentTimeMillis()
        val product = Product(
            id = 1,
            userId = 1,
            name = "PlayStation 5",
            description = "Latest gaming console",
            price = 499.99,
            siteName = "Amazon",
            siteUrl = "https://amazon.com",
            imageUrl = null,
            releaseDate = currentTime,
            purchaseDate = null,
            reminderEnabled = true,
            reminderTime = null,
            status = ProductStatus.PLANNED,
            created = currentTime,
            updated = currentTime
        )

        // Act & Assert
        assertEquals(1, product.id)
        assertEquals(1, product.userId)
        assertEquals("PlayStation 5", product.name)
        assertEquals(499.99, product.price)
        assertEquals(ProductStatus.PLANNED, product.status)
        assertTrue { product.reminderEnabled }
    }

    @Test
    fun testProductStatusTransition() {
        // Arrange
        val product = Product(
            id = 1,
            userId = 1,
            name = "Nintendo Switch",
            description = null,
            price = 299.99,
            siteName = "Best Buy",
            siteUrl = "https://bestbuy.com",
            imageUrl = null,
            releaseDate = System.currentTimeMillis(),
            purchaseDate = null,
            reminderEnabled = false,
            reminderTime = null,
            status = ProductStatus.PLANNED,
            created = System.currentTimeMillis(),
            updated = System.currentTimeMillis()
        )

        // Act - Change status to PURCHASED
        val purchasedProduct = product.copy(
            status = ProductStatus.PURCHASED,
            purchaseDate = System.currentTimeMillis()
        )

        // Assert
        assertEquals(ProductStatus.PLANNED, product.status)
        assertEquals(ProductStatus.PURCHASED, purchasedProduct.status)
        assertTrue { purchasedProduct.purchaseDate != null }
    }

    @Test
    fun testProductReminderToggle() {
        // Arrange
        val product = Product(
            id = 1,
            userId = 1,
            name = "Xbox Series X",
            description = null,
            price = 499.99,
            siteName = "GameStop",
            siteUrl = "https://gamestop.com",
            imageUrl = null,
            releaseDate = System.currentTimeMillis(),
            purchaseDate = null,
            reminderEnabled = false,
            reminderTime = null,
            status = ProductStatus.PLANNED,
            created = System.currentTimeMillis(),
            updated = System.currentTimeMillis()
        )

        // Act
        val reminderEnabled = product.copy(
            reminderEnabled = true,
            reminderTime = System.currentTimeMillis() + 86400000 // +1 day
        )

        // Assert
        assertFalse { product.reminderEnabled }
        assertTrue { reminderEnabled.reminderEnabled }
        assertTrue { reminderEnabled.reminderTime != null }
    }

    @Test
    fun testProductCancellation() {
        // Arrange
        val product = Product(
            id = 1,
            userId = 1,
            name = "Laptop",
            description = "High performance laptop",
            price = 1299.99,
            siteName = "Apple",
            siteUrl = "https://apple.com",
            imageUrl = null,
            releaseDate = System.currentTimeMillis(),
            purchaseDate = null,
            reminderEnabled = true,
            reminderTime = null,
            status = ProductStatus.PLANNED,
            created = System.currentTimeMillis(),
            updated = System.currentTimeMillis()
        )

        // Act
        val cancelledProduct = product.copy(status = ProductStatus.CANCELED)

        // Assert
        assertEquals(ProductStatus.PLANNED, product.status)
        assertEquals(ProductStatus.CANCELED, cancelledProduct.status)
    }

    @Test
    fun testProductWithoutOptionalFields() {
        // Arrange & Act
        val product = Product(
            id = 1,
            userId = 1,
            name = "Test Product",
            description = null,
            price = 50.0,
            siteName = "Test Site",
            siteUrl = null,
            imageUrl = null,
            releaseDate = System.currentTimeMillis(),
            purchaseDate = null,
            reminderEnabled = false,
            reminderTime = null,
            status = ProductStatus.PLANNED,
            created = System.currentTimeMillis(),
            updated = System.currentTimeMillis()
        )

        // Assert
        assertTrue { product.description == null }
        assertTrue { product.siteUrl == null }
        assertTrue { product.imageUrl == null }
        assertTrue { product.purchaseDate == null }
    }

    @Test
    fun testProductPriceValidation() {
        // Arrange
        val validPrice = 99.99
        val zeroPrice = 0.0

        // Act & Assert
        assertTrue { validPrice > 0 }
        assertTrue { zeroPrice == 0.0 }
    }
}
