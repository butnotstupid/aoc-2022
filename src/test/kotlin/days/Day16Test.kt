package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day16Test {

    private val daySixteen = Day16()

    @Test
    fun testPartOne() {
        val partOne = daySixteen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(1720))
    }

    @Test
    fun testPartTwo() {
        val partTwo = daySixteen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(2582))
    }
}
