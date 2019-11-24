package com.developer.ivan.easymadbus.core

sealed class Failure
{
    object ConnectivityError: Failure()
    object JsonException: Failure()

    class ServerError(errorCode: Int): Failure()

    abstract class FailureAbstract: Failure()
}
