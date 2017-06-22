package homepage.data

import org.eclipse.jgit.api.Git
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files

@Configuration
class GitConfiguration {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun git(): Git {
        val localGitRepository = Files.createTempDirectory("git-repo").toFile().apply { deleteOnExit() }
        log.info("temporary folder for local GIT repository: {}", localGitRepository)
        return Git.init().setDirectory(localGitRepository).call()
    }

}