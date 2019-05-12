package hossein.gheisary.data.remote.exception

class NetworkException (override var message: String?, var code: Int = 404) : Exception()