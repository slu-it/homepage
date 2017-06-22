package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.URIish
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.Collectors.*
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@Component
class DataRepository(
        private val mapper: ObjectMapper,
        private val settings: Settings,
        private val git: Git
) {

    @Component
    @ConfigurationProperties("git.repository")
    data class Settings(
            var originUri: String? = null
    )

    private val log: Logger = getLogger(javaClass)

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

    fun <T : Any> findDatum(path: String = "", name: String, extension: String = "", dataClass: KClass<T>): T? {
        val workingDirectory = getWorkingDirectory()
        val searchPath = if (path.isEmpty()) workingDirectory else workingDirectory.resolve(path)
        val fileName = if (extension.isEmpty()) name else "$name.$extension"
        return Files.list(searchPath)
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.name == fileName }
                .findFirst()
                .map { mapper.readValue(it, dataClass.java) }
                .orElse(null)
    }

    fun <T : Any> findData(path: String = "", extension: String, dataClass: KClass<T>): List<T> {
        val workingDirectory = getWorkingDirectory()
        val searchPath = if (path.isEmpty()) workingDirectory else workingDirectory.resolve(path)
        return Files.list(searchPath)
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.extension == extension }
                .map { mapper.readValue(it, dataClass.java) }
                .collect(toList())
    }

}
