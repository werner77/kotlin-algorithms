package com.behindmedia.algorithms

abstract class AbstractSegmentTree(val size: Int, initialValue: (Int) -> Long) : SegmentTree {
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

    protected val storage: LongArray = LongArray(computeStorageSize(size))

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

    protected abstract fun query(index: Int, left: Int, right: Int, range: IntRange): Long
    protected abstract fun updateRange(index: Int, left: Int, right: Int, range: IntRange, diff: Long): Long

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