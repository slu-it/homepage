package homepage.persistence

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.URIish
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.PostConstruct

@Component
class LocalGitRepository(
        private val settings: Settings
) {

    @Component
    @ConfigurationProperties("git.repository")
    data class Settings(
            var originUri: String? = null
    )

    private val log: Logger = getLogger(javaClass)
    private val git: Git

    init {
        val localGitRepository = Files.createTempDirectory("git-repo").toFile().apply { deleteOnExit() }
        log.info("temporary folder for local GIT repository: {}", localGitRepository)
        git = Git.init().setDirectory(localGitRepository).call()
    }

    @PostConstruct
    fun init() {
        val originUri = settings.originUri!!
        with(git.remoteAdd()) {
            setName("origin")
            setUri(URIish(originUri))
            call()
        }
        log.info("GIT: 'remote add origin $originUri'")
    }

    fun update() {
        with(git.fetch()) {
            setRemote("origin")
            call()
            log.info("GIT: 'fetch origin'")
        }
        with(git.reset()) {
            setMode(ResetCommand.ResetType.HARD)
            setRef("origin/master")
            call()
            log.info("GIT: 'reset origin/master --hard'")
        }
    }

    fun getWorkingDirectory(): Path {
        return git.repository.workTree.toPath()
    }

}
