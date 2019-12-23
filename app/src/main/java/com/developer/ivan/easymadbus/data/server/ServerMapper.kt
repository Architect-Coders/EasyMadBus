package com.developer.ivan.easymadbus.data.server

import com.developer.ivan.easymadbus.core.Either
import com.developer.ivan.easymadbus.core.Failure
import com.developer.ivan.easymadbus.core.empty
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException


object ServerMapper
{
    inline fun <reified T> parseDataServerResponse(data: String): Either<Failure,T>
    {
        return try {
            val dataServer: T = Gson().fromJson(data, object : TypeToken<T>(){}.type)
            Either.Right(dataServer)
        }catch (e: JsonSyntaxException){
            Either.Left(Failure.JsonException(e.localizedMessage ?: String.empty))
        }
    }

    inline fun <reified T> parseDataServerResponseFirst(data: String): Either<Failure,T>
    {
        return try {

            val arrayList = JSONArray(data).getString(0)
            parseDataServerResponse(arrayList)
        }catch (e: JSONException){
            Either.Left(Failure.JsonException(e.localizedMessage ?: String.empty))
        }
    }

}