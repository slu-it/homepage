package homepage.business

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import homepage.business.objects.Game
import homepage.data.DataProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.OffsetDateTime
import java.time.ZoneId

internal class GamesServiceTest {

    val fixedTimestamp = OffsetDateTime.parse("2017-06-22T12:34:56Z")!!

    val dataProvider: DataProvider = mock()
    val clock = Clock.fixed(fixedTimestamp.toInstant(), ZoneId.from(fixedTimestamp))!!
    val cut = GamesService(dataProvider, clock)

    val gameOf217 = gameOf(2017)
    val gameOf216 = gameOf(2016)
    val gameOf215 = gameOf(2015)
    val gameOf214 = gameOf(2014)
    val allGames = listOf(gameOf217, gameOf216, gameOf215, gameOf214)

    @Test
    fun `games can gotten for current year`() {
        given(dataProvider.getGames()).willReturn(allGames)
        val games = cut.getGamesOfCurrentYear()
        assertThat(games).containsOnly(gameOf217)
    }

    @Test
    fun `if there are no games for current year an empty list is returned`() {
        given(dataProvider.getGames()).willReturn(emptyList())
        val games = cut.getGamesOfCurrentYear()
        assertThat(games).isEmpty()
    }

    @Test
    fun `games can gotten for last year`() {
        given(dataProvider.getGames()).willReturn(allGames)
        val games = cut.getGamesOfLastYear()
        assertThat(games).containsOnly(gameOf216)
    }

    @Test
    fun `if there are no games for last year an empty list is returned`() {
        given(dataProvider.getGames()).willReturn(emptyList())
        val games = cut.getGamesOfLastYear()
        assertThat(games).isEmpty()
    }

    @Test
    fun `games can gotten for other years`() {
        given(dataProvider.getGames()).willReturn(allGames)
        val games = cut.getGamesOfOtherYears()
        assertThat(games).containsOnly(gameOf215, gameOf214)
    }

    @Test
    fun `if there are no games for other years an empty list is returned`() {
        given(dataProvider.getGames()).willReturn(emptyList())
        val games = cut.getGamesOfOtherYears()
        assertThat(games).isEmpty()
    }

    fun gameOf(year: Int): Game {
        return Game(year, "Game of $year", "PS4")
    }

}