package com.example.ipcserver

/**
 * Created by Chandan Jana on 13-10-2022.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
object RecentClient {
    var client: Client? = null
    // Bundle keys
    const val PID = "pid"
    const val CONNECTION_COUNT = "connection_count"
    const val PACKAGE_NAME = "package_name"
    const val DATA = "data"
}
