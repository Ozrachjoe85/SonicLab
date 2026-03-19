// RetroComponents.kt - FIXED CODE EXAMPLES

// ============================================
// FIX 1: Lines around 294-295
// ============================================

// BEFORE (BROKEN):
fun RetroButton(
    modifier: Modifier = Modifier,
    size: IntSize = IntSize(100, 40)
) {
    Box(
        modifier = modifier
            .width(size.width)      // ❌ ERROR: Function invocation expected
            .height(size.height)    // ❌ ERROR: Function invocation expected
    )
}

// AFTER (FIXED):
fun RetroButton(
    modifier: Modifier = Modifier,
    size: IntSize = IntSize(100, 40)
) {
    Box(
        modifier = modifier
            .width(size.width.dp)   // ✅ CORRECT
            .height(size.height.dp) // ✅ CORRECT
    )
}


// ============================================
// FIX 2: Lines around 496-498
// ============================================

// BEFORE (BROKEN):
@Composable
fun RetroIcon(
    iconSize: Int = 24,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = "Play",
        modifier = modifier
            .width(iconSize)    // ❌ ERROR: Function invocation expected
            .height(iconSize)   // ❌ ERROR: Function invocation expected
            .width(60)          // ❌ ERROR: Function invocation expected
    )
}

// AFTER (FIXED):
@Composable
fun RetroIcon(
    iconSize: Int = 24,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = "Play",
        modifier = modifier
            .width(iconSize.dp)     // ✅ CORRECT
            .height(iconSize.dp)    // ✅ CORRECT
            .size(60.dp)            // ✅ CORRECT (or .width(60.dp) if intentional)
    )
}


// ============================================
// FIX 3: Lines around 523-524 (Circle calculation)
// ============================================

// BEFORE (BROKEN):
Canvas(modifier = Modifier.size(200.dp)) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width / 3
    
    for (i in 0..11) {
        val angle = i * 30.0 * PI / 180.0
        val x = centerX + cos(angle) * radius  // ❌ ERROR: Overload resolution ambiguity
        val y = centerY + sin(angle) * radius  // ❌ ERROR: Overload resolution ambiguity
        
        drawCircle(
            color = Color.Cyan,
            radius = 10f,
            center = Offset(x, y)
        )
    }
}

// AFTER (FIXED):
Canvas(modifier = Modifier.size(200.dp)) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.width / 3
    
    for (i in 0..11) {
        val angle = i * 30.0 * PI / 180.0
        val x = centerX + cos(angle).toFloat() * radius  // ✅ CORRECT: explicit Float conversion
        val y = centerY + sin(angle).toFloat() * radius  // ✅ CORRECT: explicit Float conversion
        
        drawCircle(
            color = Color.Cyan,
            radius = 10f,
            center = Offset(x, y)
        )
    }
}


// ============================================
// COMMON PATTERNS TO FIX
// ============================================

// Pattern 1: Numeric values need .dp
// ❌ .width(100)
// ✅ .width(100.dp)

// Pattern 2: Variables need .dp
// ❌ .width(myWidth)
// ✅ .width(myWidth.dp)

// Pattern 3: Math operations with trigonometric functions
// ❌ val x = cos(angle) * value
// ✅ val x = cos(angle).toFloat() * value

// Pattern 4: When multiplying Double * Float
// ❌ val result = doubleValue * floatValue
// ✅ val result = (doubleValue * floatValue).toFloat()
// OR: val result = doubleValue.toFloat() * floatValue


// ============================================
// SEARCH & REPLACE PATTERNS
// ============================================

/*
In RetroComponents.kt, search for:

1. \.width\((\d+)\)
   Replace with: .width($1.dp)

2. \.height\((\d+)\)
   Replace with: .height($1.dp)

3. \.width\(([a-zA-Z]\w+)\)
   Replace with: .width($1.dp)
   
4. \.height\(([a-zA-Z]\w+)\)
   Replace with: .height($1.dp)

5. cos\(([^)]+)\) \* 
   Replace with: cos($1).toFloat() * 

6. sin\(([^)]+)\) \*
   Replace with: sin($1).toFloat() * 
*/
