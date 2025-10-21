package kr.jhp.purchtrac.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.domain.repository.UserRepository
import javax.inject.Inject

/**
 * UseCase for retrieving all users
 *
 * This UseCase fetches all available users from the repository
 * and returns them as a Flow for reactive updates.
 *
 * @return Flow<List<User>> - Stream of user lists
 */
class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
}
