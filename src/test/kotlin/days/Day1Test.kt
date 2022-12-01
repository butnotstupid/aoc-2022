package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day1Test {

    private val dayOne = Day1()

    @Test
    fun testPartOne() {
        val partOne = dayOne.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(70509L))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayOne.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(208567L))
    }
}
