package com.developer.ivan.easymadbus.core

import java.lang.Exception

sealed class Failure
{
    object ConnectivityError: Failure()
    class JsonException(val body: String): Failure()

    class ServerError(val errorCode: String): Failure()
    class ServerException(val serverException: Throwable): Failure()

    abstract class FailureAbstract: Failure()
}
