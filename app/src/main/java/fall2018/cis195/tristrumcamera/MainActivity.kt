package fall2018.cis195.tristrumcamera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream

private val MY_PERMISSIONS_REQUEST_INTERNET = 0
private val MY_PERMISSIONS_WRITE_EXTERNAL = 1
private val REQUEST_IMAGE_CAPTURE = 2

class MainActivity : AppCompatActivity() {

    private val activityContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.INTERNET),
                    MY_PERMISSIONS_REQUEST_INTERNET)
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_WRITE_EXTERNAL)
        }
    }

    fun cameraMethod(view: View) {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data.extras
            val imageBitmap = extras!!.get("data") as Bitmap
            val stream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            CheckImage(object : CheckImage.AsyncResponse {
                override fun processFinish(output: JSONObject) {
                    var confidence: Double?
                    try {
                        confidence = output.getJSONArray("images").getJSONObject(0)
                                .getJSONObject("transaction").get("confidence") as Double

                    } catch (e: JSONException) {
                        confidence = 0.0
                    }

                    val result = Intent(activityContext, DisplayResults::class.java)
                    result.putExtra("value", confidence)
                    startActivity(result)
                }
            }, resources).execute(byteArray)

        }
    }
}
