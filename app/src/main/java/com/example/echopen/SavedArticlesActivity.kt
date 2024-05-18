package com.example.echopen

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.adapter.BlogAdapter
import com.example.echopen.databinding.ActivityAddArticleBinding
import com.example.echopen.databinding.ActivitySavedArticlesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class SavedArticlesActivity : AppCompatActivity() {
    private val binding: ActivitySavedArticlesBinding by lazy {
        ActivitySavedArticlesBinding.inflate(layoutInflater)
    }

    private val savedBlogsArticles = mutableListOf<BlogItemModel>()
    private lateinit var blogAdapter: BlogAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_saved_articles)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        blogAdapter = BlogAdapter(savedBlogsArticles.filter { it.isSaved }.toMutableList())

        val recyclerView: RecyclerView = binding.savedArticleRecyclerView
        recyclerView.adapter = blogAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId: String? = auth.currentUser?.uid
        if(userId != null){
            val userReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(userId).child("saveBlogPosts")

            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(postSnapshot: DataSnapshot in snapshot.children){
                        val postId:String? = postSnapshot.key
                        val isSaved: Boolean = postSnapshot.value as Boolean
                        if(postId != null && isSaved){
                            CoroutineScope(Dispatchers.IO).launch {
                                val blogItem = fetchBlogItem(postId)
                                if(blogItem != null){
                                    savedBlogsArticles.add(blogItem)
                                    launch (Dispatchers.Main){
                                        blogAdapter.updateData(savedBlogsArticles)
                                    }
                                }
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }

        val backButton:ImageButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private suspend fun fetchBlogItem(postId: String): BlogItemModel? {
        val blogReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("blogs")
        return try{
            val dataSnapshot: DataSnapshot = blogReference.child(postId).get().await()
            val blogData: BlogItemModel? = dataSnapshot.getValue(BlogItemModel::class.java)
            blogData
        }catch (e: Exception){
            null
        }

    }
}