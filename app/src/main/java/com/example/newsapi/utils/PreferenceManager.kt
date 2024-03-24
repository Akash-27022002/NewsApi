package com.example.newsapi.utils

import android.content.Context
import android.preference.PreferenceManager

/**
 *  This [PreferenceManager] will help us to manage the preferences for the Theme
 *  it have to functions that can set and get the preference
 *  [getThemeMode] it will provide you the current theme mode
 *  [setThemeMode] it will set what should be the current theme from now onwards
 * */

object PreferenceManager {
    private const val KEY_THEME_MODE = "theme_mode"

    fun getThemeMode(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getBoolean(KEY_THEME_MODE, false) // Default to false if key not found
    }

    fun setThemeMode(context: Context, isDarkModeEnabled: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putBoolean(KEY_THEME_MODE, isDarkModeEnabled).apply()
    }
}
