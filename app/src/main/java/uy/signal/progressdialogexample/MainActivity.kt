package uy.signal.progressdialogexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import uy.signal.progressdialog.ProgressDialog
import uy.signal.progressdialogexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            ProgressDialog.Builder()
                .icon(ContextCompat.getDrawable(this,R.drawable.ic_launcher_foreground))
                .cardBackgroundColor(getColor(R.color.laked))
                .customText("Processing")
                .build().show(supportFragmentManager,"Null")
        }
    }
}