package days

class Day7 : Day(7) {

    override fun partOne(): Any {
        val root = Directory("/", null)
        readInput(root)
        return getAllSizesThat(root) { it < 100000 }.sum()
    }

    override fun partTwo(): Any {
        val root = Directory("/", null)
        readInput(root)
        val (totalSize, requiredSize) = 70000000 to 30000000
        return (totalSize - root.size).let { currentlyFree ->
            getAllSizesThat(root) { currentlyFree + it >= requiredSize }.minOrNull()!!
        }
    }

    private fun readInput(root: Directory) {
        var currentDir = root
        var line = 0
        while (line < inputList.size) {
            val input = inputList[line]
            val (command, param) = "\\$ (cd|ls)( .*)?"
                .toRegex().matchEntire(input)!!.groupValues.drop(1)
                .map { it.trim() }
            when (command) {
                "cd" -> {
                    when (param) {
                        "/" -> {
                            currentDir = root
                        }
                        ".." -> {
                            currentDir = currentDir.parent
                                ?: throw IllegalStateException("Cannot go to parent directory of $currentDir")
                        }
                        else -> {
                            currentDir = currentDir.contents.find { it.name == param } as Directory?
                                ?: throw IllegalStateException("Cannot go to $param directory of $currentDir")
                        }
                    }
                    line += 1
                }
                "ls" -> {
                    line = readContents(currentDir, inputList, line + 1)
                }
                else -> throw IllegalArgumentException("Unknown command: $command")
            }
        }
    }

    private fun getAllSizesThat(obj: FileSystemObj, filter: (Int) -> Boolean): Collection<Int> {
        return obj.size.let {
            if (obj is Directory && filter(it)) listOf(it) else emptyList()
        }.plus(
            if (obj is Directory) obj.contents.flatMap { getAllSizesThat(it, filter) } else emptyList()
        )
    }

    private fun readContents(currentDir: Directory, inputList: List<String>, startLine: Int): Int {
        var line = startLine
        val contents = mutableListOf<FileSystemObj>()
        while(line < inputList.size && !inputList[line].startsWith('$')){
            val (left, right) = inputList[line].split(" ")
            when (left) {
                "dir" -> contents.add(Directory(right, currentDir))
                else -> contents.add(File(right, currentDir, left.toInt()))
            }
            line += 1
        }
        currentDir.contents = contents
        return line
    }

    data class Directory(
        val dirName: String,
        val parent: Directory?,
        var contents: List<FileSystemObj> = emptyList()
    ) : FileSystemObj() {
        override val name = dirName
        override val size: Int by lazy {
            contents.sumOf { it.size }
        }
    }

    data class File(val fileName: String, val parent: Directory, val fileSize:Int): FileSystemObj() {
        override val name: String = fileName
        override val size: Int = fileSize
    }

    abstract class FileSystemObj{
        abstract val name: String
        abstract val size: Int
    }
}

