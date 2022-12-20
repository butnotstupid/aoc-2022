package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day18Test {

    private val dayEighteen = Day18()

    @Test
    fun testPartOne() {
        val partOne = dayEighteen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(3498))
    }

    @Test
    fun testPartTwo() {
        val partTwo = dayEighteen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(2008))
    }
}
