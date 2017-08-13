package homepage.business.objects

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class GameTest {

    @Test
    fun `valid games can be instantiated`() {
        Game(year = 2017, title = "A Game", platform = "PS4")
        Game(year = 2017, title = "A Game", platform = "PS4", score = 10, progress = 1.0f, finished = true)
        Game(year = 2017, title = "A Game", platform = "PS4", score = 0, progress = 0.0f, finished = false)
        Game(year = 2017, title = "A Game", platform = "PS4", score = 0, progress = 0.0f, finished = false)
    }

    @Test
    fun `the year of a game must be greater than 1900`() {
        Game(year = 1901, title = "A Game", platform = "PS4")
        Game(year = 1900, title = "A Game", platform = "PS4")
        assertThrows(IllegalStateException::class.java, {
            Game(year = 1899, title = "A Game", platform = "PS4")
        })
    }

    @Test
    fun `the title of a game must not be blank`() {
        assertThrows(IllegalStateException::class.java, {
            Game(year = 2017, title = "", platform = "PS4")
        })
    }

    @Test
    fun `the platform of a game must not be blank`() {
        assertThrows(IllegalStateException::class.java, {
            Game(year = 2017, title = "A Game", platform = "")
        })
    }

    @Nested
    inner class `score validation` {

        @Test
        fun `if a score is set, it must not be less than 0`() {
            assertThrows(IllegalStateException::class.java, {
                Game(year = 2017, title = "A Game", platform = "PS4", score = -1)
            })
        }

        @ParameterizedTest(name = "a score of {0} is allowed")
        @ValueSource(ints = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        fun `if a score is set, it can be between 0 and 10`(score: Int) {
            Game(year = 2017, title = "A Game", platform = "PS4", score = score)
        }

        @Test
        fun `if a score is set, it must not be greater than 10`() {
            assertThrows(IllegalStateException::class.java, {
                Game(year = 2017, title = "A Game", platform = "PS4", score = 11)
            })
        }

    }

    @Nested
    inner class `progress validation` {

        @Test
        fun `if a progress is set, it must not be less than 0f`() {
            assertThrows(IllegalStateException::class.java, {
                Game(year = 2017, title = "A Game", platform = "PS4", progress = -0.0001f)
            })
        }

        @ParameterizedTest(name = "a progress of {0} is allowed")
        @ValueSource(doubles = doubleArrayOf(0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0))
        fun `if a progress is set, it can be between 0f and 1f`(progress: Double) {
            Game(year = 2017, title = "A Game", platform = "PS4", progress = progress.toFloat())
        }

        @Test
        fun `if a progress is set, it must not be greater than 1f`() {
            assertThrows(IllegalStateException::class.java, {
                Game(year = 2017, title = "A Game", platform = "PS4", progress = 1.0001f)
            })
        }

    }

    @Nested
    inner class `Game DLC` {

        @Test
        fun `valid game with DLC can be instantiated`() {
            Game(
                    year = 2017,
                    title = "A Game",
                    platform = "PS4",
                    dlc = listOf(
                            Game.Dlc(title = "Some DLC"),
                            Game.Dlc(title = "Some DLC", finished = true))
            )
        }

        @Test
        fun `the title of a DLC must not be blank`() {
            assertThrows(IllegalStateException::class.java, {
                Game.Dlc(title = "")
            })
        }

    }

}