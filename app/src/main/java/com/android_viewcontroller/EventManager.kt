package com.android_viewcontroller

import java.util.*
import kotlin.collections.HashMap

object EventManager {

    private var observers: MutableMap<Any, MutableMap<String, ObserverWrapper>> = WeakHashMap()

    fun addObserver(observer: Any, name: String, callback: (Any?) -> Unit) {
        var map = observers.get(observer)
        if (map == null) {
            val value = HashMap<String, ObserverWrapper>()
            value.put(name, ObserverWrapper(name, callback))
            observers.put(observer, value)
        } else {
            map.put(name, ObserverWrapper(name, callback))
        }
    }

    fun removeObserver(observer: Any, name: String? = null) {
        if (name == null) {
            observers.remove(observer)
        } else {
            name?.let {
                val observer = observers.get(observer)
                observer?.let {
                    observer.remove(name)
                }
            }
        }
    }

    fun post(name: String, value: Any? = null) {
        observers.forEach {
            it.value.forEach {
                if (it.key.equals(name)) {
                    it.value.callback(value)
                }
            }
        }
    }

    data class ObserverWrapper constructor(val name: String, val callback: (Any?) -> Unit) {}
}
