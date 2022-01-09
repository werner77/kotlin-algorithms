package com.behindmedia.algorithms

class ObjectMap<T: Any>(override val sizeX: Int, override val sizeY: Int, default: (Int, Int) -> T) :
    MutableValueMap<T> {

    constructor(squareSize: Int, default: (Int, Int) -> T) : this(
        squareSize,
        squareSize,
        default
    )

    private val data: List<MutableList<T>> = List(sizeY) { y ->
        MutableList(sizeX) { x ->
            default(x, y)
        }
    }

    companion object {
        operator fun <T: Any> invoke(string: String, converter: (Char) -> T): ObjectMap<T> {
            val lines = string.trim().split("\n")
            val sizeY = lines.size
            val sizeX = lines.getOrNull(0)?.length ?: 0
            return ObjectMap(sizeX, sizeY) { x, y ->
                converter.invoke(lines[y].getOrNull(x) ?: error("Invalid coordinate: $x, $y"))
            }
        }
    }

    override operator fun get(coordinate: Coordinate): T {
        return data[coordinate.y][coordinate.x]
    }

    override operator fun set(coordinate: Coordinate, value: T) {
        data[coordinate.y][coordinate.x] = value
    }

    override fun getOrNull(coordinate: Coordinate): T? {
        return data.getOrNull(coordinate.y)?.getOrNull(coordinate.x)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherMap = other as? ObjectMap<*> ?: return false
        return data == otherMap.data
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun toString(): String = ValueMap.toString(this)
}

