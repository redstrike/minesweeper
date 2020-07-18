package minesweeper

import java.util.Scanner
import kotlin.math.round
import kotlin.random.Random

typealias Field = Array<CharArray>

private const val TOTAL_MINEFIELD_CELLS = 81
private const val CONGRATS_MESSAGE = "Congratulations! You found all the mines!"

private var totalMines = 0
private var inGame = true
private var marksCount = 0
private var mineMarksCount = 0
private var exploredCellsCount = 0

fun main() {
    // Create minefield
    print("How many mines do you want on the field? ")
    val scanner = Scanner(System.`in`)
    totalMines = scanner.nextInt()
    val minefield = addMineHints(createMinefield(totalMines))
    // Print masked minefield
    val maskedField = Array(9) { CharArray(9) { '.' } } // 9 safe cells per field line
    printMinefield(maskedField)
    // Start game
    startMinesweeper(scanner, minefield, maskedField)
}

fun startMinesweeper(scanner: Scanner, minefield: Field, maskedField: Field) {
    while (inGame) {
        // Get x and y coordinates with a following command (mine/free)
        print("Set/unset mines marks or claim a cell as free: ")
        val x = scanner.nextInt() - 1
        val y = scanner.nextInt() - 1
        when (scanner.next().toLowerCase()) {
            "mine" -> markCell(x, y, minefield, maskedField)
            "free" -> freeCell(x, y, minefield, maskedField)
        }
    }
}

fun markCell(x: Int, y: Int, minefield: Field, maskedField: Field) {
    val isUnmarkedCell = maskedField[y][x] == '.'
    val isMarkedCell = maskedField[y][x] == '*' // explored masked cells can be numbers
    when (minefield[y][x]) {
        'X' -> {
            if (isUnmarkedCell) {
                maskedField[y][x] = '*'
                marksCount++
                mineMarksCount++
            } else if (isMarkedCell) {
                maskedField[y][x] = '.'
                marksCount--
                mineMarksCount--
            }
        }
        '.' -> {
            if (isUnmarkedCell) {
                maskedField[y][x] = '*'
                marksCount++
            } else if (isMarkedCell) {
                maskedField[y][x] = '.'
                marksCount--
            }
        }
        in '1'..'8' -> {
            if (isUnmarkedCell) {
                maskedField[y][x] = '*'
                marksCount++
            } else if (isMarkedCell) {
                maskedField[y][x] = '.'
                marksCount--
            }
        }
    }
    // Print masked field with new state
    printMinefield(maskedField)

    // Check win if the player has marked all mines correctly
    if (mineMarksCount == totalMines && mineMarksCount == marksCount) {
        return println(CONGRATS_MESSAGE)
    }
}

fun freeCell(x: Int, y: Int, minefield: Field, maskedField: Field) {
    // Explore a cell
    if (minefield[y][x] == 'X') {
        maskedField[y][x] = 'X'
        printMinefield(maskedField)
        inGame = false
        return println("You stepped on a mine and failed!")
    }
    exploreCell(x, y, minefield, maskedField)

    // Print masked field with new state
    printMinefield(maskedField)

    // Check win if the player has explored all safe cells
    if (TOTAL_MINEFIELD_CELLS - exploredCellsCount == totalMines) {
        inGame = false
        return println(CONGRATS_MESSAGE)
    }
}

fun exploreCell(x: Int, y: Int, minefield: Field, maskedField: Field) {
    val isUnexploredCell = maskedField[y][x] == '.' || maskedField[y][x] == '*'
    when (minefield[y][x]) {
        '.' -> {
            // This cell is empty and has no mines around it => automatically explore all cells around it,
            // including the marked ones. Also, explore its next to empty cells with no mines around, recursively.
            if (isUnexploredCell) {
                maskedField[y][x] = '/'
                minefield[y][x] = '/'
                exploredCellsCount++
                exploreAroundCells(x, y, minefield, maskedField)
            }
        }
        in '1'..'8' -> {
            // This cell is empty and has mines around it => only it is explored, revealing a number of mines around it
            if (isUnexploredCell) {
                maskedField[y][x] = minefield[y][x]
                exploredCellsCount++
            }
        }
    }
}

fun exploreAroundCells(x: Int, y: Int, minefield: Field, maskedField: Field) {
    val ym1 = y - 1
    val yp1 = y + 1
    val xm1 = x - 1
    val xp1 = x + 1
    val ym1r = ym1 in 0..8
    val yp1r = yp1 in 0..8
    val xm1r = xm1 in 0..8
    val xp1r = xp1 in 0..8
    // topCell
    if (ym1r) exploreCell(x, ym1, minefield, maskedField)
    // bottomCell
    if (yp1r) exploreCell(x, yp1, minefield, maskedField)
    // leftCell
    if (xm1r) exploreCell(xm1, y, minefield, maskedField)
    // rightCell
    if (xp1r) exploreCell(xp1, y, minefield, maskedField)
    // topLeftCell
    if (ym1r && xm1r) exploreCell(xm1, ym1, minefield, maskedField)
    // topRightCell
    if (ym1r && xp1r) exploreCell(xp1, ym1, minefield, maskedField)
    // bottomLeftCell
    if (yp1r && xm1r) exploreCell(xm1, yp1, minefield, maskedField)
    // bottomRightCell
    if (yp1r && xp1r) exploreCell(xp1, yp1, minefield, maskedField)
}

fun printMinefield(minefield: Field) {
    println()
    println(" │123456789│")
    println("—│—————————│")
    for (i in 0..8) {
        print("${i + 1}│")
        print(minefield[i])
        println("│")
    }
    println("—│—————————│")
}

fun addMineHints(field: Field): Field {
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

fun createMinefield(totalMines: Int): Field {
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
