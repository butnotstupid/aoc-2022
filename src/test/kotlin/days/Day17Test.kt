package days

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test

class Day17Test {

    private val daySeventeen = Day17()

    @Test
    fun testPartOne() {
        val partOne = daySeventeen.partOne()
        println("\nPART ONE: $partOne")
        assertThat(partOne, `is`(3197))
    }

    @Test
    fun testPartTwo() {
        val partTwo = daySeventeen.partTwo()
        println("PART TWO: $partTwo\n")
        assertThat(partTwo, `is`(1568513119571L))
    }
}
