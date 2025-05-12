package kr.jhp.purchtrac.domain.model

data class Product(
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val price: Double,
    val siteName: String,
    val siteUrl: String? = null,
    val imageUrl: String? = null,
    val releaseDate: Long,
    val purchaseDate: Long? = null,
    val reminderEnabled: Boolean = true,
    val reminderTime: Long? = null,
    val status: ProductStatus = ProductStatus.PLANNED,
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis()
)