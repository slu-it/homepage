package homepage.business.objects

data class Game(
        val year: Int,
        val title: String,
        val platform: String,
        val score: Int? = null,
        val progress: Float? = null,
        val finished: Boolean = false,
        val dlc: List<Dlc> = listOf()
) {

    init {
        assertThat(this, year, { it >= 1900 }, "The year of a game must be greater than or equal to 1900!")
        assertThat(this, title, { it.isNotBlank() }, "The title of a game must not be blank!")
        assertThat(this, platform, { it.isNotBlank() }, "The platform of a game must not be blank!")
        if (score != null) {
            assertThat(this, score, { it >= 0 }, "The score of a game must be greater than or equal to 0!")
            assertThat(this, score, { it <= 10 }, "The score of a game must be less than or equal to 10!")
        }
        if (progress != null) {
            assertThat(this, progress, { it >= 0.0f }, "The progress of a game must be greater than or equal to 0.0!")
            assertThat(this, progress, { it <= 1.0f }, "The progress of a game must be less than or equal to 1.0!")
        }
    }

    data class Dlc(
            val title: String,
            val finished: Boolean = false
    ) {

        init {
            assertThat(this, title, { it.isNotBlank() }, "The title of a DLC must not be blank!")
        }

    }

}

private fun <T : Any> assertThat(instance: Any, value: T, condition: (T) -> Boolean, msg: String) {
    if (!condition.invoke(value)) {
        throw IllegalStateException("Validation of '$instance' failed: $msg")
    }
}
