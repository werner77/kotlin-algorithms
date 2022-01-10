package com.behindmedia.algorithms

class LazySegmentTree(size: Int, initialValue: (Int) -> Long): AbstractSegmentTree(size, initialValue) {
    constructor(array: LongArray) : this(array.size, { array[it] })

    private val lazy = LongArray(storage.size)

    override fun query(index: Int, left: Int, right: Int, range: IntRange): Long {
        if (!shouldProcess(index, left, right, range)) return 0L
        if (left >= range.first && right <= range.last) return storage[index]
        val mid = (left + right) / 2
        return query(2 * index + 1, left, mid, range) + query(2 * index + 2, mid + 1, right, range)
    }

    override fun updateRange(index: Int, left: Int, right: Int, range: IntRange, diff: Long): Long {
        if (!shouldProcess(index, left, right, range)) return 0L
        if (left >= range.first && right <= range.last) {
            return processLazy(index, left, right, diff)
        }
        val mid = (left + right) / 2
        val delta = updateRange(index * 2 + 1, left, mid, range, diff) +
                updateRange(index * 2 + 2, mid + 1, right, range, diff)
        storage[index] += delta
        return delta
    }

    private inline fun processLazy(index: Int, left: Int, right: Int, diff: Long): Long {
        val totalDiff = (right - left + 1) * diff
        storage[index] += totalDiff
        if (left != right) {
            // Since we are not yet updating children os si,
            // we need to set lazy values for the children
            lazy[index * 2 + 1] += diff
            lazy[index * 2 + 2] += diff
        }
        return totalDiff
    }

    private inline fun shouldProcess(index: Int, left: Int, right: Int, range: IntRange): Boolean {
        if (left > right || left > range.last || right < range.first) return false
        if (lazy[index] != 0L) {
            processLazy(index, left, right, lazy[index])
            lazy[index] = 0
        }
        return true
    }
}
