package com.example.echopen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.echopen.databinding.ActivityProfileBinding
import com.example.echopen.register.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.AddNewBlogButton.setOnClickListener {
            startActivity(Intent(this, SavedArticlesActivity::class.java))
        }
        binding.articleButton.setOnClickListener {
            startActivity(Intent(this, ArticleActivity::class.java))
        }

        binding.LogOutButton.setOnClickListener {
            auth.signOut()

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_profile)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users")

        val userId: String? = auth.currentUser?.uid

        if(userId != null){
            loadUserProfileDate(userId)
        }

    }
    private fun loadUserProfileDate(userId: String) {
        val userReference: DatabaseReference = databaseReference.child(userId)

        userReference.child("profileImage").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImageUrl :String? = snapshot.getValue(String::class.java)
                if(profileImageUrl != null){
                    Glide.with(this@ProfileActivity)
                        .load(profileImageUrl)
                        .into(binding.userProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to load user image", Toast.LENGTH_SHORT).show()

            }

        })

        userReference.child("name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userName: String? = snapshot.getValue(String ::class.java)
                if(userName != null){
                    binding.userName.text = userName
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}