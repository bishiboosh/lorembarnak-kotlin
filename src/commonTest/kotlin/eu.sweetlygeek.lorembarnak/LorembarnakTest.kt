package eu.sweetlygeek.lorembarnak

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LorembarnakTest {

    private fun String.checkLorembarnak(count: Int? = null) {
        val matches = swearRegex.findAll(this)
        if (!isEmpty()) {
            assertEquals('.', last(), "Last char in $this should be a period")
        }
        if (count == null) {
            assertFalse(matches.none(), "There should be at least a swear word in $this")
        } else {
            assertEquals(count, matches.count(), "There should be $count swear words in $this")
        }
        if (!isEmpty()) {
            val firstMatch = matches.first()
            val firstSwear = firstMatch.value
            assertTrue(firstMatch.range.first == 0, "First word $firstSwear in $this should be a swear word")
            assertEquals(
                firstSwear.take(1).toUpperCase(),
                firstSwear.take(1),
                "First swear $firstSwear in $this should be capitalized"
            )
            matches.drop(1).forEach { match ->
                val value = match.value
                val range = match.range
                assertEquals(value.toLowerCase(), value, "Swear word $value in $this should not be capitalized")
                if (vowelsRegex.matches(value.take(1))) {
                    assertEquals(
                        "d'",
                        substring(range.first - 2, range.first),
                        "Swear word $value in $this should be prefixed with d'"
                    )
                } else {
                    assertEquals(
                        "de ",
                        substring(range.first - 3, range.first),
                        "Swear word $value in $this should be prefixed with de"
                    )
                }
            }
        }
    }

    @Test
    fun testRandom() {
        repeat(10) {
            Lorembarnak.getText().checkLorembarnak()
        }
    }

    @Test
    fun testSpecified() {
        repeat(10) { index ->
            Lorembarnak.getText(index).checkLorembarnak(index)
        }
    }

    companion object {
        private val vowelsRegex = Lorembarnak.VOWELS.toRegex()
        private val swearRegex = Lorembarnak
            .swears
            .flatMap { array ->
                array.map { swear -> Regex.escape(swear) }
            }
            .joinToString("|")
            .let { pattern -> "($pattern)".toRegex(RegexOption.IGNORE_CASE) }
    }
}