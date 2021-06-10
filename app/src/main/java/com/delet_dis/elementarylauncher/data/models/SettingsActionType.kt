package com.delet_dis.elementarylauncher.data.models

import android.provider.Settings
import com.delet_dis.elementarylauncher.R

enum class SettingsActionType(val stringId: Int, val imageId: Int, val action: String) {
    WIFI(
        R.string.settingsPickingWifiText,
        R.drawable.ic_wifi,
        Settings.ACTION_WIFI_SETTINGS
    ),
    MOBILE_DATA(
        R.string.settingsPickingMobileDataText,
        R.drawable.ic_mobile_data,
        Settings.ACTION_NETWORK_OPERATOR_SETTINGS
    ),
    BLUETOOTH(
        R.string.settingsPickingBluetoothText,
        R.drawable.ic_bluetooth,
        Settings.ACTION_BLUETOOTH_SETTINGS
    ),
    GEOLOCATION(
        R.string.settingsPickingGeolocationText,
        R.drawable.ic_geolocation,
        Settings.ACTION_LOCATION_SOURCE_SETTINGS
    ),
    BATTERY_SAVER(
        R.string.settingsPickingBatterySaverText,
        R.drawable.ic_battery_saver,
        Settings.ACTION_BATTERY_SAVER_SETTINGS
    ),
    AIRPLANE_MODE(
        R.string.settingsPickingAirplaneText,
        R.drawable.ic_airplane,
        Settings.ACTION_AIRPLANE_MODE_SETTINGS
    ),
    STORAGE(
        R.string.settingsPickingStorageText,
        R.drawable.ic_storage,
        Settings.ACTION_INTERNAL_STORAGE_SETTINGS
    )
}