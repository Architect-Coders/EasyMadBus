package com.developer.ivan.easymadbus.core

import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Call
import kotlin.Throwable as ThrowableRequest


interface IBaseRepository {

    fun <R> request(
        request: Call<String>,
        transform: (String) -> R,
        default: String = String.empty
    ): Either<Failure, R>


    class BaseRepositoryImplementation : IBaseRepository {

        companion object {
            const val CODE_OK = "00"
            const val CODE_REFRESH = "01"
            const val CODE_FIELD = "code"
            const val DATA_FIELD = "data"
        }


        override fun <R> request(
            request: Call<String>,
            transform: (String) -> R,
            default: String
        ): Either<Failure, R> {
            return try {
                val result = request.execute()

                when (result.isSuccessful) {
                    true -> {

                        try {
                            val requestBody = JSONObject(result.body() ?: default)

                            val requestCode = requestBody.getString(CODE_FIELD)

                            if (requestCode == CODE_OK ||
                                requestCode == CODE_REFRESH
                            ) {
                                Either.Right(
                                    transform(
                                        if (!requestBody.getString(DATA_FIELD).isNullOrEmpty()) requestBody.getString(
                                            DATA_FIELD
                                        ) else default
                                    )
                                )
                            } else
                                Either.Left(Failure.ServerError(requestCode))


                        } catch (syntaxException: JsonSyntaxException) {

                            Either.Left(Failure.JsonException(syntaxException.localizedMessage))

                        }
                        Either.Right(transform(result.body() ?: default))
                    }
                    false -> Either.Left(Failure.ServerError(result.code().toString()))
                }

            } catch (exception: ThrowableRequest) {
                Either.Left(Failure.ServerException(exception))
            }
        }
    }
}