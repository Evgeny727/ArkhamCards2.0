package com.arkhamcards.v2.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class CustomShape(
    val tiny: RoundedCornerShape = RoundedCornerShape(2.dp),
    val small: RoundedCornerShape = RoundedCornerShape(4.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(6.dp),
    val large: RoundedCornerShape = RoundedCornerShape(8.dp),
    val circle: RoundedCornerShape = RoundedCornerShape(50.dp),
)

val LocalCustomShapes = staticCompositionLocalOf { CustomShape() }