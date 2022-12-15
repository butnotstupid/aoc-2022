package days

import kotlin.math.abs

class Day15 : Day(15) {

    override fun partOne(): Any {
        val targetRow = 2000000
        val input = parseInput()
        val sensors = input.mapValues { (s, b) -> manDistance(s, b) }
        val beacons = input.values.distinct()
        val events = intervalEvents(sensors, targetRow)

        var nonBeacon = 0
        var curStart: Int? = null
        var layers = 0
        events.forEach { event ->
            when (event.type) {
                EventType.START -> {
                    if (layers == 0) curStart = event.at
                    layers++
                }
                EventType.END -> {
                    layers--
                    if (layers == 0) nonBeacon += event.at - curStart!! + 1
                }
            }
        }

        val minusBeacons = beacons.count { it.row == targetRow }
        val minusSensors = sensors.keys.count { it.row == targetRow }

        return nonBeacon - minusBeacons - minusSensors
    }

    override fun partTwo(): Any {
        val maxBound = 4000000
        val input = parseInput()
        val sensors = input.mapValues { (s, b) -> manDistance(s, b) }

        for (y in 0..maxBound) {
            val events = intervalEvents(sensors, y)

            var lastEnd: Int? = null
            var curStart: Int
            var layers = 0
            events.forEach { event ->
                when (event.type) {
                    EventType.START -> {
                        if (layers == 0) {
                            curStart = event.at
                            if (lastEnd != null && curStart - lastEnd!! == 2) {
                                return tuningFrequency(Point(y, curStart - 1))
                            }
                        }
                        layers++
                    }
                    EventType.END -> {
                        layers--
                        if (layers == 0) lastEnd = event.at
                    }
                }
            }
        }

        throw IllegalStateException("Couldn't find the signal location")
    }

    private fun intervalEvents(sensors: Map<Point, Int>, targetRow: Int): List<Event> {
        val intervals = sensors
            .filter { (sensor, rad) -> abs(sensor.row - targetRow) <= rad }
            .map { (sensor, rad) ->
                val l = sensor.col - (rad - abs(sensor.row - targetRow))
                val r = sensor.col + (rad - abs(sensor.row - targetRow))
                l..r
            }
        return intervals
            .flatMap { listOf(Event(it.first, EventType.START), Event(it.last, EventType.END)) }
            .sortedBy { it.at }
    }

    private fun parseInput(): Map<Point, Point> {
        return inputList.associate { line ->
            val (Scol, Srow, Bcol, Brow) = "Sensor at x=(-?\\d*), y=(-?\\d*): closest beacon is at x=(-?\\d*), y=(-?\\d*)".toRegex()
                .matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
            Point(Srow, Scol) to Point(Brow, Bcol)
        }
    }

    private fun manDistance(from: Point, to: Point): Int {
        return abs(from.row - to.row) + abs(from.col - to.col)
    }

    private fun tuningFrequency(point: Point): Long {
        return point.col * 4000000L + point.row
    }

    data class Point(val row: Int, val col: Int)
    data class Event(val at: Int, val type: EventType)
    enum class EventType { START, END }
}
