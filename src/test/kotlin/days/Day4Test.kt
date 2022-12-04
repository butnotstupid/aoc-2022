package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day4Test {

    private val dayFour = Day4()

    @Test
    fun testPartOne() {
        val partOne = dayFour.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(588))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayFour.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(911))
    }
}
