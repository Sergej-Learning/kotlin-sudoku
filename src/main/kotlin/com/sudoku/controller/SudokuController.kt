package com.sudoku.controller

import com.sudoku.model.SudokuGame
import com.sudoku.view.SudokuView

class SudokuController {
    private val game = SudokuGame()
    private lateinit var view: SudokuView

    fun setView(view: SudokuView) {
        this.view = view
    }

    fun startNewGame() {
        println("Starting new game...")
        game.generateNewBoard()
        view.updateBoard(game.getBoard())
    }

    fun solveGame() {
        println("Solving game...")
        game.solve()
        view.updateBoard(game.getBoard())
        view.highlightSolvedBoard(game.getBoard())
    }

    fun checkBoard() {
        println("Checking board...")
        val isCorrect = game.checkBoard()
        view.displayCheckResult(isCorrect)
        if (!isCorrect) {
            highlightBoardErrors()
        }
    }

    private fun highlightBoardErrors() {
        val board = game.getBoard()
        for (i in 0 until board.size) {
            for (j in 0 until board[i].size) {
                val value = board[i][j]
                if (value == null || value != game.getSolutionBoard()[i][j]) {
                    view.highlightIncorrectCell(i, j)
                } else {
                    view.highlightCorrectCell(i, j)
                }
            }
        }
    }

    fun handleCellInput(row: Int, col: Int, value: Int?) {
        if (value != null && (value in 1..9)) {
            println("Handling cell input at ($row, $col) with value $value")
            game.setCell(row, col, value)
        } else {
            view.displayError("Invalid input: only numbers 1-9 are allowed.")
        }
    }

    fun saveGame() {
        println("Saving game...")
        game.save()
    }

    fun loadGame() {
        println("Loading game...")
        game.load()
        view.updateBoard(game.getBoard())
    }

    fun giveHint() {
        println("Giving hint...")
        val hint = game.giveHint()
        if (hint != null) {
            val (row, col) = hint
            view.highlightHintCell(row, col)
            view.updateBoard(game.getBoard())
        }
    }

    fun hasEmptyCell(): Boolean {
        return game.hasEmptyCell()
    }
}
