package com.behindmedia.algorithms

interface SegmentTree {
    fun query(range: IntRange): Long
    fun update(range: IntRange, diff: Long)
    fun update(index: Int, diff: Long) = update(index..index, diff)
}

interface RandomUpdateSegmentTree: SegmentTree {
    fun update(index: Int, value: (Long) -> Long) = update(index..index) { _, old -> value(old) }
    fun update(range: IntRange, value: (Int, Long) -> Long)
}