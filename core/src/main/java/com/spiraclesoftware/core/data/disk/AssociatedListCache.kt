package com.spiraclesoftware.core.data.disk

import com.spiraclesoftware.core.domain.Identifiable
import com.spiraclesoftware.core.domain.UniqueIdentifier
import com.spiraclesoftware.core.testing.OpenForTesting

@OpenForTesting
class AssociatedListCache<Key, Value> where Key : UniqueIdentifier<*>, Value : Identifiable<Key> {

    /** Marks the cache as invalid, to force an update the next time data is requested. */
    var isDirty = false

    private var cache: HashMap<Key, Value>? = null

    fun clear() {
        isDirty = false
        cache = null
    }

    fun set(data: List<Value>) {
        isDirty = false

        cache = LinkedHashMap(data.associateBy { it.getUniqueId() })
    }

    fun get(): List<Value>? = cache?.values?.toList()

    fun get(id: Key): Value? = cache?.get(id)
}