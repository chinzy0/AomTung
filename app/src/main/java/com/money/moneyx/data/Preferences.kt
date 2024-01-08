package com.money.moneyx.data

import android.content.Context
import android.content.SharedPreferences

class Preference (private var context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private var instancePreferences: Preference? = null

        fun getInstance(context: Context): Preference {
            if (instancePreferences == null) {
                instancePreferences = Preference(context.applicationContext)
            }
            return instancePreferences!!
        }
    }
}