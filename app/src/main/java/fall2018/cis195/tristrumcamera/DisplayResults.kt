package fall2018.cis195.tristrumcamera

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_display_results.*

class DisplayResults : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_results)
        val _similiarity = intent.getDoubleExtra("value", 0.0) * 100
        textView2.setText("Your appearance is " + java.lang.Double.toString(_similiarity) + "% similar to Tristrum")
    }

    fun returnCamera(view: View) {
        val result = Intent(this, MainActivity::class.java)
        startActivity(result)
    }

}
