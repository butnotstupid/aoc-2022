package days

class Day19 : Day(19) {
    override fun partOne(): Any {
        val timeLeft = 24
        val blueprints = parseInput()

        return blueprints.sumOf { blueprint ->
            blueprint.id * maxGeodes(blueprint, State.start(timeLeft))
        }
    }

    override fun partTwo(): Any {
        val timeLeft = 32
        val blueprints = parseInput().take(3)

        return blueprints.map { blueprint ->
            maxGeodes(blueprint, State.start(timeLeft)).toLong()
        }.reduce(Long::times)
    }

    private fun maxGeodes(blueprint: Blueprint, startState: State): Int {
        val finStates = mutableListOf<State>()
        val visited = Array(2) { mutableSetOf<State>() }
        var q = ArrayDeque<State>().apply { add(startState).also { visited[0].add(startState) } }
        var curTimeLeft = startState.timeLeft

        while (q.isNotEmpty()) {
            q = cutQueue(curTimeLeft, q)
            val state = q.removeFirst()
            if (state.timeLeft == 0) finStates.add(state)
            else with(state) {
                if (timeLeft < curTimeLeft) {
                    visited[0] = visited[1]
                    visited[1].clear()
                    curTimeLeft = timeLeft
                }
                val produced = Resource(state.oreRobots, state.clayRobots, state.obsRobots)
                listOfNotNull(
                    state,
                    if (state.resource isEnoughFor blueprint.oreCost) state.copy(
                        resource = state.resource - blueprint.oreCost,
                        oreRobots = state.oreRobots + 1
                    ) else null,
                    if (state.resource isEnoughFor blueprint.clayCost) state.copy(
                        resource = state.resource - blueprint.clayCost,
                        clayRobots = state.clayRobots + 1
                    ) else null,
                    if (state.resource isEnoughFor blueprint.obsCost) state.copy(
                        resource = state.resource - blueprint.obsCost,
                        obsRobots = state.obsRobots + 1
                    ) else null,
                    if (state.resource isEnoughFor blueprint.geoCost) state.copy(
                        resource = state.resource - blueprint.geoCost,
                        geoRobots = state.geoRobots.plus(state.timeLeft - 1)
                    ) else null
                ).map {
                    it.copy(resource = it.resource + produced, timeLeft = it.timeLeft - 1)
                }.forEach {
                    if (it !in visited[1]) {
                        visited[1].add(it)
                        q.add(it)
                    }
                }
            }
        }
        return finStates.maxOf { it.geodes }
    }

    private fun cutQueue(time: Int, q: ArrayDeque<State>, cap: Int = 1_000_000, takeTop: Double = 0.9): ArrayDeque<State> {
        val k = 0.25 * (time / 10 + 1)
        return if (q.size > cap) ArrayDeque(q.sortedByDescending {
            it.geodes * 70 +
                    it.resource.obs * 4 +
                    it.resource.ore
        }.take((cap * takeTop * k).toInt()))
        else q
    }

    private fun parseInput(): List<Blueprint> {
        return inputList.map {
            ("Blueprint (\\d*): " +
                    "Each ore robot costs (\\d*) ore. " +
                    "Each clay robot costs (\\d*) ore. " +
                    "Each obsidian robot costs (\\d*) ore and (\\d*) clay. " +
                    "Each geode robot costs (\\d*) ore and (\\d*) obsidian.")
                .toRegex().find(it)?.destructured
                ?.let { (id, oreCostOre, clayCostOre, obsidianCostOre, obsidianCostClay, geodeCostOre, geodeCostObsidian) ->
                    Blueprint(
                        id.toInt(),
                        Resource(oreCostOre.toInt(), 0, 0),
                        Resource(clayCostOre.toInt(), 0, 0),
                        Resource(obsidianCostOre.toInt(), obsidianCostClay.toInt(), 0),
                        Resource(geodeCostOre.toInt(), 0, geodeCostObsidian.toInt()),
                    )
                } ?: error("Failed to parse input string $it")
        }
    }

    data class Blueprint(
        val id: Int,
        val oreCost: Resource,
        val clayCost: Resource,
        val obsCost: Resource,
        val geoCost: Resource,
    )

    data class State(
        val resource: Resource,
        val oreRobots: Int,
        val clayRobots: Int,
        val obsRobots: Int,
        val geoRobots: List<Int>, // timeLeft at geode robots creation
        val timeLeft: Int
    ) {
        val geodes = geoRobots.sum()

        companion object {
            fun start(timeLeft: Int) = State(Resource(0, 0, 0), 1, 0, 0, emptyList(), timeLeft)
        }
    }

    data class Resource(
        val ore: Int,
        val clay: Int,
        val obs: Int,
    ) {
        infix fun isEnoughFor(cost: Resource): Boolean {
            return (ore >= cost.ore && clay >= cost.clay && obs >= cost.obs)
        }

        operator fun minus(another: Resource): Resource {
            assert(ore - another.ore >= 0) { "Resource should be all positive, ore=${ore - another.ore}" }
            assert(clay - another.clay >= 0) { "Resource should be all positive, clay=${clay - another.clay}" }
            assert(obs - another.obs >= 0) { "Resource should be all positive, obs=${obs - another.obs}" }
            return Resource(ore - another.ore, clay - another.clay, obs - another.obs)
        }

        operator fun plus(another: Resource): Resource {
            return Resource(ore + another.ore, clay + another.clay, obs + another.obs)
        }

    }
}

