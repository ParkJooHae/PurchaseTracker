package kr.jhp.purchtrac.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithAccounts(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val accounts: List<AccountEntity>
)

data class UserWithProducts(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val products: List<ProductEntity>
)