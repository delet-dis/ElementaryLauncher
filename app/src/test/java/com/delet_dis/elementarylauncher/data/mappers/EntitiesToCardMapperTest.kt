package com.delet_dis.elementarylauncher.data.mappers

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Test

class EntitiesToCardMapperTest {
    @Test
    fun staleShortcutUsesPlaceholder() {
        assertEquals(
            "App unavailable",
            displayNameOrPlaceholder(null, "App unavailable")
        )
    }

    @Test
    fun directCallRequiresBothPermissionAndCallingSupport() {
        assertEquals(Intent.ACTION_CALL, chooseCallAction(true, true))
        assertEquals(Intent.ACTION_DIAL, chooseCallAction(false, true))
        assertEquals(Intent.ACTION_DIAL, chooseCallAction(true, false))
    }
}
