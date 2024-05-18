package com.example.echopen.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.echopen.Model.BlogItemModel
import com.example.echopen.R
import com.example.echopen.ReadMoreActivity
import com.example.echopen.databinding.BlogItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogAdapter(private val items: MutableList<BlogItemModel>): RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    private val databaseReference: FirebaseDatabase = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: BlogItemBinding = BlogItemBinding.inflate(inflater, parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blogItem:BlogItemModel = items[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class BlogViewHolder(private val binding: BlogItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(blogItemModel: BlogItemModel) {
            val postId: String? = blogItemModel.postId
            val context: Context = binding.root.context
            binding.heading.text = blogItemModel.heading
            Glide.with(binding.profile.context).load(blogItemModel.profileImage).into(binding.profile)
            binding.userName.text = blogItemModel.userName
            binding.date.text = blogItemModel.date
            binding.post.text = blogItemModel.post
            binding.likeCount.text = blogItemModel.likeCount.toString()

            //set on click listener
            binding.root.setOnClickListener {
                val context: Context = binding.root.context
                val intent = Intent(context, ReadMoreActivity::class.java)
                intent.putExtra("blogItem", blogItemModel)
                context.startActivity(intent)
            }


                val postLikeReference: DatabaseReference? =
                    postId?.let { databaseReference.reference.child("blogs").child(it).child("likes") }
                // Use postLikeReference as needed, e.g., to set a value or add a listener

                val currentUserLiked:Unit? = currentUser?.uid?.let {
                        uid ->
                    if (postLikeReference != null) {
                        postLikeReference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.exists()){
                                    binding.likeButton.setImageResource(R.drawable.btn_star)

                                } else{
                                    binding.likeButton.setImageResource(R.drawable.heart)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })
                    }
                }
                binding.likeButton.setOnClickListener {
                    if(currentUser != null){
                        if (postId != null) {
                            handleLikedButtonClicked(postId, blogItemModel, binding)
                        }

                    }
                    else{
                        Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show()
                    }
                }

            // set the initial icon based on the saves status
            val userReference: DatabaseReference = databaseReference.reference.child("users").child(currentUser?.uid ?: "")
            val postSaveReference: DatabaseReference? =
                postId?.let { userReference.child("saveBlogPosts").child(it) }

            if (postSaveReference != null) {
                postSaveReference.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            binding.postSaveButton.setImageResource(R.drawable.save)
                        }
                        else{
                            binding.postSaveButton.setImageResource(R.drawable.ic_menu_save)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }


            //Handle Save button clicks
            binding.postSaveButton.setOnClickListener{
                if(currentUser != null){
                    if (postId != null) {
                        handleSaveButtonClicked(postId, blogItemModel, binding)
                    }
                }
                else{
                    Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show()
                }

            }


        }

    }



    private fun handleLikedButtonClicked(postId: String, blogItemModel: BlogItemModel, binding: BlogItemBinding) {

        val userReference: DatabaseReference = databaseReference.reference.child("users").child(currentUser!!.uid)
        val postLikeReference: DatabaseReference = databaseReference.reference.child("blogs").child("likes")

        postLikeReference.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               if(snapshot.exists()){
                   userReference.child("likes").child(postId).removeValue().addOnSuccessListener {
                       postLikeReference.child(currentUser.uid).removeValue()
                       blogItemModel.likedBy?.remove(currentUser.uid)
                       updateLikeButtonImage(binding,false)

                       val newLikeCount: Int = blogItemModel.likeCount - 1
                       blogItemModel.likeCount = newLikeCount
                       databaseReference.reference.child("blogs").child(postId).child("likeCount").setValue(newLikeCount)
                       notifyDataSetChanged()
                   }
                       .addOnFailureListener{
                           e -> Log.e("LikedClicked", "onDataChange: Failed to unlike the blog $e",)
                       }
               }
                else{
                    userReference.child("likes").child(postId).setValue(true)
                        .addOnSuccessListener {
                            postLikeReference.child(currentUser.uid).setValue(true)
                            blogItemModel.likedBy?.add(currentUser.uid)
                            updateLikeButtonImage(binding, true)

                            val newLikeCount:Int = blogItemModel.likeCount + 1
                            blogItemModel.likeCount = newLikeCount
                            databaseReference.reference.child("blogs").child(postId).child("likeCount").setValue(newLikeCount)
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener{
                            e ->
                            Log.e("LikedClicked", "onDataChange: Failed to like the blog $e",)
                        }
               }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun updateLikeButtonImage(binding: BlogItemBinding, liked: Boolean) {
        if(liked){
            binding.likeButton.setImageResource(android.R.drawable.btn_star)
        }
        else{
            binding.likeButton.setImageResource(R.drawable.heart)
        }

    }

    private fun handleSaveButtonClicked(
        postId: String,
        blogItemModel: BlogItemModel,
        binding: BlogItemBinding
    ) {
       val userReference: DatabaseReference = databaseReference.reference.child("users").child(currentUser!!.uid)
        userReference.child("saveBlogPosts").child(postId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userReference.child("saveBlogPosts").child(postId).removeValue().addOnSuccessListener {
                        val clickedBlogItem: BlogItemModel?= items.find { it.postId == postId }
                        clickedBlogItem?.isSaved = false
                        notifyDataSetChanged()

                        val context: Context = binding.root.context
                        Toast.makeText(context, "Blog Unsaved!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        val context: Context = binding.root.context
                        Toast.makeText(context,"Failed to unSave The Blog", Toast.LENGTH_SHORT).show()
                    }
                    binding.postSaveButton.setImageResource(R.drawable.save)
                }
                else{
                    userReference.child("saveBlogPosts").child(postId).setValue(true).addOnSuccessListener {
                        val clickedBlogItem: BlogItemModel? = items.find { it.postId == postId }
                        clickedBlogItem?.isSaved = true
                        notifyDataSetChanged()

                        val context: Context = binding.root.context
                        Toast.makeText(context, "Blog Saved!", Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener{
                        val context: Context = binding.root.context
                        Toast.makeText(context, "Failed to save Blog", Toast.LENGTH_SHORT).show()
                    }
                    binding.postSaveButton.setImageResource(R.drawable.save)
                }
                }




            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun updateData(savedBlogsArticles: MutableList<BlogItemModel>) {
        items.clear()
        items.addAll(savedBlogsArticles)
        notifyDataSetChanged()

    }


}