package homepage.data

import homepage.business.objects.Game
import homepage.business.objects.SocialLinks
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.net.URL
import java.util.stream.Collectors.toList


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
        return (repository.findDatum(name = "social-links", extension = "json", dataClass = SocialLinksData::class)
                ?: throw IllegalStateException("social links configuration data could not be found"))
    }

    private fun convert(socialLinksData: SocialLinksData): SocialLinks {
        val github = socialLinksData.github
                ?: throw IllegalStateException("GitHub profile URL not configured")
        val twitter = socialLinksData.twitter
                ?: throw IllegalStateException("Twitter profile URL not configured")
        val xing = socialLinksData.xing
                ?: throw IllegalStateException("Xing profile URL not configured")

        return SocialLinks(
                github = URL(github),
                twitter = URL(twitter),
                xing = URL(xing)
        )
    }

    @Cacheable("games")
    fun getGames(): List<Game> {
        val gameData = repository.findData(path = "games", extension = "game", dataClass = GameData::class)
        return gameData.map(this::convert)
    }

    private fun convert(gameData: GameData): Game {
        val year = gameData.year
                ?: throw IllegalStateException("$gameData does not declare a year")
        val title = gameData.title
                ?: throw IllegalStateException("$gameData does not declare a title")
        val platform = gameData.platform
                ?: throw IllegalStateException("$gameData does not declare a platform")
        val score = gameData.score
        val progress = gameData.progress

        return Game(
                year = year,
                title = title,
                platform = platform,
                score = score,
                progress = progress
        )
    }

}
