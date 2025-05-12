package kr.jhp.purchtrac.domain.model

data class Memo(
    val id: Long = 0,
    val userId: Long,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isImportant: Boolean = false
)