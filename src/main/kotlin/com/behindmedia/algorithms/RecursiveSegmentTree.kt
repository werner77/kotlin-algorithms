package com.behindmedia.algorithms

class RecursiveSegmentTree(val size: Int, initialValue: (Int) -> Long) : SegmentTree {
    companion object {
        private fun computeStorageSize(size: Int): Int {
            if (size <= 1) return size
            var i = size - 1
            var j = 1
            while (i != 1) {
                i /= 2
                j++
            }
            var n = 1
            repeat(j + 1) {
                n *= 2
            }
            return n - 1
        }
    }

    constructor(array: LongArray) : this(array.size, { array[it] })

    init {
        require(size > 0) { "Size should be > 0" }
    }

    private val storage: LongArray = LongArray(computeStorageSize(size))

    init {
        build(0, 0, size - 1, initialValue)
    }

    override fun query(range: IntRange): Long {
        return query(0, 0, size - 1, range)
    }

    override fun update(range: IntRange, value: (Int, Long) -> Long) {
        updateRange(0, 0, size - 1, range, value)
    }

    private fun query(index: Int, left: Int, right: Int, range: IntRange): Long {
        if (range.first <= left && range.last >= right) return storage[index]
        if (right < range.first || left > range.last) return 0L
        val mid = (left + right) / 2
        return query(2 * index + 1, left, mid, range) + query(2 * index + 2, mid + 1, right, range)
    }

    private fun updateRange(
        index: Int,
        left: Int,
        right: Int,
        range: IntRange,
        value: (Int, Long) -> Long
    ): Long {
        // out of range
        if (left > right || left > range.last || right < range.first) return 0L

        // Current node is a leaf node
        if (left == right) {
            // Add the difference to current node
            val new = value(left, storage[index])
            val diff = new - storage[index]
            storage[index] = new
            return diff
        }

        val mid = (left + right) / 2
        val diff = updateRange(index * 2 + 1, left, mid, range, value) +
                updateRange(index * 2 + 2, mid + 1, right, range, value)
        storage[index] += diff
        return diff
    }

    private fun build(index: Int, left: Int, right: Int, initialValue: (Int) -> Long): Long {
        if (left == right) {
            val value = initialValue(left)
            storage[index] = value
            return value
        }
        val mid = (left + right) / 2
        val ret = build(index * 2 + 1, left, mid, initialValue) + build(index * 2 + 2, mid + 1, right, initialValue)
        storage[index] = ret
        return ret
    }
}
