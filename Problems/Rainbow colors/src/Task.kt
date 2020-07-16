import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val color = scanner.next().toUpperCase()
    println(color in Rainbow.values().map { it.name })
}

enum class Rainbow {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    INDIGO,
    VIOLET
}
