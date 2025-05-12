package kr.jhp.purchtrac.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("releaseDate")]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String? = null,
    val price: Double,
    val siteName: String,
    val siteUrl: String? = null,
    val imageUrl: String? = null,
    val releaseDate: Long,  // 발매 예정일 (timestamp)
    val purchaseDate: Long? = null,  // 구매일 (timestamp)
    val reminderEnabled: Boolean = true,
    val reminderTime: Long? = null,  // 알림 시간 (timestamp)
    val status: String,  // ProductStatus enum의 이름을 문자열로 저장
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis()
)
