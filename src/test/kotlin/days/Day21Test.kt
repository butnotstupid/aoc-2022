package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day21Test {

    private val dayTwentyOne = Day21()

    @Test
    fun testPartOne() {
        val partOne = dayTwentyOne.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(87457751482938L))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTwentyOne.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(3221245824363L))
    }
}
