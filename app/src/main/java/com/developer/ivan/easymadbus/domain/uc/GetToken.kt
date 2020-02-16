package com.developer.ivan.easymadbus.domain.uc

import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.UseCase
import com.developer.ivan.easymadbus.domain.models.Token
import com.developer.ivan.easymadbus.domain.repository.IBusRepository

class GetToken(val repository: IBusRepository) : UseCase<GetToken.Params, Token>() {
    override suspend fun body(param: Params): Either<Failure, Token> =
        repository.login(param.email, param.password, param.apiKey, param.clientKey)

    class Params(val email: String, val password: String, val apiKey: String, val clientKey: String)
}