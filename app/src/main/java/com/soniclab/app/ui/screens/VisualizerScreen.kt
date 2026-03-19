// VisualizerScreen.kt - FIXED CODE EXAMPLES

// ============================================
// FIX 1: Line around 245 - Duplicate size() calls
// ============================================

// BEFORE (BROKEN):
Box(
    modifier = Modifier
        .size(100).size(100)  // ❌ ERROR: Function invocation expected
        .background(Color.Blue)
)

// AFTER (FIXED):
Box(
    modifier = Modifier
        .size(100.dp)         // ✅ CORRECT: Single size call with .dp
        .background(Color.Blue)
)


// ============================================
// FIX 2: Lines around 298-304 - Double to Float conversions
// ============================================

// BEFORE (BROKEN):
Canvas(modifier = Modifier.fillMaxSize()) {
    val maxRadius = size.minDimension / 2
    val waveWidth = size.width / 10
    
    audioData.forEachIndexed { index, value ->
        val angle = (index / audioData.size.toFloat()) * 2 * PI
        
        // Drawing circle
        drawCircle(
            color = Color.Cyan,
            radius = value * maxRadius,  // ❌ ERROR: Type mismatch (Double * Float)
            center = Offset(
                center.x + value * waveWidth,  // ❌ ERROR: Type mismatch
                center.y
            )
        )
    }
}

// AFTER (FIXED):
Canvas(modifier = Modifier.fillMaxSize()) {
    val maxRadius = size.minDimension / 2
    val waveWidth = size.width / 10
    
    audioData.forEachIndexed { index, value ->
        val angle = (index / audioData.size.toFloat()) * 2 * PI
        
        // Drawing circle
        drawCircle(
            color = Color.Cyan,
            radius = (value * maxRadius).toFloat(),  // ✅ CORRECT: Explicit Float conversion
            center = Offset(
                center.x + (value * waveWidth).toFloat(),  // ✅ CORRECT
                center.y
            )
        )
    }
}


// ============================================
// COMPLETE EXAMPLE: Waveform Visualization
// ============================================

@Composable
fun WaveformVisualization(
    audioData: FloatArray,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val centerY = size.height / 2
        val width = size.width
        val barWidth = width / audioData.size
        
        audioData.forEachIndexed { index, amplitude ->
            // Calculate bar height
            val barHeight = (amplitude * size.height / 2).toFloat()  // ✅ Convert Double to Float
            
            // Calculate x position
            val x = (index * barWidth).toFloat()  // ✅ Convert to Float
            
            // Draw bar
            drawRect(
                color = Color.Cyan,
                topLeft = Offset(x, centerY - barHeight / 2),
                size = Size(barWidth.toFloat(), barHeight)
            )
        }
    }
}


// ============================================
// COMPLETE EXAMPLE: Circular Visualizer
// ============================================

@Composable
fun CircularVisualizer(
    audioData: FloatArray,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(300.dp)) {  // ✅ Correct: size with .dp
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 3
        
        audioData.forEachIndexed { index, value ->
            val angle = (index.toFloat() / audioData.size) * 2 * PI.toFloat()
            
            // Calculate position
            val radius = maxRadius + (value * maxRadius).toFloat()  // ✅ Explicit conversion
            val x = center.x + cos(angle.toDouble()).toFloat() * radius  // ✅ Both conversions
            val y = center.y + sin(angle.toDouble()).toFloat() * radius  // ✅ Both conversions
            
            // Draw point
            drawCircle(
                color = Color.Cyan,
                radius = 5f,
                center = Offset(x, y)
            )
        }
    }
}


// ============================================
// IMPORT STATEMENTS NEEDED
// ============================================

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


// ============================================
// TYPE CONVERSION RULES
// ============================================

/*
1. When multiplying Double * Float:
   ❌ val result = doubleValue * floatValue
   ✅ val result = (doubleValue * floatValue).toFloat()
   ✅ val result = doubleValue.toFloat() * floatValue

2. When using trigonometric functions (cos, sin) in Float context:
   ❌ val x = cos(angle) * radius
   ✅ val x = cos(angle).toFloat() * radius
   ✅ val x = cos(angle.toDouble()).toFloat() * radius  // If angle is already Float

3. When using size or dimension calculations:
   ❌ .size(100)
   ✅ .size(100.dp)
   
   ❌ .size(mySize)
   ✅ .size(mySize.dp)

4. Canvas drawing coordinates must be Float:
   ❌ drawCircle(radius = value * maxRadius, ...)
   ✅ drawCircle(radius = (value * maxRadius).toFloat(), ...)
*/


// ============================================
// COMMON ERRORS AND FIXES
// ============================================

// Error: "Type mismatch: inferred type is Double but Float was expected"
// Fix: Add .toFloat() to the expression
val radius = (amplitude * maxValue).toFloat()

// Error: "Function invocation 'size(...)' expected"
// Fix: Add .dp to numeric size values
.size(100.dp)

// Error: "Overload resolution ambiguity"
// Fix: Explicitly convert one operand to match the other's type
val x = cos(angle).toFloat() * radius
