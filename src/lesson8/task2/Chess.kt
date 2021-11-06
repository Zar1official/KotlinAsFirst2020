@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.max

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String =
        if (inside()) "${'a' + column - 1}${row}" else ""
}

/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square {
    if (notation.length != 2)
        throw IllegalArgumentException("notation is incorrect")
    val square = Square(notation[0] - 'a' + 1, notation[1].digitToInt())
    if (!square.inside())
        throw IllegalArgumentException("notation is incorrect")
    return square
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int =
    when {
        !start.inside() || !end.inside() -> throw IllegalArgumentException("square is incorrect")
        start == end -> 0
        start.column == end.column || start.row == end.row -> 1
        else -> 2
    }

/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> = when (rookMoveNumber(start, end)) {
    0 -> listOf(start)
    1 -> listOf(start, end)
    else -> listOf(start, Square(start.column, end.row), end)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int = when {
    !start.inside() || !end.inside() -> throw IllegalArgumentException("incorrect square")
    (start.row + start.column) % 2 != (end.row + end.column) % 2 -> -1
    start == end -> 0
    abs(start.row - end.row) == abs(start.column - end.column) -> 1
    else -> 2
}

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> =
    when (bishopMoveNumber(start, end)) {
        -1 -> listOf()
        0 -> listOf(start)
        1 -> listOf(start, end)
        else -> {
            var res = listOf<Square>()
            for (r in 1..8) {
                for (c in 1..8) {
                    val square = Square(c, r)
                    val fromStartDistance = bishopMoveNumber(start, square)
                    val fromEndDistance = bishopMoveNumber(end, square)
                    if (fromEndDistance == fromStartDistance && fromEndDistance == 1)
                        res = listOf(start, square, end)
                }
            }
            res
        }
    }

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int =
    if (!start.inside() || !end.inside()) throw IllegalArgumentException("square is incorrect") else max(
        abs(start.row - end.row),
        abs(start.column - end.column)
    )

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> = TODO()

/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
fun knightMoveNumber(start: Square, end: Square): Int = when {
    start.inside() && end.inside() -> knightTrajectory(start, end).size - 1
    else -> throw IllegalArgumentException("square is incorrect")
}

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> =
    when {
        start == end -> listOf(start)
        getKnightMoves(start).any { it == end } -> listOf(start, end) // если попали в end сразу
        else -> {
            // создаем из нашей доски невзвешенный граф
            val moves = mutableMapOf<Square, Set<Square>>()
            for (r in 1..8) {
                for (c in 1..8) {
                    val currentSquare = Square(c, r)
                    moves[currentSquare] = getKnightMoves(currentSquare)
                }
            }
            bfs(start, end, moves) // и запускаем поиск в ширину.
        }
    }

// функция, которая возвращает всевозможные ходы коня из клетки
fun getKnightMoves(square: Square): Set<Square> {
    val column = square.column
    val row = square.row
    return listOf(
        Square(column + 2, row + 1),
        Square(column + 2, row - 1),
        Square(column - 2, row + 1),
        Square(column - 2, row - 1),
        Square(column + 1, row + 2),
        Square(column + 1, row - 2),
        Square(column - 1, row + 2),
        Square(column - 1, row - 2)
    ).filter { it.inside() }.toSet()
}

// воспользуемся поиском в ширину. (суть поиска в ширину подсмотрел из интернета)
fun bfs(start: Square, end: Square, graph: Map<Square, Set<Square>>): List<Square> {
    val distances = mutableMapOf<Square, Int>() // здесь будут храниться длины путей от каждой клетки до стартовой
    val prev = mutableMapOf<Square, Square>() // для каждой клетки будем хранить предыдущую клетку на кратчайшем пути
    for (i in graph.keys) {
        distances[i] = 8 // так как ищем кратчайший путь, забиваем по умолчанию макс значением
        prev[i] = Square(0, 0) // забиваем клеткой, которая не может существовать на доске
    }
    distances[start] = 0 // расстояние от start до самой себя 0
    val queue = ArrayDeque<Square>() // нашел только эту реализацию очереди
    queue.add(start)
    while (queue.isNotEmpty()) {
        val s = queue.first()
        queue.remove(s)
        for (i in graph[s]!!) {
            if (distances[i]!! > (distances[s]!! + 1)) {
                prev[i] = s
                distances[i] = distances[s]!! + 1
                queue.add(i)
            }
        }
    }
    println(distances)
    // теперь осталось восстановить путь
    var finish = end
    val resultPath = mutableListOf<Square>()
    while (finish != Square(0, 0)) {
        resultPath.add(finish)
        finish = prev[finish]!!
    }
    return resultPath.reversed() // переворачиваем путь, так как шли с finish
}


fun main() {
    println(knightTrajectory(Square(1, 1), Square(4, 4)))
}