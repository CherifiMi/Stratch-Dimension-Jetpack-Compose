import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlin.math.sqrt

val position = mutableStateOf(Offset(0f, 0f))
val animated = mutableStateOf(-30f)
val scroll = mutableStateOf(0f)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun Balls() {

    println("x-> ${position.value.x}  y-> ${position.value.y}    a-> ${animated.value}  s-> ${scroll.value}")

    val isOpaque = remember { mutableStateOf(value = true) }

    val splash: Float by animateFloatAsState(
        targetValue = if (isOpaque.value) -60f-scroll.value else 350f+scroll.value,
        animationSpec = tween(
            durationMillis = 1000, // animation duration
            easing = EaseOutBounce // animation easing
        )
    )

    animated.value = splash

    val size = 30.dp

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .fillMaxSize()
            .offset((-10).dp, (-10).dp)
            .background(Color(0xffF9F871))
            .onPointerEvent(PointerEventType.Move) {
                position.value = it.changes.first().position
            }
            .onPointerEvent(PointerEventType.Scroll) {
                scroll.value += it.changes.first().scrollDelta.y * 20
            }
            .mouseClickable(
                onClick = {
                    isOpaque.value = !isOpaque.value
                }
            )
    ) {
        for (i in 0..(600 / size.value.toInt())) {
            for (j in 0..(600 / size.value.toInt())) {
                Box(
                    modifier = Modifier
                        .size(size.doStuff(i, j))
                        .offset((size - 10.dp) * i, (size - 10.dp) * j)
                        .border(3.dp, Color.Black, RoundedCornerShape(100.dp))
                        .background(Color(0xffFC5E63), shape = RoundedCornerShape(100.dp))
                )
            }
        }
    }
}

val EaseOutBounce: Easing = Easing { fraction ->
    val n1 = 7.5625f
    val d1 = 2.75f
    var newFraction = fraction

    return@Easing if (newFraction < 1f / d1) {
        n1 * newFraction * newFraction
    } else if (newFraction < 2f / d1) {
        newFraction -= 1.5f / d1
        n1 * newFraction * newFraction + 0.75f
    } else if (newFraction < 2.5f / d1) {
        newFraction -= 2.25f / d1
        n1 * newFraction * newFraction + 0.9375f
    } else {
        newFraction -= 2.625f / d1
        n1 * newFraction * newFraction + 0.984375f
    }
}

private fun Dp.doStuff(i: Int, j: Int): Dp {

    val x1 = i * 30
    val y1 = j * 30
    val x2 = position.value.x
    val y2 = position.value.y

    //val d = sqrt((((x2-x1.toFloat()).pow(2)+(y2 -y1.toFloat()).pow(2)).toDouble())) //√((x2 – x1)² + (y2 – y1)²)
    val d = sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1))

    val x = d * 100f / (300f + animated.value)

    return (x * (this.value / 100f)).dp
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 400.dp, height = 400.dp),
        resizable = false
    ) {

        Balls()
        //MainContent()
    }

}

@Composable
fun MainContent() {
    val isOpaque = remember { mutableStateOf(value = true) }

    val alpha: Float by animateFloatAsState(
        targetValue = if (isOpaque.value) 1f else 0.2f,
        animationSpec = tween(
            durationMillis = 3000, // animation duration
            easing = FastOutSlowInEasing // animation easing
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8DC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { isOpaque.value = !isOpaque.value },
            colors = ButtonDefaults.buttonColors(
                Color(0xFF9E1B32), Color(0xCCFFFFFF)
            )
        ) {
            Text(text = "Animate Opacity")
        }

        Icon(
            Icons.Filled.Favorite,
            "Localized description",
            tint = Color(0xFFE30022),
            modifier = Modifier
                .size(300.dp)
                .alpha(alpha = alpha)
        )
    }
}