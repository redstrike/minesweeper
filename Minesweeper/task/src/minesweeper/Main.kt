package minesweeper

import kotlin.random.Random
import java.util.Scanner
import kotlin.math.round

fun main() {
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field? ")
    // Create and print minefield
    val totalMines = scanner.nextInt()
    val minefield = addMineHints(createMinefield(totalMines))
    printMinefield(minefield)
    // Start game
    startMineSweeper(scanner, minefield, totalMines)
}

fun startMineSweeper(scanner: Scanner, minefield: Array<CharArray>, totalMines: Int) {
    var marksCount = 0
    var mineMarksCount = 0
    loop@ while (true) {
        // Get mark coordinates
        print("Set/delete mines marks (x and y coordinates): ")
        val x = scanner.nextInt() - 1
        val y = scanner.nextInt() - 1
        // Mark or un-mark field cell
        when (minefield[y][x]) {
            in '1'..'8' -> {
                println("There is a number here!")
                continue@loop
            }
            'X' -> {
                minefield[y][x] = '*'
                marksCount++
                mineMarksCount++
            }
            '.' -> {
                minefield[y][x] = '*'
                marksCount++
            }
            '*' -> {
                minefield[y][x] = '.'
                marksCount--
            }
        }
        // Check end game
        if (mineMarksCount == totalMines && mineMarksCount == marksCount) {
            return println("Congratulations! You found all the mines!")
        }
        // Print minefield with new state
        printMinefield(minefield)
    }
}

fun printMinefield(minefield: Array<CharArray>) {
    for (i in 0..8) {
        if (i == 0) {
            println(" │123456789│")
            println("—│—————————│")
        }
        print("${i + 1}│")
        print(minefield[i].joinToString("").replace('X', '.'))
        println("│")
        if (i == 8) {
            println("—│—————————│")
        }
    }
}

fun addMineHints(field: Array<CharArray>): Array<CharArray> {
    val range = 0..8
    for (y in range) {
        for (x in range) {
            val currentCell = field[y][x]
            // Skip checking if current cell has a mine
            if (currentCell == 'X') {
                continue
            }
            // Get around cells' values and add them to an array
            val aroundCells = arrayOfNulls<Char>(8)
            val ym1 = y - 1 in range
            val yp1 = y + 1 in range
            val xm1 = x - 1 in range
            val xp1 = x + 1 in range
            aroundCells[0] = if (ym1) field[y - 1][x] else null // topCell
            aroundCells[1] = if (yp1) field[y + 1][x] else null // bottomCell
            aroundCells[2] = if (xm1) field[y][x - 1] else null // leftCell
            aroundCells[3] = if (xp1) field[y][x + 1] else null // rightCell
            aroundCells[4] = if (ym1 && xm1) field[y - 1][x - 1] else null // topLeftCell
            aroundCells[5] = if (ym1 && xp1) field[y - 1][x + 1] else null // topRightCell
            aroundCells[6] = if (yp1 && xm1) field[y + 1][x - 1] else null // bottomLeftCell
            aroundCells[7] = if (yp1 && xp1) field[y + 1][x + 1] else null // bottomRightCell
            // Check mines around the cell
            var minesCount = 0
            for (cell in aroundCells) {
                if (cell == 'X') {
                    minesCount++
                }
            }
            if (minesCount > 0) {
                field[y][x] = minesCount.toString().first()
            }
        }
    }
    return field
}

fun createMinefield(totalMines: Int): Array<CharArray> {
    val field = Array(9) { CharArray(9) { '.' } } // 9 safe cells per field line
    var minMines = 0
    var usedMines = 0
    for (i in 0..8) {
        val line = field[i]
        // Increase min mines per line to ensure total of mines in the worst case (line 1 has no mine)
        if (i == 1) {
            val remainingMines = totalMines - usedMines
            minMines = round(remainingMines.toFloat() / 8).toInt().coerceAtMost(6)
        }
        // Placing mines on field line
        if (usedMines < totalMines) {
            var mines = Random.nextInt(minMines, 8) // until >= 8
            while (mines > 0) {
                if (usedMines == totalMines) break
                var pos = Random.nextInt(0, 8)
                while (line[pos] == 'X') {
                    pos = Random.nextInt(0, 8)
                }
                line[pos] = 'X'
                usedMines++
                mines--
            }
        }
    }
    return field
}
