package com.graphicless.newsapp.utils

import android.widget.Toast
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {


    fun internetUnavailableToast(){
        Toast.makeText(MyApplication.instance, AppConstants.NO_INTERNET_MESSAGE, Toast.LENGTH_SHORT).show()
    }

    fun bookmarkAdded(){
        Toast.makeText(MyApplication.instance, "News added to favorites", Toast.LENGTH_SHORT).show()
    }

    fun bookmarkRemoved(){
        Toast.makeText(MyApplication.instance, "News removed from favorites", Toast.LENGTH_SHORT).show()
    }

    fun DateToTimeFormat(oldstringDate: String?): String? {
        val p = PrettyTime(Locale(getCountry()))
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH
            )
            val date: Date? = oldstringDate?.let { sdf.parse(it) }
            isTime = p.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime
    }

    private fun getCountry(): String {
        val locale: Locale = Locale.getDefault()
        val country: String = java.lang.String.valueOf(locale.getCountry())
        return country.lowercase(Locale.getDefault())
    }
}