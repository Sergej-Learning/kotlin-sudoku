package com.sudoku.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.random.Random

@Serializable
data class SudokuBoard(val board: Array<Array<Int?>>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SudokuBoard) return false
        return board.contentDeepEquals(other.board)
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }
}

class SudokuGame {
    private val boardSize = 9
    private val subGridSize = 3
    private var board = Array(boardSize) { Array<Int?>(boardSize) { null } }
    private val solutionBoard = Array(boardSize) { Array<Int?>(boardSize) { null } }

    fun generateNewBoard() {
        println("Generating new board...")
        resetBoard()
        if (fillBoard()) {
            saveSolution()
            removeNumbers()
            println("New board generated.")
        } else {
            println("Failed to generate a valid board.")
        }
    }

    private fun resetBoard() {
        println("Resetting board...")
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board[i][j] = null
            }
        }
    }

    private fun fillBoard(): Boolean {
        println("Filling board...")
        return fillRemaining(0, 0)
    }

    private fun fillRemaining(row: Int, col: Int): Boolean {
        var currentRow = row
        var currentCol = col

        if (currentRow == boardSize - 1 && currentCol == boardSize) return true
        if (currentCol == boardSize) {
            currentRow++
            currentCol = 0
        }
        if (board[currentRow][currentCol] != null) return fillRemaining(currentRow, currentCol + 1)

        val numbers = (1..9).shuffled()
        for (num in numbers) {
            if (isSafeToPlace(currentRow, currentCol, num)) {
                board[currentRow][currentCol] = num
                if (fillRemaining(currentRow, currentCol + 1)) return true
                board[currentRow][currentCol] = null
            }
        }
        return false
    }

    private fun isSafeToPlace(row: Int, col: Int, num: Int): Boolean {
        return !isInRow(row, num) && !isInCol(col, num) && !isInBox(row, col, num) && !isInDiagonal(row, col, num)
    }

    private fun isInRow(row: Int, num: Int): Boolean {
        return board[row].contains(num)
    }

    private fun isInCol(col: Int, num: Int): Boolean {
        for (i in 0 until boardSize) {
            if (board[i][col] == num) return true
        }
        return false
    }

    private fun isInBox(row: Int, col: Int, num: Int): Boolean {
        val boxRowStart = row - row % subGridSize
        val boxColStart = col - col % subGridSize
        for (i in 0 until subGridSize) {
            for (j in 0 until subGridSize) {
                if (board[boxRowStart + i][boxColStart + j] == num) return true
            }
        }
        return false
    }

    private fun isInDiagonal(row: Int, col: Int, num: Int): Boolean {
        if (row == col) {
            for (i in 0 until boardSize) {
                if (board[i][i] == num) return true
            }
        }

        if (row + col == boardSize - 1) {
            for (i in 0 until boardSize) {
                if (board[i][boardSize - 1 - i] == num) return true
            }
        }

        return false
    }

    private fun saveSolution() {
        println("Saving solution...")
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                solutionBoard[i][j] = board[i][j]
            }
        }
    }

    private fun removeNumbers() {
        println("Removing numbers to create puzzle...")
        val random = Random(System.currentTimeMillis())
        var count = 0
        while (count < boardSize * boardSize - 22) {
            val row = random.nextInt(boardSize)
            val col = random.nextInt(boardSize)
            if (board[row][col] != null) {
                val temp = board[row][col]
                board[row][col] = null
                if (!hasUniqueSolution()) {
                    board[row][col] = temp
                } else {
                    count++
                }
            }
        }
    }

    private fun hasUniqueSolution(): Boolean {
        var solutionCount = 0

        fun solve(row: Int, col: Int): Boolean {
            if (row == boardSize) {
                solutionCount++
                return solutionCount == 1
            }

            val nextRow = if (col == boardSize - 1) row + 1 else row
            val nextCol = if (col == boardSize - 1) 0 else col + 1

            if (board[row][col] != null) {
                return solve(nextRow, nextCol)
            }

            for (num in 1..9) {
                if (isSafeToPlace(row, col, num)) {
                    board[row][col] = num
                    if (solve(nextRow, nextCol)) {
                        board[row][col] = null
                        return true
                    }
                    board[row][col] = null
                }
            }

            return false
        }

        solve(0, 0)
        return solutionCount == 1
    }

    fun getBoard(): Array<Array<Int?>> {
        return board
    }

    fun getSolutionBoard(): Array<Array<Int?>> {
        return solutionBoard
    }

    fun setCell(row: Int, col: Int, value: Int) {
        if (isSafeToPlace(row, col, value)) {
            board[row][col] = value
        }
    }

    fun checkBoard(): Boolean {
        println("Checking board...")
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (board[i][j] != solutionBoard[i][j]) {
                    return false
                }
            }
        }
        return true
    }

    fun isValidMove(row: Int, col: Int, value: Int): Boolean {
        val temp = board[row][col]
        board[row][col] = null

        val isValid = isSafeToPlace(row, col, value)

        board[row][col] = temp

        return isValid
    }

    fun solve() {
        println("Solving board...")
        resetBoard()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board[i][j] = solutionBoard[i][j]
            }
        }
        solveBoard(0, 0)
    }

    private fun solveBoard(row: Int, col: Int): Boolean {
        var currentRow = row
        var currentCol = col
        if (currentRow == boardSize - 1 && currentCol == boardSize) return true
        if (currentCol == boardSize) {
            currentRow++
            currentCol = 0
        }
        if (board[currentRow][currentCol] != null) return solveBoard(currentRow, currentCol + 1)
        for (num in 1..9) {
            if (isSafeToPlace(currentRow, currentCol, num)) {
                board[currentRow][currentCol] = num
                if (solveBoard(currentRow, currentCol + 1)) return true
                board[currentRow][currentCol] = null
            }
        }
        return false
    }

    fun save() {
        println("Saving game to JSON file...")
        val json = Json.encodeToString(SudokuBoard(board))
        File("Savefile.json").writeText(json)
    }

    fun load() {
        println("Loading game from JSON file...")
        val json = File("Savefile.json").readText()
        val loadedBoard = Json.decodeFromString<SudokuBoard>(json)
        board = loadedBoard.board
    }

    fun giveHint(): Pair<Int, Int>? {
        println("Giving hint...")
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (board[i][j] == null) {
                    emptyCells.add(Pair(i, j))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            board[row][col] = solutionBoard[row][col]
            return Pair(row, col)
        }
        return null
    }

    fun hasEmptyCell(): Boolean {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (board[i][j] == null) {
                    return true
                }
            }
        }
        return false
    }
}
