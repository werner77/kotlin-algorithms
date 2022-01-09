package com.behindmedia.algorithms

import kotlin.math.max
import kotlin.math.min

class OptimizedSegmentTree(val size: Int, initializer: (Int) -> Long = { 0L }) : RandomUpdateSegmentTree {

    constructor(array: LongArray) : this(array.size, { array[it] })

    private val storage = LongArray(2 * size)

    init {
        build(initializer)
    }

    /**
     * Gets the sum of the indexes within the specified range
     */
    override fun query(range: IntRange): Long {
        return query(range.first, range.last)
    }

    override fun update(range: IntRange, diff: Long) {
        update(range) { _, old -> old + diff }
    }

    override fun update(range: IntRange, value: (Int, Long) -> Long) {
        for (index in range) {
            val p = index + size
            storage[p] = value(index, storage[p])
        }
        val l = range.last - range.first
        val i = size + range.first
        var u = min(i, i xor 1)
        var v = max(i + l, (i + l) xor 1)
        while (u > 1) {
            for (j in u .. v + 1 step 2) {
                storage[j shr 1] = storage[j] + storage[j xor 1]
            }
            u = u shr 1
            v = v shr 1
        }
    }

    // function to build the tree
    private fun build(initializer: (Int) -> Long) {
        // insert leaf nodes in tree
        for (i in 0 until size) storage[size + i] = initializer(i)

        // build the tree by calculating
        // parents
        for (i in size - 1 downTo 1) storage[i] = storage[i shl 1] + storage[i shl 1 or 1]
    }

    private fun query(left: Int, right: Int): Long {
        var l = left
        var r = right + 1
        var res = 0L
        // loop to find the sum in the range
        l += size
        r += size
        while (l < r) {
            if (l and 1 > 0) res += storage[l++]
            if (r and 1 > 0) res += storage[--r]
            l = l shr 1
            r = r shr 1
        }
        return res
    }
}
