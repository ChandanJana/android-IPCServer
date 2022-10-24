package com.example.ipcserver

/**
 * Created by Chandan Jana on 13-10-2022.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
data class Client(
    var clientPackageName: String?,
    var clientProcessId: String,
    var clientData: String?,
    var ipcMethod: String
)
