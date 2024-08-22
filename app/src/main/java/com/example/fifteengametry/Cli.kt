package com.example.fifteengametry

import com.example.fifteengametry.FifteenEngine.Companion.EMPTY
import com.example.fifteengametry.FifteenEngine.Companion.ix


// MVC - Model View Controller

// Controller - dialog with user
// View - print board
// Model - 1) state (data) 2) logic (service)


// MODEL: ENGINE (Service)
val engine: FifteenEngine = FifteenEngine
// MODEL: STATE
var state = engine.getInitialState()

// CONTROLLER
fun main() {
//    val engine: FifteenEngine = FifteenEngine
    println("Welcome to Fifteen Game!")
//    var state = engine.getInitialState()
    while (!engine.isWin(state)) {
        printBoard(state)
        val cell: Byte = readCell()
        state = engine.transitionState(state, cell)
    }
    printBoard(state)
    println("Victory!")
}

fun readCell(
    println: (String) -> Unit = ::println,
    readln: () -> String = ::readln
): Byte {
    while (true) {
        println("Enter cell to move (1..15):")
        val res = readln().toIntOrNull()
        if (res in 1..15) return res!!.toByte()
    }
}

// VIEW
fun printBoard(
    state: ByteArray,
    printer: (String) -> Unit = ::print
) {
    printer("-".repeat(18))
    printer("\n")
    for (iRow in 0..<DIM) {
        printer("|")
        for (iCol in 0..<DIM) {
            printer(formatCell(state[ix(iRow, iCol)]))
        }
        printer("|\n")
    }
    printer("------------------\n")
}

fun formatCell(cell: Byte): String {
    return "%3s ".format(if (cell == EMPTY) " " else cell)
}