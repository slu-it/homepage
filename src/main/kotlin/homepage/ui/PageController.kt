package homepage.ui

import homepage.business.GamesService
import homepage.business.objects.Game
import homepage.data.DataProvider
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.Clock
import java.time.LocalDate
import java.util.stream.Collectors.toList

@Controller
class PageController(
        private val gameService: GamesService,
        private val dataProvider: DataProvider,
        private val clock: Clock
) {

    private val gameComparator = compareByDescending<Game> { it.score }.thenBy { it.title }

    @GetMapping("/")
    fun index(model: Model): String {
        addTemplateData(model)
        addGamesOfCurrentYearData(model)
        return "index.html"
    }

    @GetMapping("/games")
    fun games(model: Model): String {
        addTemplateData(model)
        addGamesOfCurrentYearData(model)
        addGamesOfLastYearData(model)
        addGamesOfOtherYears(model)
        return "games.html"
    }

    private fun addTemplateData(model: Model) {
        addSocialLinks(model)
        addTemporalInformation(model)
    }

    private fun addSocialLinks(model: Model) {
        model.addAttribute("socialLinks", dataProvider.getSocialLinks())
    }

    private fun addTemporalInformation(model: Model) {
        val currentYear = LocalDate.now(clock).year
        val lastYear = LocalDate.now(clock).minusYears(1).year
        model.apply {
            addAttribute("currentYear", currentYear)
            addAttribute("lastYear", lastYear)
        }
    }

    private fun addGamesOfCurrentYearData(model: Model) {
        val games = gameService.getGamesOfCurrentYear()
        val gamesModel = toModel(games)
        model.addAttribute("gamesOfCurrentYear", gamesModel)
    }

    private fun addGamesOfLastYearData(model: Model) {
        val games = gameService.getGamesOfLastYear()
        val gamesModel = toModel(games)
        model.addAttribute("gamesOfLastYear", gamesModel)
    }

    private fun addGamesOfOtherYears(model: Model) {
        val games = gameService.getGamesOfOtherYears()
        val gamesModel = toModel(games)
        model.addAttribute("gamesOfOtherYears", gamesModel)
    }

    private fun toModel(games: List<Game>): List<GameModel> {
        return games.stream()
                .sorted(gameComparator)
                .map(this::transform)
                .collect(toList())
    }

    private fun transform(data: Game): GameModel {
        val model = GameModel()
        with(data) {
            year.let { model.year = it }
            title.let { model.title = it }
            platform.let { model.platform = it }
            score?.let { model.score = "${it} / 10" }
            progress?.let { model.progress = "${(it * 100).toInt()}%" }
            finished.let { model.finished = it }
        }
        return model
    }

    data class GameModel(
            var year: Int = 0,
            var title: String = "-",
            var platform: String = "-",
            var score: String = "-",
            var progress: String = "-",
            var finished: Boolean = false
    )

}
