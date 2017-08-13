package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

internal class GameDataTest : JsonSerializableContract<GameData> {

    override val minInstance = GameData()

    override val maxInstance = GameData().apply {
        year = 2017
        title = "Horizon: Zero Dawn"
        platform = "PS4"
        score = 10
        progress = 1.00f
        finished = true
    }


    @Test fun `unknown properties are ignored`() {

        val json = """
        {
          "year": 2015,
          "title": "Bloodborne",
          "platform": "PS4",
          "score": 10,
          "progress": 1.00,
          "finished": true,
          "unknown": true
        }
        """.trimIndent()

        ObjectMapper().readValue(json, GameData::class.java)
        // no exception

    }


}

