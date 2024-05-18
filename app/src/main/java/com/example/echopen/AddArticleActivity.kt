package com.example.echopen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.echopen.Model.UserData
import androidx.appcompat.app.AppCompatActivity
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class AddArticleActivity : AppCompatActivity() {
    private val binding: ActivityAddArticleBinding by lazy{
        ActivityAddArticleBinding.inflate(layoutInflater)
    }
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("blogs")
    private val userReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addBlogButton.setOnClickListener{
            val title: String = binding.blogTitle.editText?.text.toString().trim()
            val description: String = binding.blogDescription.editText?.text.toString().trim()

            if(title.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

            }
            val user: FirebaseUser?= auth.currentUser
            if(user != null){
                val userId:String = user.uid
                val userName: String = user.displayName ?: "Anonymous"
                val userEmail: String = user.email ?: ""
                val userImageUrl = user.photoUrl?:""

               userReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       val userData: UserData? = snapshot.getValue(UserData::class.java)
                       if(userData != null){
                           val userNameFromDB: String = userData.name
                           val userImageUrlFromDB = userData.profileImage

                           val currentDate:String = SimpleDateFormat("yyyy-MM-dd").format(Date())

                           val blogItem = BlogItemModel(
                               title,
                               userNameFromDB,
                               currentDate,
                               description,
                               userId,
                               0,
                               userImageUrlFromDB
                           )
                           val key: String? = databaseReference.push().key
                           if(key != null){
                               blogItem.postId = key
                               val blogReference: DatabaseReference = databaseReference.child(key)
                               blogReference.setValue(blogItem).addOnCompleteListener {
                                   if(it.isSuccessful){
                                       Toast.makeText(this@AddArticleActivity, "Blog added successfully", Toast.LENGTH_SHORT).show()
                                       // Navigate to MainActivity
                                       startActivity(Intent(this@AddArticleActivity, MainActivity::class.java))
                                       finish()
                                   }
                                   else{
                                       Toast.makeText(this@AddArticleActivity, "Failed to add blog", Toast.LENGTH_SHORT).show()
                                   }
                               }
                           }
//                           else {
//                               Toast.makeText(this@AddArticleActivity, "Failed to generate unique key", Toast.LENGTH_SHORT).show()
//                           }
                       }
                   }

                   override fun onCancelled(error: DatabaseError) {

                   }

               })

            }

        }



//        enableEdgeToEdge()
//        setContentView(R.layout.activity_add_article)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}


