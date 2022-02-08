@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная, общая ценность в баллах -- 32.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */

class UnsignedBigInteger(private val number: MutableList<Int> = mutableListOf()) : Comparable<UnsignedBigInteger> {

    companion object {
        private const val WRONG_FORMAT = "wrong number format"
        private const val OUT_OF_BOUNDS = "number is out of bounds Int"
        private const val DIVISION_BY_ZERO = "Division by zero"
        private const val DIVIDEND_LESS = "Dividend less than divisor"

        private fun fromString(s: String): MutableList<Int> {
            if (!s.matches(Regex("""\d+""")))
                throw NumberFormatException(WRONG_FORMAT)
            return mutableListOf<Int>().apply {
                s.forEach { digit ->
                    add(digit.digitToInt())
                }
            }
        }

        private fun fromInt(i: Int): MutableList<Int> =
            mutableListOf<Int>().apply {
                when {
                    i < 0 -> throw NumberFormatException(WRONG_FORMAT)
                    i == 0 -> add(0)
                    else -> {
                        var n = i
                        while (n != 0) {
                            add(n % 10)
                            n /= 10
                        }
                    }
                }
            }.asReversed()

        fun max(a: UnsignedBigInteger, b: UnsignedBigInteger) =
            if (a > b) a else b

        fun min(a: UnsignedBigInteger, b: UnsignedBigInteger) =
            if (a < b) a else b

        private fun buildUnsignedBigInteger(
            action: MutableList<Int>.() -> Unit,
            reverse: Boolean = true
        ): UnsignedBigInteger {
            val list = mutableListOf<Int>().apply(action)
            return if (reverse) UnsignedBigInteger(list.asReversed()) else UnsignedBigInteger(list)
        }
    }

    init {
        if (number.size != 0) {
            while (number[0] == 0 && number.size != 1) {
                number.removeFirst()
            }
        }
    }

    /**
     * Конструктор из строки
     */
    constructor(s: String) : this(fromString(s))

    /**
     * Конструктор из целого
     */
    constructor(i: Int) : this(fromInt(i))

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        val big = max(this, other).number
        val small = min(this, other).number
        return buildUnsignedBigInteger({
            var memory = 0
            for (i in big.lastIndex downTo 0) {
                val d = big[i] + small.getOrElse(i - (big.size - small.size)) { 0 } + memory
                add(d % 10)
                memory = d / 10
            }
            if (memory > 0)
                add(memory)
        })
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger =
        when {
            other == UnsignedBigInteger(0) -> throw ArithmeticException(DIVISION_BY_ZERO)
            this < other -> throw ArithmeticException(DIVIDEND_LESS)
            else ->
                buildUnsignedBigInteger({
                    var memory = 0
                    for (i in number.lastIndex downTo 0) {
                        var d =
                            number[i] - other.number.getOrElse(i - (number.size - other.number.size)) { 0 } - memory
                        if (d < 0) {
                            d += 10
                            memory = 1
                        } else {
                            memory = 0
                        }
                        add(d % 10)
                    }
                })
        }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        val big = max(this, other).number
        val small = min(this, other).number
        var result = UnsignedBigInteger(0)
        var countOfZeros = 0
        for (i in small.lastIndex downTo 0) {
            result += buildUnsignedBigInteger({
                var memory = 0
                for (j in big.lastIndex downTo 0) {
                    val d = big[j] * small.getOrElse(i) { 0 } + memory
                    add(d % 10)
                    memory = d / 10
                }
                if (memory > 0)
                    add(memory)
                reverse()
                repeat(countOfZeros) { add(0) }
                countOfZeros++
            }, reverse = false)
        }
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger =
        when {
            other > this -> UnsignedBigInteger(0)
            other == UnsignedBigInteger(0) -> throw ArithmeticException(DIVISION_BY_ZERO)
            else -> {
                buildUnsignedBigInteger(
                    {
                        var temporary = mutableListOf<Int>()
                        var afterFirstDivision = false
                        for (i in 0 until number.size + 1) {
                            if (UnsignedBigInteger(temporary) < other) {
                                number.getOrNull(i)?.let { temporary.add(it) }
                                if (afterFirstDivision)
                                    add(0)
                            } else {
                                var digit = 0
                                var current = mutableListOf<Int>()
                                while (UnsignedBigInteger(current) < UnsignedBigInteger(temporary)) {
                                    val possible = UnsignedBigInteger(current) + other
                                    if (possible == UnsignedBigInteger(temporary)) {
                                        temporary.clear()
                                        add(digit + 1)
                                    } else if (possible > UnsignedBigInteger(temporary)) {
                                        temporary = (UnsignedBigInteger(temporary) - UnsignedBigInteger(current)).number
                                        add(digit)
                                    }
                                    current = possible.number
                                    digit++
                                }
                                number.getOrNull(i)?.let { temporary.add(it) }
                                afterFirstDivision = true
                            }
                        }
                    }, reverse = false
                )
            }
        }


    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger = this - this / other * other

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean = other is UnsignedBigInteger && this.compareTo(other) == 0

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        when {
            number.size > other.number.size -> return 1
            number.size < other.number.size -> return -1
            else -> {
                number.forEachIndexed { index, digit ->
                    if (digit > other.number[index]) return 1
                    if (digit < other.number[index]) return -1
                }
            }
        }
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = buildString { number.forEach { append(it) } }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (this > UnsignedBigInteger(Int.MAX_VALUE))
            throw ArithmeticException(OUT_OF_BOUNDS)
        var result = 0
        var fraction = 1
        for (i in number.lastIndex downTo 0) {
            result += number[i] * fraction
            fraction *= 10
        }
        return result
    }

    override fun hashCode(): Int = number.hashCode()
}