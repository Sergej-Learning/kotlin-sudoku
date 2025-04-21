# Sudoku-X

Eine Variation des klassischen Sudoku-Spieles.Die Standardregeln werden erweitert.
(jede Zahl 1-9 darf in einer Zeile,Spalte und 3x3 Block nur einmal vorkommen).
Die Hauptdiagonalen bilden die Zusatzbedingung, da darf ebenfalls eine Zahl nur einmal vorkommen, pro Diagonale.

# Projektstruktur

Gebaut basierend auf der MVC-Architektur(Model-View-Controller).Für die grafische Benutzeroberläche wird `SWING` benutzt
die Programmiersprache is Kotlin.

# Verzeichnis
bash 
```
src/
│
├── main/
│   ├── kotlin/
│   │   └── com/
│   │       └── sudoku/
│   │           ├── controller/
│   │           │   └── SudokuController.kt
│   │           ├── model/
│   │           │   └── SudokuGame.kt
│   │           └── view/
│   │               └── SudokuView
│   │           
│   └── Main.kt
│
└── resources/
├── Savefile.json
└── Sudoku.MD
```

# Spielregeln:

1. Jede Zahl von 1 bis 9 darf in jeder Zeile,Spalte,und 3x3-Block nur einmal vorkommen
2. Zusätzlich darf jede Zahl nur einmal in den Hauptdiagonalen vorkommen.

# Schaltflächen

New Game - Generiert ein neues Sudoku-Board mit 22 zufällig RICHTIG platzierten Zahlen.
Solve - Löst das Sudoku und füllt die restlichen Felder aus.
Check Board - Prüft das aktuelle Board und zeigt an, ob es korrekt ist. (Siegbedingung)
Save - Speichert den aktuellen Spielstand.
Load - Lädt einen gespeicherten Spielstand.
Hint - Füllt eine zufällige Zelle mit der richtigen Zahl. Limitiert auf 5!

# Benutzeroberfläche

- Die Hauptdiagonalen des Sudoku-Rasters sind visuell hervorgehoben durch blaue Umrandung.
- Vorbelegte Zahlfelder sind NICHT editierbar und haben graue Hintergrundfarbe.
- Rest des Boards ist in weiß gehalten und erlaubt Eingabe von Zahlen zwischen 1 und 9.

# Übersicht der Hauptkomponenten und Methoden

Das Projekt besteht aus vier Hauptkomponenten:

1. `Main.kt`
2. `SudokuController.kt`
3. `SudokuView.kt`
4. `SudokuGame.kt`

Zusätzlich gibt es noch weitere Dateien:

1. `Savefile.json`
2. `Sudoku.MD`

## Komponenten und Methoden

### Main.kt

Dies ist der Einstiegspunkt der Anwendung. Es initialisiert den `SudokuController` und die `SudokuView` und richtet deren Interaktion ein.

- **main()**: Die Hauptfunktion, die Instanzen von `SudokuController` und `SudokuView` erstellt und deren Interaktion einrichtet.

### SudokuController.kt

Diese Klasse fungiert als Controller im MVC-Muster. Sie verarbeitet Benutzereingaben und aktualisiert die Ansicht entsprechend.

- **setView(view: SudokuView)**: Setzt die Ansicht für den Controller.
- **startNewGame()**: Startet ein neues Spiel, indem ein neues Board generiert und die Ansicht aktualisiert wird.
- **solveGame()**: Löst das aktuelle Spiel und aktualisiert die Ansicht.
- **checkBoard()**: Überprüft, ob das aktuelle Board korrekt ist, und aktualisiert die Ansicht mit dem Ergebnis.
- **handleCellInput(row: Int, col: Int, value: Int?)**: Verarbeitet Benutzereingaben für eine bestimmte Zelle.
- **saveGame()**: Speichert den aktuellen Spielstand in einer Datei.
- **loadGame()**: Lädt einen gespeicherten Spielstand aus einer Datei.
- **giveHint()**: Gibt einen Hinweis, indem eine leere Zelle mit dem richtigen Wert gefüllt wird.
- **hasEmptyCell()**: Überprüft, ob es leere Zellen auf dem Board gibt.

### SudokuView.kt

Diese Klasse fungiert als Ansicht im MVC-Muster. Sie verarbeitet die Benutzeroberflächenelemente und zeigt das Spielbrett an.

- **init**: Initialisiert die Ansicht, richtet UI-Komponenten ein und fügt Ereignislistener hinzu.
- **validateInput(field: JTextField, row: Int, col: Int)**: Validiert Benutzereingaben für eine bestimmte Zelle.
- **updateBoard(board: Array<Array<Int?>>)**: Aktualisiert die Ansicht mit dem aktuellen Zustand des Boards.
- **displayCheckResult(isCorrect: Boolean)**: Zeigt das Ergebnis der Überprüfung des Boards an.
- **displayError(message: String)**: Zeigt eine Fehlermeldung an.
- **highlightCorrectCell(row: Int, col: Int)**: Hebt eine Zelle als korrekt hervor.
- **highlightIncorrectCell(row: Int, col: Int)**: Hebt eine Zelle als falsch hervor.
- **highlightSolvedBoard(board: Array<Array<Int?>>)**: Hebt alle Zellen als korrekt hervor, wenn das Board gelöst ist.
- **highlightHintCell(row: Int, col: Int)**: Hebt eine Zelle hervor, die durch einen Hinweis gefüllt wurde.
- **resetBoardColors()**: Setzt die Farben aller Zellen auf ihren Standardzustand zurück.

### SudokuGame.kt

Diese Klasse repräsentiert die Spiellogik. Sie verarbeitet das Generieren, Lösen und Validieren des Sudoku-Boards.

- **generateNewBoard()**: Generiert ein neues Sudoku-Board.
- **resetBoard()**: Setzt das Board in einen leeren Zustand zurück.
- **fillBoard()**: Füllt das Board mit Zahlen, um ein gültiges Sudoku-Puzzle zu erstellen.
- **fillRemaining(row: Int, col: Int)**: Füllt rekursiv die verbleibenden Zellen auf dem Board.
- **isSafeToPlace(row: Int, col: Int, num: Int)**: Überprüft, ob es sicher ist, eine Zahl in eine bestimmte Zelle zu setzen.
- **isInRow(row: Int, num: Int)**: Überprüft, ob eine Zahl bereits in einer bestimmten Zeile vorhanden ist.
- **isInCol(col: Int, num: Int)**: Überprüft, ob eine Zahl bereits in einer bestimmten Spalte vorhanden ist.
- **isInBox(row: Int, col: Int, num: Int)**: Überprüft, ob eine Zahl bereits in einem bestimmten 3x3-Feld vorhanden ist.
- **isInDiagonal(row: Int, col: Int, num: Int)**: Überprüft, ob eine Zahl bereits in einer der Hauptdiagonalen vorhanden ist.
- **saveSolution()**: Speichert die Lösung des generierten Boards.
- **removeNumbers()**: Entfernt Zahlen vom Board, um ein Puzzle mit einer eindeutigen Lösung zu erstellen.
- **hasUniqueSolution()**: Überprüft, ob das Board eine eindeutige Lösung hat.
- **getBoard()**: Gibt den aktuellen Zustand des Boards zurück.
- **setCell(row: Int, col: Int, value: Int)**: Setzt einen Wert in eine bestimmte Zelle auf dem Board.
- **checkBoard()**: Überprüft, ob der aktuelle Zustand des Boards korrekt ist.
- **isValidMove(row: Int, col: Int, value: Int)**: Überprüft, ob das Setzen eines Wertes an (row, col) ein gültiger Zug ist.
- **solve()**: Löst das aktuelle Sudoku-Puzzle.
- **solveBoard(row: Int, col: Int)**: Löst das Sudoku-Puzzle rekursiv.
- **save()**: Speichert den aktuellen Spielstand in einer JSON-Datei.
- **load()**: Lädt den Spielstand aus einer JSON-Datei.
- **giveHint()**: Gibt einen Hinweis, indem eine leere Zelle mit dem richtigen Wert gefüllt wird.
- **hasEmptyCell()**: Überprüft, ob es leere Zellen auf dem Board gibt.
