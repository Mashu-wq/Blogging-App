package com.example.echopen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.databinding.ActivityReadMoreBinding

class ReadMoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            finish()
        }

        val blogs:BlogItemModel? = intent.getParcelableExtra<BlogItemModel>("blogItem")

        if(blogs != null){
            binding.titleText.text = blogs.heading
            binding.userName.text = blogs.userName
            binding.Date.text = blogs.date
            binding.blogDescriptionTextView.text = blogs.post

            val userImageUrl:String? = blogs.profileImage
            Glide.with(this).load(userImageUrl).apply(RequestOptions.circleCropTransform()).into(binding.profileImage)

        }
        else{
            Toast.makeText(this, "Failed to load blogs", Toast.LENGTH_SHORT).show()
        }
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_read_more)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}