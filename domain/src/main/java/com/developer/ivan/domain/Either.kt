package com.developer.ivan.domain

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRht get() = this is Right<R>
    val isLeft get() = this is Left<L>

    suspend fun <L> left(a: L) = Either.Left(a)
    suspend fun <R> right(b: R) = Either.Right(b)

    suspend fun fold(fnL: suspend (L) -> Any, fnR: suspend (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }

    suspend fun <T> default(defaultValue: T, fnR: (R) -> T): T =
        when (this) {
            is Left -> defaultValue
            is Right -> fnR(b)
        }


}

suspend fun <A, B, C> (suspend (A) -> B).c(f: suspend (B) -> C): suspend (A) -> C = {
    f(this(it))
}

suspend fun <T, L, R> Either<L, R>.flatMap(fn: suspend (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left ->{
            Either.Left(a)
        }
        is Either.Right -> fn(b)
    }

suspend fun <T, L, R> Either<L, R>.map(fn: suspend (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))