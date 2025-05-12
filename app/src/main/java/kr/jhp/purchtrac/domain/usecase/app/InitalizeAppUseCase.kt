package kr.jhp.purchtrac.domain.usecase.app

import kotlinx.coroutines.flow.first
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.domain.model.UserType
import kr.jhp.purchtrac.domain.repository.UserRepository
import javax.inject.Inject

class InitializeAppUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        // 사용자가 없는 경우에만 기본 사용자 추가
        if (userRepository.getAllUsers().first().isEmpty()) {
            val defaultUsers = listOf(
                User(
                    id = 0, // 자동 생성
                    name = "나",
                    type = UserType.SELF
                ),
                User(
                    id = 0, // 자동 생성
                    name = "어머니",
                    type = UserType.MOTHER
                ),
                User(
                    id = 0, // 자동 생성
                    name = "아버지",
                    type = UserType.FATHER
                )
            )

            defaultUsers.forEach { user ->
                userRepository.insertUser(user)
            }
        }
    }
}