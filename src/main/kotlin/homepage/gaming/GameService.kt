package homepage.gaming

import com.fasterxml.jackson.databind.ObjectMapper
import homepage.persistence.LocalGitRepository
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.util.stream.Stream

@Service
class GameService(
        val repository: LocalGitRepository,
        val mapper: ObjectMapper
) {

    fun getAll(): Stream<GameData> {
        val gamesFolder = repository.getWorkingDirectory().resolve("games")
        return Files.list(gamesFolder)
                .map { it.toFile() }
                .filter { it.isFile }
                .filter { it.extension == "game" }
                .map { mapper.readValue(it, GameData::class.java) }
    }

}
