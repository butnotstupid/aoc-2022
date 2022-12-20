package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day19Test {

    private val dayNineteen = Day19()

    @Test
    fun testPartOne() {
        val partOne = dayNineteen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(1266))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayNineteen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(5800L))
    }
}
