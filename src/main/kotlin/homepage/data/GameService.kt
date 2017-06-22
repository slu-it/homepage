package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import homepage.business.gaming.GameData
import org.slf4j.Logger
import org.slf4j.LoggerFactory.*
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.nio.file.Files.*
import java.util.stream.Collectors.*

@Service
class GameService(
        private val repository: DataRepository,
        private val mapper: ObjectMapper
) {

    private val log: Logger = getLogger(javaClass)

    @Cacheable("allGames")
    fun getAll(): List<GameData> {
        val gamesFolder = repository.getWorkingDirectory().resolve("games")
        log.debug("loading games from {}", gamesFolder)
        return list(gamesFolder)
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.extension == "game" }
                .map { mapper.readValue(it, GameData::class.java) }
                .peek { log.debug("found game: {}", it) }
                .collect(toList())
    }

}
