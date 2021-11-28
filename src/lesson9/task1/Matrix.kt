@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

import java.lang.IndexOutOfBoundsException

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)

    // не собирается проект когда пытаюсь добавить их в интерфейс поэтому вынес в экстеншны
//    fun contains(other: Matrix<E>): Boolean
//
//    fun getOrNull(cell: Cell): E?
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {
    require(height > 0 && width > 0)
    return MatrixImpl(height, width, e)
}

/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {
    private val dataList = MutableList(width * height) { e }

    override fun get(row: Int, column: Int): E = dataList[row * width + column]

    override fun get(cell: Cell): E = get(cell.row, cell.column)

    override fun set(row: Int, column: Int, value: E) {
        dataList[width * row + column] = value
    }

    override fun set(cell: Cell, value: E) {
        set(cell.row, cell.column, value)
    }

    override fun equals(other: Any?) = other is MatrixImpl<*> && other.dataList == dataList

    override fun toString(): String {
        val result = StringBuilder()
        for (height in 0 until this.height) {
            for (width in 0 until this.width) {
                result.append("%3d".format(this[height, width]))
            }
            result.append("\n")
        }
        return result.toString()
    }

//    override fun contains(other: Matrix<E>) = this.width >= other.width && this.height >= other.height

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        return result
    }
}

