package eu.sweetlygeek.lorembarnak

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LorembarnakTest {

    private fun checkLorembarnak(text: String, matches: Sequence<MatchResult>, count: Int? = null) {
        assertEquals('.', text.last(), "Last char in $text should be a period")
        if (count == null) {
            assertFalse(matches.none(), "There should be at least a swear word in $text")
        } else {
            assertEquals(count, matches.count(), "There should be $count swear words in $text")
        }
        val firstMatch = matches.first()
        val firstSwear = firstMatch.value
        assertTrue(firstMatch.range.first == 0, "First word $firstSwear in $text should be a swear word")
        assertEquals(firstSwear.take(1).toUpperCase(), firstSwear.take(1), "First swear $firstSwear in $text should be capitalized")
        matches.drop(1).forEach { match ->
            val value = match.value
            assertEquals(value.toLowerCase(), value, "Swear word $value in $text should not be capitalized")
        }
    }

    @Test
    fun testRandom() {
        val text = Lorembarnak.getText()
        val matches = swearRegex.findAll(text)
        checkLorembarnak(text, matches)
    }

    @Test
    fun testSpecified() {
        val text = Lorembarnak.getText(5)
        val matches = swearRegex.findAll(text)
        checkLorembarnak(text, matches, 5)
    }

    companion object {
        private val swearRegex = Lorembarnak
            .swears
            .flatMap { array ->
                array.map { swear -> Regex.escape(swear) }
            }
            .joinToString("|")
            .let { pattern -> "($pattern)".toRegex(RegexOption.IGNORE_CASE) }
    }
}