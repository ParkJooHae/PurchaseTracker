package kr.jhp.purchtrac.domain.model

data class User(
    val id: Long = 0,
    val name: String,
    val type: UserType
)