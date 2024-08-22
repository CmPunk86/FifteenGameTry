package com.example.fifteengametry

import kotlin.math.abs

interface FifteenEngine {
    fun transitionState(oldState: ByteArray, cell: Byte): ByteArray
    fun isWin(state: ByteArray): Boolean
    fun getInitialState(): ByteArray

    companion object : FifteenEngine {
        const val EMPTY: Byte = 16
        const val DIM = 4
        internal val FINAL_STATE = ByteArray(16) { (it + 1).toByte() }

        fun row(ix: Int) = ix / DIM
        fun col(ix: Int) = ix % DIM
        fun ix(row: Int, col: Int) = row * DIM + col
        fun formatCell(cell: Byte) = "%s".format(if (cell == EMPTY) " " else cell)

        override fun transitionState(oldState: ByteArray, cell: Byte): ByteArray {
            val ixCell = oldState.indexOf(cell)
            val ixEmpty = oldState.indexOf(EMPTY)

            return if (areAdjacent(ixCell, ixEmpty))
                withSwapped(oldState, ixCell, ixEmpty)
            else oldState
        }

        internal fun withSwapped(arr: ByteArray, ix1: Int, ix2: Int): ByteArray {
            if (ix1 == ix2) return arr
            val res = arr.clone()
            res[ix1] = res[ix2].also { res[ix2] = res[ix1] }
            return res
        }

        internal fun areAdjacent(ix1: Int, ix2: Int): Boolean {
            val row1 = row(ix1)
            val col1 = col(ix1)
            val row2 = row(ix2)
            val col2 = col(ix2)
            return (row1 == row2 && abs(col1 - col2) == 1 ||
                    col1 == col2 && abs(row1 - row2) == 1)
        }

        override fun isWin(state: ByteArray): Boolean =
            state.contentEquals(FINAL_STATE)

        private fun countInversions(state: ByteArray): Int {
            val rowOfEmptyCell = row(state.indexOf(EMPTY))
            var inversions = rowOfEmptyCell
            repeat(state.size) {
                if (state[it] != EMPTY)
                    for (j in it + 1..<state.size) {
                        if (state[j] != EMPTY && state[it] > state[j]) inversions++
                    }
            }
            return inversions
        }

        private fun isFeasibleSolution(state: ByteArray): Boolean = countInversions(state) % 2 == 1

        override fun getInitialState(): ByteArray {
            val res = FINAL_STATE.clone()
            do {
                res.shuffle()
            } while (!isFeasibleSolution(res))
            return res
        }
    }
}