package fall2018.cis195.tristrumcamera

import android.content.res.Resources
import android.os.AsyncTask
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by arelin on 10/20/18.
 */

internal class CheckImage(asyncResponse: AsyncResponse, private val _resources: Resources)
    : AsyncTask<ByteArray, Any, String>() {

    var asyncResponse: AsyncResponse? = null

    interface AsyncResponse {
        fun processFinish(output: JSONObject)
    }

    init {
        this.asyncResponse = asyncResponse
    }


    override fun doInBackground(vararg image: ByteArray): String {

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("gallery_name", "tristrum")
                .addFormDataPart("subject_id", "tristrum")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MediaType.parse("image/*"), image[0]))
                .build()
        val headers = Headers.of("app_id", _resources.getString(R.string.appId), "app_key",
                _resources.getString(R.string.appKey))
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("https://api.kairos.com/verify")
                .headers(headers)
                .post(requestBody)
                .build()
        try {
            val response = client.newCall(request).execute()
            return response.body().string().toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return "{}"
        }

    }

    override fun onPostExecute(responseBody: String) {
        super.onPostExecute(responseBody)
        try {
            asyncResponse!!.processFinish(JSONObject(responseBody))
        } catch (e: JSONException) {
            e.printStackTrace()
            asyncResponse!!.processFinish(JSONObject())
        }

    }
}
