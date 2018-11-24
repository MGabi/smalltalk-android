package com.example.smalltalkAndroid.utils

import android.os.Bundle
import android.os.Parcelable
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

sealed class BundleDelegate<T>(protected val key: String) : ReadWriteProperty<Bundle, T> {

    class ParcelableList<U : android.os.Parcelable>(key: kotlin.String) : BundleDelegate<List<U>>(key) {

        override fun getValue(thisRef: Bundle, property: KProperty<*>) = thisRef.getParcelableArrayList(key) ?: emptyList<U>()
        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: List<U>) = thisRef.putParcelableArrayList(key, value as ArrayList<out Parcelable>)
    }
}