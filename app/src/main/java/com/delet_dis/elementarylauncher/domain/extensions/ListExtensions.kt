package com.delet_dis.elementarylauncher.domain.extensions

fun <T> concatenate(vararg lists: List<T>): List<T> =
    listOf(*lists).flatten()
