package com.developer.ivan.easymadbus.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

interface Scope : CoroutineScope {

    var job: Job

    fun initScope()
    {
        job = SupervisorJob()
    }

    fun cancelScope(){
        job.cancel()
    }


    class DefaultScopeImplementation: Scope {
        override lateinit var job: Job

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Default + job
    }

    class IOScopeImplementation : Scope {
        override lateinit var job: Job

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

    }
}