package com.behindmedia.algorithms

class LazySegmentTree(val size: Int, initialValue: (Int) -> Long): SegmentTree {
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

    private val storage: LongArray = LongArray(computeStorageSize(size))
    private val lazy = LongArray(storage.size)

    init {
        build(0, 0, size - 1, initialValue)
    }

    override fun query(range: IntRange): Long {
        return query(0, 0, size - 1, range)
    }

    override fun update(index: Int, diff: Long) {
        updateRange(0, 0, size - 1, index..index, diff)
    }

    override fun update(range: IntRange, diff: Long) {
        updateRange(0, 0, size - 1, range, diff)
    }

    override fun update(range: IntRange, value: (Int, Long) -> Long) {
        error("Unsupported operation for lazy segment tree")
    }

    fun query(index: Int, left: Int, right: Int, range: IntRange): Long {
        if (lazy[index] != 0L) {
            storage[index] += (right - left + 1) * lazy[index]
            if (left != right) {
                // Since we are not yet updating children os si,
                // we need to set lazy values for the children
                lazy[index * 2 + 1] += lazy[index]
                lazy[index * 2 + 2] += lazy[index]
            }
            lazy[index] = 0
        }
        if (left > right || left > range.last || right < range.first) return 0L
        if (left >= range.first && right <= range.last) return storage[index]
        val mid = (left + right) / 2
        return query(2 * index + 1, left, mid, range) + query(2 * index + 2, mid + 1, right, range)
    }

    private fun updateRange(index: Int, left: Int, right: Int, range: IntRange, diff: Long): Long {
        if (lazy[index] != 0L) {
            storage[index] += (right - left + 1) * lazy[index]
            if (left != right) {
                lazy[index * 2 + 1] += lazy[index]
                lazy[index * 2 + 2] += lazy[index]
            }
            lazy[index] = 0
        }
        if (left > right || left > range.last || right < range.first) return 0L
        if (left >= range.first && right <= range.last) {
            val totalDiff = (right - left + 1) * diff
            storage[index] += totalDiff
            if (left != right) {
                lazy[index * 2 + 1] += diff
                lazy[index * 2 + 2] += diff
            }
            return totalDiff
        }
        val mid = (left + right) / 2
        val diff = updateRange(index * 2 + 1, left, mid, range, diff) +
                updateRange(index * 2 + 2, mid + 1, right, range, diff)
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
