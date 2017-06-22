package homepage.data

internal class GameDataTest : JsonSerializableContract<GameData> {

    override val minInstance = GameData()

    override val maxInstance = GameData().apply {
        year = 2017
        title = "Horizon: Zero Dawn"
        platform = "PS4"
        score = 10
        progress = 1.00f
    }

}

