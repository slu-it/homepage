package homepage.data

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import homepage.business.objects.Game
import homepage.business.objects.SocialLinks
import homepage.data.DataProvider.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.URL

internal class DataProviderTest {

    val repository: DataRepository = mock()
    val cut = DataProvider(repository)

    @Nested
    inner class `social media links` {

        @Test
        fun `if social media link data is found, it is returned`() {
            setupRepositoryToFind(socialLinksData())
            val found = cut.getSocialLinks()
            assertThat(found).isEqualTo(socialLinks())
        }

        @Test
        fun `exception is thrown if social media links are not configured at all`() {
            setupRepositoryToFind(null)
            assertThrows(SocialLinksConfigurationNotFoundException::class.java, {
                cut.getSocialLinks()
            })
        }

        @Test
        fun `exception is thrown if GitHub is not configured`() {
            setupRepositoryToFind(socialLinksData().apply { github = null })
            assertThrows(GitHubNotConfiguredException::class.java, {
                cut.getSocialLinks()
            })
        }

        @Test
        fun `exception is thrown if Twitter is not configured`() {
            setupRepositoryToFind(socialLinksData().apply { twitter = null })
            assertThrows(TwitterNotConfiguredException::class.java, {
                cut.getSocialLinks()
            })
        }

        @Test
        fun `exception is thrown if Xing is not configured`() {
            setupRepositoryToFind(socialLinksData().apply { xing = null })
            assertThrows(XingNotConfiguredException::class.java, {
                cut.getSocialLinks()
            })
        }

        fun setupRepositoryToFind(data: SocialLinksData?) {
            doReturn(data).whenever(repository)
                    .findDatum(fileName = "social-links.json", dataClass = SocialLinksData::class)
        }

        fun socialLinksData(): SocialLinksData {
            return SocialLinksData(
                    github = "https://github.com",
                    twitter = "https://twitter.com",
                    xing = "https://xing.de"
            )
        }

        fun socialLinks(): SocialLinks {
            return SocialLinks(
                    github = URL("https://github.com"),
                    twitter = URL("https://twitter.com"),
                    xing = URL("https://xing.de")
            )
        }

    }

    @Nested
    inner class `games` {

        @Test
        fun `if game data is found, it is returned`() {
            setupRepositoryToFind(gameData())
            val found = cut.getGames()
            assertThat(found).isEqualTo(listOf(game()))
        }

        @Test
        fun `exception is thrown if game is missing its year`() {
            setupRepositoryToFind(gameData().apply { year = null })
            assertThrows(MissingYearException::class.java, {
                cut.getGames()
            })
        }

        @Test
        fun `exception is thrown if game is missing its title`() {
            setupRepositoryToFind(gameData().apply { title = null })
            assertThrows(MissingTitleException::class.java, {
                cut.getGames()
            })
        }

        @Test
        fun `exception is thrown if game is missing its platform`() {
            setupRepositoryToFind(gameData().apply { platform = null })
            assertThrows(MissingPlatformException::class.java, {
                cut.getGames()
            })
        }

        fun setupRepositoryToFind(vararg data: GameData) {
            doReturn(data.toList()).whenever(repository)
                    .findData(path = "games", extension = "game", dataClass = GameData::class)
        }

        fun gameData(): GameData {
            return GameData(year = 2017, title = "A Game", platform = "PS4", score = 10, progress = 1.0f)
        }

        fun game(): Game {
            return Game(year = 2017, title = "A Game", platform = "PS4", score = 10, progress = 1.0f)
        }

    }

}