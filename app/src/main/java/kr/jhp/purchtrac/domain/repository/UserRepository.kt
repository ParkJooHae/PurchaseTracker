package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.domain.model.UserType

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    fun getUsersByType(type: UserType): Flow<List<User>>
    suspend fun getUserById(id: Long): User?
    suspend fun insertUser(user: User): Long
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: Long)
}