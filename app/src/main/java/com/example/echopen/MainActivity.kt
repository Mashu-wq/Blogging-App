package com.example.echopen

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.adapter.BlogAdapter
import com.example.echopen.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.collection.LLRBNode.Color

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private val blogItems = mutableListOf<BlogItemModel>()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.saveArticalButton.setOnClickListener{
            startActivity(Intent(this, SavedArticlesActivity::class.java))
        }
        binding.profileImage.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
        }
        binding.cardView2.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("blogs")
        var userId: String? = auth.currentUser?.uid

        if(userId != null){
            loadUserProfileImage(userId)
        }

        val recyclerView: RecyclerView = binding.blogRecyclerView
        val blogAdapter = BlogAdapter(blogItems)
        recyclerView.adapter = blogAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //fetch data from firebase database
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                blogItems.clear()
               for(snapshot: DataSnapshot in snapshot.children){
                   val blogItem : BlogItemModel? = snapshot.getValue(BlogItemModel::class.java)
                   if(blogItem != null){
                       blogItems.add(blogItem)
                   }
               }
                blogItems.reverse()
                //notify the adapter that the data has changed
                blogAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Blog loading field", Toast.LENGTH_SHORT).show()

            }

        })


        binding.floatingAddArticleButton.setOnClickListener{
            startActivity(Intent(this, AddArticleActivity::class.java))
        }

    }

    private fun loadUserProfileImage(userId: String) {
        val userReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(userId)

        userReference.child("profileImage").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImageUrl: String? = snapshot.getValue(String::class.java)

                if(profileImageUrl != null){
                    Glide.with(this@MainActivity).load(profileImageUrl).into(binding.profileImage)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error loading profile image", Toast.LENGTH_SHORT).show()
            }

        })
    }
}