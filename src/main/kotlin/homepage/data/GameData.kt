package homepage.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GameData(
        var year: Int? = null,
        var title: String? = null,
        var platform: String? = null,
        var score: Int? = null,
        var progress: Float? = null,
        var finished: Boolean? = null
)
