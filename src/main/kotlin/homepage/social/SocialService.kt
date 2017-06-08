package homepage.social

import com.fasterxml.jackson.databind.ObjectMapper
import homepage.persistence.LocalGitRepository
import org.springframework.stereotype.Service
import java.nio.file.Files


@Service
class SocialService(
        val repository: LocalGitRepository,
        val mapper: ObjectMapper
) {

    fun getSocialLinks(): SocialLinks {
        return Files.list(repository.getWorkingDirectory())
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.nameWithoutExtension == "social-links" }
                .findFirst()
                .map { mapper.readValue(it, SocialLinks::class.java) }
                .orElseGet { SocialLinks() }
    }

}
