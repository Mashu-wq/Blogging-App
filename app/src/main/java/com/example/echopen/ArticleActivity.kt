package com.example.echopen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.adapter.ArticleAdapter
import com.example.echopen.adapter.BlogAdapter
import com.example.echopen.databinding.ActivityArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticleActivity : AppCompatActivity() {
    private val binding: ActivityArticleBinding by lazy {
        ActivityArticleBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var blogAdapter: ArticleAdapter
    private val EDIT_BLOG_REQUEST_CODE = 123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_article)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val currentUserId: String? = auth.currentUser?.uid
        val recyclerView: RecyclerView = binding.articleRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (currentUserId != null) {
            blogAdapter =
                ArticleAdapter(this, emptyList(), object : ArticleAdapter.OnItemClickListener {
                    override fun onEditClick(blogItem: BlogItemModel) {
                        val intent = Intent(this@ArticleActivity, ReadMoreActivity::class.java)
                        intent.putExtra("blogItem", blogItem)
                        startActivityForResult(intent, EDIT_BLOG_REQUEST_CODE)
                    }

                    override fun onReadMoreClick(blogItem: BlogItemModel) {
                        val intent = Intent(this@ArticleActivity, ReadMoreActivity::class.java)
                        intent.putExtra("blogItem", blogItem)
                        startActivity(intent)
                    }

                    override fun onDeleteClick(blogItem: BlogItemModel) {
                        deleteBlogPost(blogItem)
                    }

                })

        }



        recyclerView.adapter = blogAdapter

        databaseReference =
            FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("blogs")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blogSavedList = ArrayList<BlogItemModel>()
                for (postSnapshot: DataSnapshot in snapshot.children) {
                    val blogsaved: BlogItemModel? = postSnapshot.getValue(BlogItemModel::class.java)
                    if (blogsaved != null && currentUserId == blogsaved.userId) {
                        blogSavedList.add(blogsaved)
                    }
                }
                blogAdapter.setData(blogSavedList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ArticleActivity,
                    "Error loading Saved blogs",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


    }

    private fun deleteBlogPost(blogItem: BlogItemModel) {
        val postId: String = blogItem.postId
        val blogPostReference: DatabaseReference = databaseReference.child(postId)

        blogPostReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Blog post deleted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Blog post Deleted UnSuccessfully", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_BLOG_REQUEST_CODE && resultCode == Activity.RESULT_OK){

        }
    }
}