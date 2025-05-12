package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.jhp.purchtrac.data.local.dao.UserDao
import kr.jhp.purchtrac.data.local.entity.UserEntity
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.domain.model.UserType
import kr.jhp.purchtrac.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getUsersByType(type: UserType): Flow<List<User>> {
        return userDao.getUsersByType(type.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getUserById(id: Long): User? {
        return userDao.getUserById(id)?.toDomainModel()
    }

    override suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun deleteUser(userId: Long) {
        userDao.getUserById(userId)?.let {
            userDao.deleteUser(it)
        }
    }

    // 확장 함수: Entity <-> Domain 모델 변환
    private fun UserEntity.toDomainModel(): User {
        return User(
            id = this.id,
            name = this.name,
            type = UserType.valueOf(this.type)
        )
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            type = this.type.name
        )
    }
}