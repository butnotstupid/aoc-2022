package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day5Test {

    private val dayFive = Day5()

    @Test
    fun testPartOne() {
        val partOne = dayFive.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`("SVFDLGLWV"))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayFive.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`("DCVTCVPCL"))
    }
}
