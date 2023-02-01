package com.graphicless.newsapp.utils

import androidx.room.TypeConverter
import com.graphicless.newsapp.model.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name.toString()
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}