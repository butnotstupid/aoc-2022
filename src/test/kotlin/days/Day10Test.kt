package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day10Test {

    private val dayTen = Day10()

    @Test
    fun testPartOne() {
        val partOne = dayTen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(12880))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayTen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`("FCJAPJRE"))
    }
}
