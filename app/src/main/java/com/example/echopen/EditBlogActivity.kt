package com.example.echopen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.databinding.ActivityEditBlogBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditBlogActivity : AppCompatActivity() {
    private val binding : ActivityEditBlogBinding by lazy {
        ActivityEditBlogBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            finish()
        }

        val blogItemModel: BlogItemModel? = intent.getParcelableExtra<BlogItemModel>("blogItem")

        binding.blogTitle.editText?.setText(blogItemModel?.heading)
        binding.blogDescription.editText?.setText(blogItemModel?.post)

        binding.saveBlogButton.setOnClickListener {
            val updatedTitle : String = binding.blogTitle.editText?.text.toString().trim()

            val updatedDescription : String = binding.blogDescription.editText?.text.toString().trim()

            if(updatedTitle.isEmpty() || updatedDescription.isEmpty()){
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }else{
                blogItemModel ?.heading = updatedTitle
                blogItemModel?.post = updatedDescription

                if(blogItemModel != null){
                    updateDataInFirebase(blogItemModel)
                }
            }
        }
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_edit_blog)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun updateDataInFirebase(blogItemModel: BlogItemModel) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("blogs")

        val postId:String = blogItemModel.postId

        databaseReference.child(postId).setValue(blogItemModel)
            .addOnSuccessListener {
                Toast.makeText(this, "Blog Updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Blog Updated Unsuccessfully", Toast.LENGTH_SHORT).show()
            }

    }
}