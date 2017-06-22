package homepage.business.games

import homepage.data.DataProvider
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GamesService(
        private val dataProvider: DataProvider
) {

    fun getGamesOfCurrentYear(): List<GameData> {
        val currentYear = LocalDate.now().year
        return dataProvider.getGames()
                .filter { it.year == currentYear }
    }

    fun getGamesOfLastYear(): List<GameData> {
        val lastYear = LocalDate.now().minusYears(1).year
        return dataProvider.getGames()
                .filter { it.year == lastYear }
    }

    fun getGamesOfOtherYears(): List<GameData> {
        val lastYear = LocalDate.now().minusYears(1).year
        return dataProvider.getGames()
                .filter { it.year == null || it.year!! < lastYear }
    }

}