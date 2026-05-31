package br.com.gtel.ensaios.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GTELBlue = Color(0xFF003B71)
val GTELOrange = Color(0xFFF58220)
val GTELBackground = Color(0xFFF7F9FC)

private val GTELColorScheme = lightColorScheme(
    primary = GTELBlue,
    secondary = GTELOrange,
    background = GTELBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1B1B1B),
    onSurface = Color(0xFF1B1B1B)
)

@Composable
fun GTELTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GTELColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
