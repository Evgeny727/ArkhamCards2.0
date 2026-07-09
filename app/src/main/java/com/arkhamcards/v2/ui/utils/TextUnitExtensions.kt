package com.arkhamcards.v2.ui.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

fun Int.appSp(scaleFactor: Float): TextUnit = (this * scaleFactor).sp