package com.developer.ivan.easymadbus.core

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure

/*abstract class UseCase<Param, Return, Scope> where Scope : CoroutineScope
{
    abstract suspend fun body(param: Param): Either<Failure,Return>

    fun execute(onResult: (Either<Failure,Return>)->Unit,param: Param,scope: Scope){

        scope.launch {
            val deferredResult = async{body(param)}

            withContext(Dispatchers.Main){
                onResult(deferredResult.await())
            }
        }
    }
}*/

abstract class UseCase<Param, Return>
{
    abstract suspend fun body(param: Param): Either<Failure,Return>

    suspend fun execute(param: Param): Either<Failure, Return> = body(param)
}