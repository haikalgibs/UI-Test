package org.d3if1059.mobpro2.helloworld.widget

import android.content.SharedPreferences
import org.d3if1059.mobpro2.helloworld.model.Harian

object PrefUtils {
    const val KEY_DATE = "date"
    const val KEY_DATA = "data"
    fun saveData(sharedPref: SharedPreferences, data: Harian) {
        with(sharedPref.edit()) {
            putLong(KEY_DATE, data.key)
            putInt(KEY_DATA, data.jumlahPositif.value)
            apply()
        }
    }
}