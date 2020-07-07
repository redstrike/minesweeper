package minesweeper

import kotlin.random.Random

fun main() {
    val field = "........."
    for (i in 1..9) {
        val chars = field.toCharArray()
        chars[Random.nextInt(0, 8)] = 'X'
        println(chars)
    }
}
