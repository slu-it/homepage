package homepage

import homepage.gaming.GameData
import homepage.gaming.GameService
import homepage.social.SocialService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDate
import java.util.stream.Collectors.toList

@Controller
class ContentController(
        val gameService: GameService,
        val socialService: SocialService
) {

    private val gameComparator = compareByDescending<GameData> { it.score }.thenBy { it.title }

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
    }

    private fun addSocialLinks(model: Model) {
        model.addAttribute("socialLinks", socialService.getSocialLinks())
    }

    private fun addGamesOfCurrentYearData(model: Model) {
        val currentYear = LocalDate.now().year
        val games = getGames({ it.year == currentYear })
        with(model) {
            addAttribute("gamesOfCurrentYear", games)
            addAttribute("currentYear", currentYear)
        }
    }

    private fun addGamesOfLastYearData(model: Model) {
        val lastYear = LocalDate.now().minusYears(1).year
        val games = getGames({ it.year == lastYear })
        with(model) {
            addAttribute("gamesOfLastYear", games)
            addAttribute("lastYear", lastYear)
        }
    }

    private fun addGamesOfOtherYears(model: Model) {
        val lastYear = LocalDate.now().minusYears(1).year
        val games = getGames({ it.year < lastYear })
        with(model) {
            addAttribute("gamesOfOtherYears", games)
        }
    }

    private fun getGames(filter: (GameModel) -> Boolean): List<GameModel> {
        return gameService.getAll()
                .sorted(gameComparator)
                .map(this::transform)
                .filter(filter)
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
