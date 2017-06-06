package homepage

import homepage.GameService.GameData
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate
import java.util.stream.Collectors.toList

@Controller
@RequestMapping("/")
class ContentController(
        val gameService: GameService
) {

    private val gameComparator = compareByDescending<GameData> { it.score }.thenByDescending { it.progress }

    @GetMapping
    fun index(model: Model): String {
        addGamesOfCurrentYearData(model)
        addGamesOfLastYearData(model)
        return "index.html"
    }

    private fun addGamesOfCurrentYearData(model: Model) {
        val currentYear = LocalDate.now().year
        val games = getGamesOfYear(currentYear)
        model.addAttribute("gamesOfCurrentYear", games)
        model.addAttribute("currentYear", currentYear)
    }

    private fun addGamesOfLastYearData(model: Model) {
        val lastYear = LocalDate.now().minusYears(1).year
        val games = getGamesOfYear(lastYear)
        model.addAttribute("gamesOfLastYear", games)
        model.addAttribute("lastYear", lastYear)
    }

    private fun getGamesOfYear(year: Int): List<GameModel> {
        return gameService.getAll()
                .sorted(gameComparator)
                .map(this::transform)
                .filter { it.year == year }
                .collect(toList())
    }

    private fun transform(gameData: GameData): GameModel {
        val model = GameModel()
        with(gameData) {
            year?.let { model.year = it }
            title?.let { model.title = it }
            platform?.let { model.platform = it }
            score?.let { model.score = "${it} / 10" }
            progress?.let { model.progress = "${(it * 100).toInt()}%" }
        }
        return model
    }

    class GameModel(
            var year: Int = 0,
            var title: String = "-",
            var platform: String = "-",
            var score: String = "-",
            var progress: String = "-"
    )

}
