import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val h1 = scanner.nextInt()
    val h2 = scanner.nextInt()
    val h3 = scanner.nextInt()
    // Compare boys' heights
    println(h1 >= h2 && h2 >= h3 || h1 <= h2 && h2 <= h3)
    // Bonus: IntelliJ IDEA suggested converting these comparisons to range checks :o
    // println(h2 in h3..h1 || h2 in h1..h3)
}