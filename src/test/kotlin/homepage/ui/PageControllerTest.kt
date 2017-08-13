package homepage.ui

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.willReturn
import homepage.business.GamesService
import homepage.business.objects.Game
import homepage.business.objects.SocialLinks
import homepage.data.DataProvider
import homepage.ui.PageController.GameModel
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.ui.Model
import java.net.URL
import java.time.Clock
import java.time.OffsetDateTime
import java.time.ZoneId


internal class PageControllerTest {

    val fixedTimestamp = OffsetDateTime.parse("2017-06-22T12:34:56Z")!!

    val gameService: GamesService = mock()
    val dataProvider: DataProvider = mock()
    val clock = Clock.fixed(fixedTimestamp.toInstant(), ZoneId.from(fixedTimestamp))!!
    val cut = PageController(gameService, dataProvider, clock)

    val model: Model = mock()

    val socialLinks = socialLinks()

    @Nested
    inner class `index page` : TemplateDataContract() {

        @Test
        fun `games of this year are set`() {
            val (game1, gameModel1) = game("Horizon: Zero Dawn")
            val (game2, gameModel2) = game("Zelda: Breath of the Wild")
            given(gameService.getGamesOfCurrentYear()).willReturn(listOf(game1, game2))

            cut.games(model)

            verify(model).addAttribute("gamesOfCurrentYear", listOf(gameModel1, gameModel2))
        }

    }

    @Nested
    inner class `games page` : TemplateDataContract() {

        @Test
        fun `games of this year are set`() {
            val (game1, gameModel1) = game("Game #1")
            val (game2, gameModel2) = game("Game #2")
            given(gameService.getGamesOfCurrentYear()).willReturn(listOf(game1, game2))

            cut.games(model)

            verify(model).addAttribute("gamesOfCurrentYear", listOf(gameModel1, gameModel2))
        }

        @Test
        fun `games of last year are set`() {
            val (game1, gameModel1) = game("Game #1")
            val (game2, gameModel2) = game("Game #2")
            given(gameService.getGamesOfLastYear()).willReturn(listOf(game1, game2))

            cut.games(model)

            verify(model).addAttribute("gamesOfLastYear", listOf(gameModel1, gameModel2))
        }

        @Test
        fun `games of other years are set`() {
            val (game1, gameModel1) = game("Game #1")
            val (game2, gameModel2) = game("Game #2")
            given(gameService.getGamesOfOtherYears()).willReturn(listOf(game1, game2))

            cut.games(model)

            verify(model).addAttribute("gamesOfOtherYears", listOf(gameModel1, gameModel2))
        }

    }

    open inner class TemplateDataContract {

        @Test
        fun `social media links are set`() {
            given(dataProvider.getSocialLinks()).willReturn(socialLinks)
            cut.games(model)
            verify(model).addAttribute("socialLinks", socialLinks)
        }

        @Test
        fun `temporal data is set`() {
            cut.index(model)
            verify(model).addAttribute("currentYear", 2017)
            verify(model).addAttribute("lastYear", 2016)
        }

    }

    fun socialLinks(): SocialLinks {
        val gitHub = URL("https://github.com")
        val twitter = URL("https://twitter.com")
        val xing = URL("https://xing.de")
        return SocialLinks(github = gitHub, twitter = twitter, xing = xing)
    }

    fun game(title: String): Pair<Game, GameModel> {
        val game = Game(
                year = 2017,
                title = title,
                platform = "PS4",
                progress = 0.5f,
                score = 5,
                finished = true,
                dlc = listOf(
                        Game.Dlc(title = "Some DLC", finished = true)
                )
        )
        val gameModel = GameModel(
                year = 2017,
                title = title,
                platform = "PS4",
                progress = "50%",
                score = "5 / 10",
                finished = true,
                dlc = mutableListOf(
                        GameModel.DlcModel(title = "Some DLC", finished = true)
                )
        )
        return game to gameModel
    }

}