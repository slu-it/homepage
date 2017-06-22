package homepage.business

import homepage.business.objects.Game
import homepage.data.DataProvider
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate

@Service
class GamesService(
        private val dataProvider: DataProvider,
        private val clock: Clock
) {

    fun getGamesOfCurrentYear(): List<Game> {
        val currentYear = LocalDate.now(clock).year
        return dataProvider.getGames()
                .filter { it.year == currentYear }
    }

    fun getGamesOfLastYear(): List<Game> {
        val lastYear = LocalDate.now(clock).minusYears(1).year
        return dataProvider.getGames()
                .filter { it.year == lastYear }
    }

    fun getGamesOfOtherYears(): List<Game> {
        val lastYear = LocalDate.now(clock).minusYears(1).year
        return dataProvider.getGames()
                .filter { it.year!! < lastYear }
    }

}