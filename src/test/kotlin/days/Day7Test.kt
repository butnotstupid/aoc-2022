package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day7Test {

    private val daySeven = Day7()

    @Test
    fun testPartOne() {
        val partOne = daySeven.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(1723892))
    }

    @Test
    fun testPartTwo() {
        val partTwo = daySeven.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(8474158))
    }
}
