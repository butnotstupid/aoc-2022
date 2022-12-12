package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day11Test {

    private val dayEleven = Day11()

    @Test
    fun testPartOne() {
        val partOne = dayEleven.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(69918L))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayEleven.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(19573408701L))
    }
}
