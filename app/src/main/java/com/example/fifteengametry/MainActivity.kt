package com.example.fifteengametry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val DIM = 4

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF673AB7)
            ) {
                FifteenGameApp()
            }
        }
    }
}

@Composable
fun FifteenGameApp(engine: FifteenEngine = FifteenEngine) {
    var state by rememberSaveable { mutableStateOf(engine.getInitialState()) }
    var moveCount by rememberSaveable { mutableStateOf(0) }

    fun onReset() {
        state = engine.getInitialState()
        moveCount = 0
    }

    fun onCellClick(cellValue: Byte) {
        val newState = engine.transitionState(state, cellValue)
        if (newState != state) {
            state = newState
            moveCount++
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            FifteenGrid(
                state,
                engine.isWin(state),
                FifteenEngine::formatCell,
                onClick = ::onCellClick
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Button(
                onClick = { /* Do nothing, just show the move count */ },
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = "Moves: $moveCount",
                    color = Color.Black)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                onClick = ::onReset,
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = "Restart",
                    color = Color.Black)
            }
        }
    }
}




@Composable
fun FifteenGrid(
    state: ByteArray,
    isVictory: Boolean = false,
    formatText: (Byte) -> String = { it.toString() },
    onClick: (Byte) -> Unit = {}
) {
    fun ix(iRow: Int, iCol: Int) = iRow * DIM + iCol

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.pole),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (iRow in 0 until DIM) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (iCol in 0 until DIM) {
                        val id = ix(iRow, iCol)
                        Cell(
                            formatText(state[id]),
                            onClick = { onClick(state[id]) }
                        )
                    }
                }
            }
        }
        if (isVictory) {
            VictoryBanner(
                Modifier.align(Alignment.Center),
                onReset = {}
            )
        }
    }
}

@Composable
fun VictoryBanner(
    modifier: Modifier = Modifier,
    onReset: () -> Unit = {}
) {
    Text(
        "VICTORY!",
        modifier = Modifier
            .clickable(onClick = onReset)
            .border(width = 2.dp, color = Color.Blue)
            .background(
                brush = Brush.verticalGradient(
                    0f to Color.Blue.copy(alpha = 0.95f),
                    0.5f to Color.Blue.copy(alpha = 0.2f),
                    0.5f to Color.Yellow.copy(alpha = 0.2f),
                    1f to Color.Yellow.copy(alpha = 0.95f)
                )
            )
            .padding(
                vertical = 150.dp,
                horizontal = 50.dp
            )
            .then(modifier),
        style = TextStyle(
            color = Color.Blue,
            fontSize = 60.sp,
            fontWeight = FontWeight.W900
        )
    )
}

@Composable
fun Cell(number: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val isVisible = number.isNotBlank()
    FilledTonalButton(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .size(90.dp)
            .alpha(if (isVisible) 1f else 0f)
            .padding(2.dp)
            .then(modifier),
        shape = MaterialTheme.shapes.small
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.fishka),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                number,
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Cursive,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun GridPreview() {
    FifteenGrid(
        byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16),
        true, FifteenEngine::formatCell
    )
}

@Preview(
    showSystemUi = true, showBackground = true,
    backgroundColor = 0xFF673AB7
)
@Composable
fun GridPreview2() {
    FifteenGameApp(
        engine = object : FifteenEngine by FifteenEngine.Companion {
            override fun getInitialState() = byteArrayOf(
                1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 16, 15
            )
        }
    )
}
