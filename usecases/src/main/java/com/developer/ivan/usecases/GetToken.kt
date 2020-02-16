package com.developer.ivan.usecases

import com.developer.ivan.data.repository.IBusRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token

class GetToken(private val repository: IBusRepository) : UseCase<GetToken.Params, Token>() {
    override suspend fun body(param: Params): Either<Failure, Token> =
        repository.login(param.email, param.password, param.apiKey, param.clientKey)

    class Params(val email: String, val password: String, val apiKey: String, val clientKey: String)
}