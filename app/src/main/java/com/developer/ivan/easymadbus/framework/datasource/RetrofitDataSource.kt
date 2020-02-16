package com.developer.ivan.easymadbus.framework.datasource

import com.developer.ivan.data.repository.RemoteDataSource
import com.developer.ivan.domain.*
import com.developer.ivan.easymadbus.core.IRequest
import com.developer.ivan.easymadbus.data.server.ServerMapper
import com.developer.ivan.easymadbus.data.server.models.EntityArrive
import com.developer.ivan.easymadbus.data.server.models.EntityBusStop
import com.developer.ivan.easymadbus.data.server.models.EntityToken
import com.developer.ivan.easymadbus.framework.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

data class RetrofitDataSource(val apiService: ApiService,
                              val servermapper: ServerMapper
                              ): IRequest by IRequest.RequestRetrofitImplementation(), RemoteDataSource{
    override suspend fun getLogin(headers: Map<String, String>): Either<Failure, Token> {

        return withContext(Dispatchers.IO){
             request(apiService.getLogin(headers),{ token->

                when (val data =
                    servermapper.parseDataServerResponseFirst<EntityToken>(token)) {
                    is Either.Left -> EntityToken.empty()
                    is Either.Right -> data.b
                }.toDomain()
            })
        }


    }

    override suspend fun getBusStops(headers: Map<String, String>): Either<Failure, List<BusStop>> {

        return withContext(Dispatchers.IO){
            request(apiService.getBusStops(headers),{ token->

                when (val data =
                    servermapper.parseDataServerResponse<List<EntityBusStop>>(token)) {
                    is Either.Left -> listOf()
                    is Either.Right -> data.b
                }.map { it.toDomain() }
            })
        }


    }

    override suspend fun getArrives(
        busStop: String,
        headers: Map<String, String>,
        body: Map<String, String>
    ): Either<Failure, List<Arrive>> {

        val bodyJson = JSONObject(body).toString()

        return withContext(Dispatchers.IO){
            request(apiService.getArrivesEndpoint(busStop,bodyJson,headers),{arrives->
                when (val data =
                    servermapper.parseArriveServerResponse<List<EntityArrive>>(arrives)) {
                    is Either.Left -> listOf()
                    is Either.Right -> data.b
                }.map { it.toDomain() }
            })
        }
    }


}