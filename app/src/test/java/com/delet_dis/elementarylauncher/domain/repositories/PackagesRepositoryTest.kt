package com.delet_dis.elementarylauncher.domain.repositories

import org.junit.Assert.assertEquals
import org.junit.Test

class PackagesRepositoryTest {
    @Test
    fun packageEnumerationDeduplicatesPackagesWithoutChangingFirstMatchOrder() {
        val resolvedPackages = listOf("camera", "maps", "camera", "phone", "maps")

        assertEquals(
            listOf("camera", "maps", "phone"),
            resolvedPackages.deduplicateByPackage { it }
        )
    }
}
