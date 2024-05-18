//package com.example.echopen.register
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.echopen.MainActivity
//import com.example.echopen.R
//import com.example.echopen.SignInAndRegistrationActivity
//import com.example.echopen.databinding.ActivityWelcomeBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//class WelcomeActivity : AppCompatActivity() {
//    private val binding: ActivityWelcomeBinding by lazy {
//        ActivityWelcomeBinding.inflate(layoutInflater)
//    }
//    private lateinit var auth: FirebaseAuth
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
////        enableEdgeToEdge()
////        setContentView(R.layout.activity_welcome)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }
//        binding.loginButton.setOnClickListener {
//            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
//                putExtra("action", "login")
//            }
//            startActivity(intent)
//            finish()
//        }
//
//        binding.RegisterButton.setOnClickListener {
//            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
//                putExtra("action", "register")
//            }
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser: FirebaseUser? = auth.currentUser
//        if (currentUser != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
//    }


package com.example.echopen.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.echopen.MainActivity
import com.example.echopen.R
import com.example.echopen.SignInAndRegistrationActivity
import com.example.echopen.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
                putExtra("action", "login")
            }
            startActivity(intent)
            finish()
        }

        binding.RegisterButton.setOnClickListener {
            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
                putExtra("action", "register")
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}






//
////package com.example.echopen.register
////
////import android.content.Intent
////import android.os.Bundle
////import androidx.activity.enableEdgeToEdge
////import androidx.appcompat.app.AppCompatActivity
////import androidx.core.view.ViewCompat
////import androidx.core.view.WindowInsetsCompat
////import com.example.echopen.R
////import com.example.echopen.SignInAndRegistrationActivity
////import com.example.echopen.databinding.ActivityWelcomeBinding
////
////class WelcomeActivity : AppCompatActivity() {
////    private val binding:ActivityWelcomeBinding by lazy{
////        ActivityWelcomeBinding.inflate(layoutInflater)
////    }
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(binding.root)
////        enableEdgeToEdge()
////        setContentView(R.layout.activity_welcome)
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
////            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
////            insets
////        }
////
////        binding.loginButton.setOnClickListener{
////            val intent = Intent(this, SignInAndRegistrationActivity::class.java)
////            intent.putExtra("action", "login")
////            startActivity(intent)
////        }
////
////        binding.RegisterButton.setOnClickListener{
////            val intent = Intent(this, SignInAndRegistrationActivity::class.java)
////            intent.putExtra("action", "register")
////            startActivity(intent)
////        }
////    }
////}
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.echopen.MainActivity
//import com.example.echopen.R
//import com.example.echopen.SignInAndRegistrationActivity
//import com.example.echopen.databinding.ActivityWelcomeBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//
//class WelcomeActivity : AppCompatActivity() {
////    private var binding: ActivityWelcomeBinding by lazy {
////        ActivityWelcomeBinding.inflate(layoutInflater)
////    }
//
//    private val binding: ActivityWelcomeBinding by lazy {
//        ActivityWelcomeBinding.inflate(layoutInflater)
//    }
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//
//        binding.loginButton.setOnClickListener {
//            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
//                putExtra("action", "login")
//            }
//            startActivity(intent)
//            finish()
//        }
//
//        binding.RegisterButton.setOnClickListener {
//            val intent = Intent(this, SignInAndRegistrationActivity::class.java).apply {
//                putExtra("action", "register")
//            }
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val currentUser: FirebaseUser? = auth.currentUser
//        if (currentUser != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }
//    }
//}
