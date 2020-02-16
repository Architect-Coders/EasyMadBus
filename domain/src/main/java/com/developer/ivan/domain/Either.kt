package com.developer.ivan.domain

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRht get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Either.Left(a)
    fun <R> right(b: R) = Either.Right(b)

    suspend fun fold(fnL: suspend (L) -> Any, fnR: suspend (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }

    fun <T> default(defaultValue: T, fnR: (R) -> T): T =
        when (this) {
            is Left -> defaultValue
            is Right -> fnR(b)
        }


}
