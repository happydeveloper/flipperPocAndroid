package com.talktomeinkorean.debuggingpocapplication

import com.facebook.flipper.core.FlipperConnection
import com.facebook.flipper.core.FlipperObject
import com.facebook.flipper.core.FlipperPlugin

class TestPlugin : FlipperPlugin {
    private var connection: FlipperConnection? = null

    override fun getId(): String = "sea-mammals"

    override fun onConnect(connection: FlipperConnection?) {
        this.connection = connection

        FlipperObject.Builder()
            .put("status", "connected")
            .build()
    }

    override fun onDisconnect() {
        connection = null
    }

    override fun runInBackground(): Boolean = false

    private fun newRow(row: FlipperObject) {
        connection?.send("newRow", row)
    }
}