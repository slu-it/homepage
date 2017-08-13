package homepage.business.objects

data class Game(
        val year: Int,
        val title: String,
        val platform: String,
        val score: Int? = null,
        val progress: Float? = null,
        val finished: Boolean = false
) {

    init {
        assertThat(year, { year -> year >= 1900 }, "The year of a game must be greater than or equal to 1900!")
        assertThat(title, { title -> title.isNotBlank() }, "The title of a game must not be blank!")
        assertThat(platform, { platform -> platform.isNotBlank() }, "The platform of a game must not be blank!")
        if (score != null) {
            assertThat(score, { score -> score >= 0 }, "The score of a game must be greater than or equal to 0!")
            assertThat(score, { score -> score <= 10 }, "The score of a game must be less than or equal to 10!")
        }
        if (progress != null) {
            assertThat(progress, { progress -> progress >= 0.0f }, "The progress of a game must be greater than or equal to 0.0!")
            assertThat(progress, { progress -> progress <= 1.0f }, "The progress of a game must be less than or equal to 1.0!")
        }
    }

    private fun <T : Any> assertThat(value: T, condition: (T) -> Boolean, msg: String) {
        if (!condition.invoke(value)) {
            throw IllegalStateException("Validation of game '$title' failed: $msg The value was: $value")
        }
    }

}
