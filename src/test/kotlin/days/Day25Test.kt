package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day25Test {

    private val dayTwentyFive = Day25()

    @Test
    fun testPartOne() {
        val partOne = dayTwentyFive.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`("2=000=22-0-102=-1001"))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTwentyFive.partTwo()
        println("PART TWO: $partTwo\n")
    }
}
