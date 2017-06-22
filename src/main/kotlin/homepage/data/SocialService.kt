package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import homepage.business.social.SocialLinks
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.nio.file.Files


@Service
class SocialService(
        private val mapper: ObjectMapper,
        private val repository: DataRepository
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Cacheable("socialLinks")
    fun getSocialLinks(): SocialLinks {
        val socialLinks = Files.list(repository.getWorkingDirectory())
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.nameWithoutExtension == "social-links" }
                .findFirst()
                .map { mapper.readValue(it, SocialLinks::class.java) }
                .orElseGet {
                    log.debug("fallback to default social links")
                    SocialLinks()
                }
        log.debug("loaded social links: {}", socialLinks)
        return socialLinks
    }

}
