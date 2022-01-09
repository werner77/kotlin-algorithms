package com.behindmedia.algorithms

class RecursiveSegmentTree(size: Int, initialValue: (Int) -> Long) : AbstractSegmentTree(size, initialValue), RandomUpdateSegmentTree {
    constructor(array: LongArray) : this(array.size, { array[it] })

    override fun updateRange(index: Int, left: Int, right: Int, range: IntRange, diff: Long): Long {
        return updateRange(index, left, right, range) { _, old -> old + diff }
    }

    override fun query(index: Int, left: Int, right: Int, range: IntRange): Long {
        if (range.first <= left && range.last >= right) return storage[index]
        if (right < range.first || left > range.last) return 0L
        val mid = (left + right) / 2
        return query(2 * index + 1, left, mid, range) + query(2 * index + 2, mid + 1, right, range)
    }

    override fun update(range: IntRange, value: (Int, Long) -> Long) {
        updateRange(0, 0, size - 1, range, value)
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
        val diff = updateRange(index * 2 + 1, left, mid, range, value) + updateRange(index * 2 + 2, mid + 1, right, range, value)
        storage[index] += diff
        return diff
    }
}
