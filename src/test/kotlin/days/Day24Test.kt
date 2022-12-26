package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day24Test {

    private val dayTwentyFour = Day24()

    @Test
    fun testPartOne() {
        val partOne = dayTwentyFour.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(260))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTwentyFour.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(747))
    }
}
