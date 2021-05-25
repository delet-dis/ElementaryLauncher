package com.delet_dis.elementarylauncher.common.extensions

fun <T> concatenate(vararg lists: List<T>): List<T> {
    return listOf(*lists).flatten()
}