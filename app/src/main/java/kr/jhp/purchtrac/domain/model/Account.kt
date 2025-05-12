package kr.jhp.purchtrac.domain.model

data class Account(
    val id: Long = 0,
    val userId: Long,
    val siteName: String,
    val siteUrl: String,
    val username: String,
    val password: String,
    val notes: String? = null
)