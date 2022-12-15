package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day15Test {

    private val dayFifteen = Day15()

    @Test
    fun testPartOne() {
        val partOne = dayFifteen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(5525990))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayFifteen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(11756174628223L))
    }
}
