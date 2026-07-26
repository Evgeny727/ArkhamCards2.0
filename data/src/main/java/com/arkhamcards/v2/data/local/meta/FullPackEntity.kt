package com.arkhamcards.v2.data.local.meta

data class FullPackEntity(
    val code: String,
    val name: String,
    val cycleName: String,
    val position: Int,
    val cyclePosition: Int,
    val official: Boolean,
    val reprint: Boolean,
    val chapter: Int?
)
