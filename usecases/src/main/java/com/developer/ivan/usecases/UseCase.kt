package com.developer.ivan.usecases

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure

abstract class UseCase<Param, Return>
{
    abstract suspend fun body(param: Param): Either<Failure, Return>

    suspend fun execute(param: Param): Either<Failure, Return> = body(param)
}