package com.developer.ivan.usecases

import com.developer.ivan.domain.Constants
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.Token

interface IExecuteToken{



    suspend fun executeWithToken(): Either<Failure,Token>

    class ExecuteTokenImpl(private val getAccessToken: GetToken) : IExecuteToken {


        override suspend fun executeWithToken(): Either<Failure,Token> = getAccessToken.execute(
                GetToken.Params(
                    Constants.EMTApi.USER_EMAIL,
                    Constants.EMTApi.USER_PASSWORD,
                    Constants.EMTApi.API_KEY,
                    Constants.EMTApi.CLIENT_KEY
                )
            )
        }

}