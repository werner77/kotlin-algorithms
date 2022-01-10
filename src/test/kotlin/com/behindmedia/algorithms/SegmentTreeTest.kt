package com.behindmedia.algorithms

import junit.framework.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class SegmentTreeTest {

    private val random = Random.Default

    @Test
    fun randomUpdatesAndQuery() {
        val sourceArray = LongArray(1_000_001) {
            random.nextLong(0, 100)
        }
        val tree1 = LazySegmentTree(sourceArray)
        val tree2 = OptimizedSegmentTree(sourceArray)
        val tree3 = RecursiveSegmentTree(sourceArray)

        val arrays = listOf(sourceArray.copyOf(), sourceArray.copyOf(), sourceArray.copyOf())
        val trees = listOf(tree1, tree2, tree3)
        for ((treeIndex, tree) in trees.withIndex()) {
            println("Testing tree implementation: ${tree::class.simpleName}")
            timing {
                repeat(10) {
                    val array = arrays[treeIndex]

                    repeat(100) {
                        val range = randomRange(array.size)
                        val diff = random.nextLong(1, 10)
                        tree.update(range, diff)
                        for (i in range) {
                            array[i] += diff
                        }
                    }

                    testSum(array, tree, 0..0)
                    testSum(array, tree, array.indices)

                    repeat(100) {
                        val range = randomRange(array.size)
                        testSum(array, tree, range)
                    }
                }
            }
        }
    }

    private fun randomRange(size: Int): IntRange {
        val end = random.nextInt(1, size)
        val start = random.nextInt(0, end)
        return IntRange(start, end - 1)
    }

    private fun testSum(array: LongArray, tree: SegmentTree, range: IntRange) {
        val treeSum = tree.query(range)
        val expected = range.fold(0L) { sum, index -> sum + array[index] }
        assertEquals("Assertion failed for tree: $tree", expected, treeSum)
    }

    @Test
    fun singletonTree() {
        val array = longArrayOf(2)
        val tree = LazySegmentTree(array)
        assertEquals(2, tree.query(0..0))
    }

    @Test
    fun update() {
    }
}