package com.arkhamcards.v2.domain.model.meta

data class Pack(
    val code: String,
    val name: String,
    val cycleName: String,
    val position: Int,
    val cyclePosition: Int,
    val official: Boolean,
    val reprint: Boolean,
    val chapter: Int?,
)
