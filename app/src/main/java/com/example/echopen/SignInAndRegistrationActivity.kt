package com.example.echopen

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.echopen.databinding.ActivitySignInAndRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignInAndRegistrationActivity : AppCompatActivity() {
    private val binding:ActivitySignInAndRegistrationBinding by lazy{
        ActivitySignInAndRegistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private val PICK_IMAGE_REQUEST = 1
    private val imageUri: Uri? = null
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_sign_in_and_registration)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        //initialize firebase authentication
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://blog-app-9a56d-default-rtdb.asia-southeast1.firebasedatabase.app")
        storage = FirebaseStorage.getInstance()
        val action:String? = intent.getStringExtra("action")
        if(action == "login"){
            binding.loginEmail.visibility = View.VISIBLE
            binding.loginPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE


            binding.RegisterButton.isEnabled = false
            binding.RegisterButton.alpha = 0.5f
            binding.registerNewHere.isEnabled = false
            binding.registerNewHere.alpha = 0.5f


            binding.registerEmail.visibility = View.GONE
            binding.registerName.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.cardView.visibility = View.GONE

            binding.loginButton.setOnClickListener{
                val loginEmail = binding.loginEmail.text.toString()
                val loginPassword = binding.loginPassword.text.toString()

                if(loginEmail.isEmpty() || loginPassword.isEmpty()){
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                }

                else{
                    auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener {
                            task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))


                        }else{
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()


                        }
                    }
                }
            }


        }
        else if(action == "register"){
            binding.loginButton.isEnabled = false
            binding.loginButton.alpha = 0.5f

            binding.RegisterButton.setOnClickListener{
                Log.d("register", "register button is clicked");
                val registerName = binding.registerName.text.toString()
                val registerEmail = binding.registerEmail.text.toString()
                val registerPassword = binding.registerPassword.text.toString()

                if(registerName.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty()){
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(registerEmail, registerPassword).addOnCompleteListener {
                        task ->
                        if(task.isSuccessful){
                            val user:FirebaseUser? = auth.currentUser
                            auth.signOut()
                            user?.let{
                                val userReference:DatabaseReference = database.getReference("user")
                                val userId: String = user.uid
                                val userData = com.example.echopen.Model.UserData(
                                    registerName,
                                    registerEmail

                                )
                                userReference.child(userId).setValue(userData)
                                //uload image to firebase storage
                                val storageReference: StorageReference = storage.reference.child("profile_image/$userId.jpg")
                                storageReference.putFile(imageUri!!).addOnCompleteListener{
                                    task->
                                    if(task.isSuccessful){
                                        storageReference.downloadUrl.addOnCompleteListener {
                                                imageUri->
                                            if(imageUri.isSuccessful){
                                                val imageUrl: String = imageUri.toString()
                                                //save the image url to the realtime database
                                                userReference.child(userId).child("profileImage").setValue(imageUrl)
//                                                Glide.with(this)
//                                                    .load(imageUri)
//                                                    .apply(RequestOptions.circleCropTransform())
//                                                    .into(binding.registerUserImage)
                                            }
                                    }
                                }
                                }
                                Toast.makeText(this, "User Registration Successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }

                        }else{

                        }
                    }
                }
            }

        }
        // Set an OnClickListener for the Choose image
        binding.cardView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.registerUserImage)
        }
    }

}