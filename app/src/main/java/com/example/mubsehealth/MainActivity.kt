package com.example.mubsehealth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mubsehealth.doctors.DoctorsHome
import com.example.mubsehealth.model.PrefsManager
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefsManager.setContext(this.application)

        Handler().postDelayed({
            if (prefsManager.isLoggedIn()){
                startActivity(Intent(this, Home::class.java))
                finish()
            }
            else if(prefsManager.isDLoggedIn()){
                startActivity(Intent(this, DoctorsHome::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, Login::class.java))
                finish()
            }

        }, 3000)

//        scope.launch {
//            delay(3000)
//
//            startActivity(Intent(this@MainActivity, Login::class.java))
//            finish()
//        }
    }

    override fun onPause() {
       // scope.cancel()
        super.onPause()
    }
}