package com.sudoku

import com.sudoku.controller.SudokuController
import com.sudoku.view.SudokuView

fun main() {
    val controller = SudokuController()
    val view = SudokuView(controller)
    controller.setView(view)
    println("Sudoku Application Started")
}
