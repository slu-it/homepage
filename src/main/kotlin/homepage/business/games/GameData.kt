package homepage.business.games

data class GameData(
        var year: Int? = null,
        var title: String? = null,
        var platform: String? = null,
        var score: Int? = null,
        var progress: Float? = null
)