package kr.jhp.purchtrac.domain.usecase.user

import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.domain.model.UserType
import kr.jhp.purchtrac.domain.repository.UserRepository
import javax.inject.Inject

/**
 * UseCase for creating a new user
 *
 * This UseCase creates a new user with the provided name
 * and adds them to the repository.
 *
 * @param name - The name of the new user
 * @return Long - The ID of the created user
 */
class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String): Long {
        // Validate input
        if (name.isBlank()) {
            throw IllegalArgumentException("User name cannot be blank")
        }

        // Create new user (defaulting to SELF type, can be customized later)
        val newUser = User(
            id = 0,
            name = name,
            type = UserType.SELF
        )

        // Save and return the ID
        return userRepository.createUser(newUser)
    }
}
