package hossein.gheisary.data.remote.exception

import android.content.Context
import com.google.gson.Gson
import hossein.gheisary.data.R
import okhttp3.ResponseBody
import retrofit2.HttpException
import org.json.JSONException
import org.json.JSONArray
import org.json.JSONObject
import java.net.UnknownHostException
import javax.inject.Inject

class ExceptionHandler @Inject constructor(var context: Context, var standardHttpErrorHandler: StandardHttpErrorHandler) {
    fun handleError(error: Throwable) : NetworkException {
        when (error) {
            is HttpException -> {
                val errorBody = (error.response().errorBody()as ResponseBody).string()

                if(errorBody.isNotEmpty() && isJSONValid(errorBody)){
                    return Gson().fromJson(errorBody, NetworkException::class.java)
                }else {
                    return standardHttpErrorHandler.handleError(error.code())
                }
            }
            is NetworkException ->
                return if(isJSONValid(error.message)){
                    Gson().fromJson(error.message, NetworkException::class.java)
                }else {
                    if(error.message?.isNotEmpty()!!){
                        NetworkException(error.message+ context.getString(R.string.error_code) + error.code)
                    }else {
                        NetworkException(context.getString(R.string.server_error)+ context.getString(R.string.error_code) + error.code)
                    }
                }

            is UnknownHostException -> return NetworkException(error.message)

            else -> return NetworkException(context.getString(R.string.server_error))
        }
    }

    private fun isJSONValid(message: String?): Boolean {
        try {
            JSONObject(message)
        } catch (ex: JSONException) {
            try {
                JSONArray(message)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }
}
