package com.sudoku.view

import com.sudoku.controller.SudokuController
import java.awt.*
import java.util.Timer
import javax.swing.*
import kotlin.concurrent.schedule

class SudokuView(private val controller: SudokuController) : JFrame() {

    private val boardSize = 9
    private val boardFields = Array(boardSize) { Array(boardSize) { JTextField(1) } }

    private val newGameButton = JButton("New Game")
    private val solveButton = JButton("Solve")
    private val checkBoardButton = JButton("Check Board")
    private val saveButton = JButton("Save")
    private val loadButton = JButton("Load")
    private val hintButton = JButton("Hint (5)")

    private var hintCount = 0
    private val maxHints = 5

    init {
        title = "X-Sudoku"
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()
        preferredSize = Dimension(600, 600)

        val sudokuPanel = JPanel(GridLayout(boardSize, boardSize)).apply {
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        }

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val field = boardFields[i][j]
                field.horizontalAlignment = JTextField.CENTER
                field.border = if (i == j || i + j == boardSize - 1) {
                    BorderFactory.createLineBorder(Color.BLUE, 2)
                } else if ((i / 3 + j / 3) % 2 == 0) {
                    BorderFactory.createLineBorder(Color.BLACK, 2)
                } else {
                    BorderFactory.createLineBorder(Color.GRAY)
                }
                field.addActionListener {
                    validateInput(field, i, j)
                }
                sudokuPanel.add(field)
            }
        }

        add(sudokuPanel, BorderLayout.CENTER)

        val buttonPanel = JPanel().apply {
            layout = FlowLayout()
            add(newGameButton)
            add(solveButton)
            add(checkBoardButton)
            add(saveButton)
            add(loadButton)
            add(hintButton)
        }
        add(buttonPanel, BorderLayout.SOUTH)

        newGameButton.addActionListener {
            println("New Game Button Clicked")
            hintCount = 0
            hintButton.text = "Hint ($maxHints)"
            hintButton.isEnabled = true
            controller.startNewGame()
        }
        solveButton.addActionListener {
            println("Solve Button Clicked")
            controller.solveGame()
        }
        checkBoardButton.addActionListener {
            println("Check Board Button Clicked")
            controller.checkBoard()
            Timer().schedule(1500) {
                resetBoardColors()
            }
        }
        saveButton.addActionListener {
            println("Save Button Clicked")
            controller.saveGame()
        }
        loadButton.addActionListener {
            println("Load Button Clicked")
            controller.loadGame()
        }
        hintButton.addActionListener {
            println("Hint Button Clicked")
            if (hintCount < maxHints) {
                if (controller.hasEmptyCell()) {
                    controller.giveHint()
                    hintCount++
                    hintButton.text = "Hint (${maxHints - hintCount})"
                    if (hintCount >= maxHints) {
                        hintButton.isEnabled = false
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Bitte leere eine Zelle, um einen Hint zu erhalten.", "Keine leeren Zellen", JOptionPane.INFORMATION_MESSAGE)
                }
            }
        }

        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }

    private fun validateInput(field: JTextField, row: Int, col: Int) {
        try {
            val value = field.text.toInt()
            if (value in 1..9) {
                controller.handleCellInput(row, col, value)
            } else {
                throw NumberFormatException()
            }
        } catch (e: NumberFormatException) {
            field.text = ""
            JOptionPane.showMessageDialog(this, "Nur Zahlen von 1 bis 9 sind erlaubt.", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE)
        }
    }

    fun updateBoard(board: Array<Array<Int?>>) {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val field = boardFields[i][j]
                val value = board[i][j]
                if (value != null) {
                    field.text = value.toString()
                    field.isEditable = false
                    field.background = Color(211, 211, 211)
                } else {
                    field.text = ""
                    field.isEditable = true
                    field.background = Color.WHITE
                }
            }
        }
    }

    fun displayCheckResult(isCorrect: Boolean) {
        val message = if (isCorrect) {
            "Das Board ist korrekt! Du hast gewonnen!"
        } else {
            "Es gibt Fehler auf dem Board!"
        }
        JOptionPane.showMessageDialog(this, message)
    }

    fun displayError(message: String) {
        JOptionPane.showMessageDialog(this, message, "Fehler", JOptionPane.ERROR_MESSAGE)
    }

    fun highlightCorrectCell(row: Int, col: Int) {
        val field = boardFields[row][col]
        field.background = Color(144, 238, 144)
    }

    fun highlightIncorrectCell(row: Int, col: Int) {
        val field = boardFields[row][col]
        field.background = Color.RED
    }

    fun highlightSolvedBoard(board: Array<Array<Int?>>) {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val field = boardFields[i][j]
                val value = board[i][j]
                if (value != null) {
                    field.background = Color(144, 238, 144)
                }
            }
        }
    }

    fun highlightHintCell(row: Int, col: Int) {
        val field = boardFields[row][col]
        field.background = Color.YELLOW
    }

    private fun resetBoardColors() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val field = boardFields[i][j]
                if (field.isEditable) {
                    field.background = Color.WHITE
                } else {
                    field.background = Color(211, 211, 211)
                }
            }
        }
    }
}
