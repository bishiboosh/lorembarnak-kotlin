package eu.sweetlygeek.lorembarnak

import kotlin.random.Random

/**
 * *Québécois* swear generator.
 */
public object Lorembarnak {

    internal val swears = arrayOf(
        arrayOf("tabarnak", "tabarnouche", "tabarouette", "taboire", "tabarslaque"),
        arrayOf("câlisse", "câlique", "câline", "câline de bine", "câliboire"),
        arrayOf("crisse", "christie", "crime"),
        arrayOf("ostie", "astie", "estique", "ostifie", "esprit"),
        arrayOf("ciboire", "saint-ciboire"),
        arrayOf("torrieux"),
        arrayOf("cimonaque", "saint-cimonaque"),
        arrayOf("baptême", "batince"),
        arrayOf("bâtard"),
        arrayOf("calvaire", "calvince"),
        arrayOf("mosus"),
        arrayOf("maudit", "mautadit", "maudine"),
        arrayOf("sacrament"),
        arrayOf("viarge", "sainte-viarge", "bout d'viarge"),
        arrayOf("cibouleau"),
        arrayOf("sacréfice"),
        arrayOf("cibole", "cibolac"),
        arrayOf("enfant d'chienne"),
        arrayOf("verrat"),
        arrayOf("marde", "maudite marde"),
        arrayOf("boswell"),
        arrayOf("sacristi", "sapristi"),
        arrayOf("jésus de plâtre"),
        arrayOf("torvisse"),
        arrayOf("patente à gosse"),
        arrayOf("viande à chien"),
        arrayOf("bout d'crisse"),
        arrayOf("cul"),
        arrayOf("jésus marie joseph"),
        arrayOf("charrue"),
        arrayOf("charogne"),
        arrayOf("gériboire")
    )

    private val startsWithprefixRegex = "^(de\\s|d')".toRegex()

    private val startsWithVowelRegex = "^[aeiouhyAEIOUHYÀ-ÖØ-öø-ÿ]".toRegex()

    private fun fullSwears(): MutableList<MutableList<String>> {
        return swears.mapTo(mutableListOf()) { array -> array.toMutableList() }
    }

    /**
     * Generate a swear with [nbRequested] words if supplied, or a random number of them
     */
    public fun getText(nbRequested: Int = Random.nextInt(4) + 6): String {
        var remaining = fullSwears()
        val result = StringBuilder()
        var previousSwear: String? = null
        var previousIndex = -1

        for (i in 0 until nbRequested) {
            // If we've run out of remaining swears or only the previous family remains, reinitialize remaining.
            if (remaining.isEmpty() || (remaining.size == 1 && previousIndex >= 0)) {
                remaining = fullSwears()
            }

            // Choose a random swear family that isn't the previous one.
            var currentIndex = Random.nextInt(remaining.size)
            while (currentIndex == previousIndex || previousSwear in remaining[currentIndex]) {
                currentIndex = Random.nextInt(remaining.size)
            }
            val family = remaining[currentIndex].toMutableList()
            previousIndex = currentIndex

            // Choose a random swear, remove it from family, and delete the family if empty
            val currentSwear = family.random()
            previousSwear = currentSwear
            family.remove(currentSwear)
            if (family.isEmpty()) {
                remaining.removeAt(currentIndex)
                previousIndex = -1
            }

            // Capitalize the fist swear, add an article prefix to others.
            if (i == 0) {
                result.append(currentSwear.capitalize())
            } else {
                when {
                    // If it already starts with `de` or `d'`, don't add another.
                    startsWithprefixRegex.containsMatchIn(currentSwear) -> Unit
                    // If it starts with a vowel, prepend with "d'"
                    startsWithVowelRegex.containsMatchIn(currentSwear) -> result.append("d'")
                    // Otherwise prepend with "de"
                    else -> result.append("de ")
                }
                result.append(currentSwear)
            }

            // Add a period after the last swear, a space after others.
            result.append(if (i == nbRequested - 1) '.' else ' ')
        }

        return result.toString()
    }
}