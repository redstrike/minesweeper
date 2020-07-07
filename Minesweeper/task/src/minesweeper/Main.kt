package minesweeper

import kotlin.random.Random
import java.util.Scanner
import kotlin.math.round

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    val totalMines = scanner.nextInt()
    val fieldLine = "........." // 9 safe cells
    var usedMines = 0
    var minMines = 0
    for (line in 1..9) {
        val lineChars = fieldLine.toCharArray()
        // Increase min mines per line to ensure total of mines in the worst case (line 1 has no mine)
        if (line == 2) {
            val remainingMines = totalMines - usedMines
            minMines = round(remainingMines.toFloat() / 8).toInt().coerceAtMost(6)
        }
        // Placing mines on field line
        if (usedMines < totalMines) {
            var mines = Random.nextInt(minMines, 8) // until >= 8
            while (mines > 0) {
                if (usedMines == totalMines) break
                var pos = Random.nextInt(0, 8)
                while (lineChars[pos] == 'X') {
                    pos = Random.nextInt(0, 8)
                }
                lineChars[pos] = 'X'
                usedMines++
                mines--
            }
        }
        println(lineChars)
    }
}
