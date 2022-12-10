package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day9Test {

    private val dayNine = Day9()

    @Test
    fun testPartOne() {
        val partOne = dayNine.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(6018))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayNine.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(2619))
    }
}
