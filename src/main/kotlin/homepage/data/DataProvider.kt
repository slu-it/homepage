package homepage.data

import homepage.business.objects.Game
import homepage.business.objects.SocialLinks
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.net.URL


@Service
class DataProvider(
        private val repository: DataRepository
) {

    @Cacheable("socialLinks")
    fun getSocialLinks(): SocialLinks {
        val socialLinksData = findSocialLinksConfigData()
        return convert(socialLinksData)
    }

    private fun findSocialLinksConfigData(): SocialLinksData {
        return (repository.findDatum(fileName = "social-links.json", dataClass = SocialLinksData::class)
                ?: throw SocialLinksConfigurationNotFoundException())
    }

    private fun convert(data: SocialLinksData): SocialLinks {
        return SocialLinks(
                github = URL(data.github ?: throw GitHubNotConfiguredException()),
                twitter = URL(data.twitter ?: throw TwitterNotConfiguredException()),
                xing = URL(data.xing ?: throw XingNotConfiguredException())
        )
    }

    @Cacheable("games")
    fun getGames(): List<Game> {
        val gameData = repository.findData(path = "games", extension = "game", dataClass = GameData::class)
        return gameData.map(this::convert)
    }

    private fun convert(data: GameData): Game {
        return Game(
                year = data.year ?: throw MissingYearException(data),
                title = data.title ?: throw MissingTitleException(data),
                platform = data.platform ?: throw MissingPlatformException(data),
                score = data.score,
                progress = data.progress,
                finished = data.finished ?: false
        )
    }

    abstract class DataProviderException(msg: String) : RuntimeException(msg)

    abstract class SocialLinksException(msg: String) : DataProviderException(msg)

    class SocialLinksConfigurationNotFoundException
        : SocialLinksException("social links configuration data could not be found")

    class GitHubNotConfiguredException
        : SocialLinksException("GitHub profile URL not configured")

    class TwitterNotConfiguredException
        : SocialLinksException("Twitter profile URL not configured")

    class XingNotConfiguredException
        : SocialLinksException("Xing profile URL not configured")

    abstract class GameException(msg: String) : DataProviderException(msg)

    class MissingYearException(game: GameData)
        : GameException("Game is missing year data: $game")

    class MissingTitleException(game: GameData)
        : GameException("Game is missing title data: $game")

    class MissingPlatformException(game: GameData)
        : GameException("Game is missing platform data: $game")

}
