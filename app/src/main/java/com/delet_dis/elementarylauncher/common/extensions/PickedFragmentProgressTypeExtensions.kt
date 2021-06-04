package com.delet_dis.elementarylauncher.common.extensions

import com.delet_dis.elementarylauncher.common.models.PickedFragmentProgressType

fun findPickedFragmentProgress(searchingFragmentId: Int): PickedFragmentProgressType? {
    return PickedFragmentProgressType.values().find { pickedFragmentProgressType ->
        pickedFragmentProgressType.fragmentId == searchingFragmentId
    }
}