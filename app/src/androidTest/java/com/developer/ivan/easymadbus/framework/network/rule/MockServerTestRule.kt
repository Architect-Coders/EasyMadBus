package com.developer.ivan.easymadbus.framework.network.rule

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException

class MockServerTestRule : TestRule {

    val server = MockWebServer()

    companion object{
        const val PORT = 8080
    }

    override fun apply(base: Statement?, description: Description?): Statement = object:
        Statement() {
        override fun evaluate() {

            server.start(PORT)
            base?.evaluate()

            try {

                server.shutdown()
            }catch (e: IOException){}
        }


    }
}