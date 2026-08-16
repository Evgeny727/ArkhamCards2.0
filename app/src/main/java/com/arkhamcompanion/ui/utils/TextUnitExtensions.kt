package com.arkhamcompanion.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun Int.appSp(scaleFactor: Float): TextUnit = (this * scaleFactor).sp

@ReadOnlyComposable
@Composable
fun Dp.scaledByFont(scaleFactor: Float): Dp = this * scaleFactor * LocalDensity.current.fontScale