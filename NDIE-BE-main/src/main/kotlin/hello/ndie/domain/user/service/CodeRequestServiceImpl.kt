package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.model.Code
import hello.ndie.domain.user.repository.CodeRepository
import hello.ndie.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.ThreadLocalRandom

@Service
class CodeRequestServiceImpl(
    private val codeRepository: CodeRepository,
    private val userRepository: UserRepository
): CodeRequestService {
    override fun request(userId: Int): String {
        // 유저 존재 여부 체크
        val userExists = userRepository.existsById(userId.toLong())
        if (!userExists) {
            throw IllegalArgumentException("해당 유저 ID가 존재하지 않습니다: $userId")
        }

        val code: String = generateRandomCode(10)

        // 코드 중복 체크 - 만약 중복 방지 필요하면, 여기서 검증
        if (codeRepository.existsById(code)) {
            throw IllegalStateException("생성된 코드가 이미 존재합니다. 다시 시도해주세요.")
        }

        codeRepository.save(
            Code(
                code = code,
                userId = userId,
            )
        )
        return code
    }

    override fun response(code: String): Int {
        val codeEntity = codeRepository.findById(code)
            .orElseThrow { IllegalArgumentException("해당 코드가 존재하지 않습니다: $code") }
        return codeEntity.userId
    }

    @Transactional
    override fun changeGender(gender: String, id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("해당 유저가 존재하지 않습니다: $id") }
        user?.gender = gender
    }

    @Transactional
    override fun changeBirth(birth: LocalDate, id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("해당 유저가 존재하지 않습니다: $id") }
        user?.birthDate = birth
    }

    @Transactional
    override fun changeActivityArea(activityArea: String, id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { IllegalArgumentException("해당 유저가 존재하지 않습니다: $id") }
        user?.activeArea = activityArea
    }


    private fun generateRandomCode(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder()
        val random = ThreadLocalRandom.current()

        repeat(length) {
            val index = random.nextInt(characters.length)
            sb.append(characters[index])
        }

        return sb.toString()
    }
}
