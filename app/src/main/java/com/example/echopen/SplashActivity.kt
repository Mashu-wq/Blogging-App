//package com.example.echopen
//
//import com.example.echopen.register.WelcomeActivity
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
////import com.example.echopen.register.WelcomeActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//class SplashActivity : AppCompatActivity() {
//    private lateinit var auth:FirebaseAuth
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        auth = FirebaseAuth.getInstance()
////        enableEdgeToEdge()
////        setContentView(R.layout.activity_splash)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////
////        }
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this,WelcomeActivity::class.java))
//            finish()
//        },3000)
//    }
//    override  fun onStart(){
//        super.onStart()
//       val currentUser: FirebaseUser? = auth.currentUser
//        if(currentUser != null){
//           startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//
//   }
//}



package com.example.echopen

import com.example.echopen.register.WelcomeActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is signed in
            val currentUser: FirebaseUser? = auth.currentUser
            if (currentUser != null) {
                // User is signed in, go to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // User is not signed in, go to WelcomeActivity
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            finish()
        }, 3000) // 3 seconds delay
    }
}


