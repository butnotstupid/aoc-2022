package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day20Test {

    private val dayTwenty = Day20()

    @Test
    fun testPartOne() {
        val partOne = dayTwenty.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(1591L))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTwenty.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(14579387544492L))
    }
}
