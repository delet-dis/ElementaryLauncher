package com.delet_dis.elementarylauncher.domain.extensions

import com.delet_dis.elementarylauncher.data.models.PickedFragmentProgressType

fun findPickedFragmentProgress(searchingFragmentId: Int): PickedFragmentProgressType? {
    return PickedFragmentProgressType.values().find { pickedFragmentProgressType ->
        pickedFragmentProgressType.fragmentId == searchingFragmentId
    }
}