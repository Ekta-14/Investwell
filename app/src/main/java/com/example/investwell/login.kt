package com.example.investwell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class login : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN=15
    lateinit var googleSignInClient:GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?)
    {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val dont_have=findViewById<TextView>(R.id.tv_dont_have)
        val emailLogin=findViewById<TextView>(R.id.et_email)
        val passLogin=findViewById<TextView>(R.id.et_pass)
        val btn_login=findViewById<TextView>(R.id.btn_login)

        btn_login.setOnClickListener{
            loginCheck(emailLogin,passLogin)
        }

        setDontHaveClickable(dont_have)

        val signInRequest = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(this,signInRequest)

        auth = Firebase.auth

        val btn_Googlelogin=findViewById<ImageButton>(R.id.btn_google_login)

        btn_Googlelogin.setOnClickListener{loginUser()}
    }


    private fun loginCheck(emailLogin: TextView, passLogin: TextView) {
        val database=UserDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            val userRoom=database.UserDao().getUserThroughEmail(emailLogin.text.toString())

            if(userRoom==null)
                runOnUiThread {
                    Toast.makeText(this@login, "User not registered", Toast.LENGTH_SHORT).show()
                }
            else
                passCheck(emailLogin,passLogin, userRoom)
        }
    }

    private fun passCheck(emailLogin: TextView, passLogin: TextView, userRoom: UserModel) {
        if(userRoom.password==0)
        {
            runOnUiThread{
            Toast.makeText(this,"Log in through email",Toast.LENGTH_SHORT).show()}
        }
        else
        {
            if(userRoom.password==passLogin.text.toString().toInt()) {
                val intent = Intent(this@login, home::class.java)
                intent.putExtra("userEmail", userRoom.email)
                startActivity(intent)
                finish()
            }
            else {
                runOnUiThread {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    //google sign in methods
    private fun loginUser() {

        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)  {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                //google sign in account create hgya, ab authenticate krdo firebase se
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.w("GoogleSignIn", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String)
    {
        auth=FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Sign in success hgya

                    AppPrefrences.init(this)
                    AppPrefrences.putBooleanLogin(PrefConstants.isUserLoggedIn,true)

                    //update the firebase authenticated details in room
                    val userGoogle = auth.currentUser
                    CoroutineScope(Dispatchers.IO).launch {

                        val email=userGoogle?.email.toString()
                        val database=UserDatabase.getDatabase(this@login)
                        val userRoom=database.UserDao().getUserThroughEmail(email)

                        if(userRoom==null) {
                            val name = userGoogle?.displayName.toString()
                            val occupation = "null"
                            val password = 0
                            DatabaseConst.database.UserDao()
                                .insert(UserModel(name, email, occupation, password))
                        }
                        val intent=Intent(this@login,home::class.java)
                        intent.putExtra("userEmail",email)
                        startActivity(intent)
                    }
                }
                else {
                    //sign in fail hgya firebase se
                    Log.w("FirebaseAuth89", "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    //function for spannable text dont have an account
    private fun setDontHaveClickable(dontHave: TextView?) {
        val spannableString=SpannableString(dontHave?.text.toString())
        val clickable=object:ClickableSpan()
        {
            override fun onClick(widget: View) {
                startActivity(Intent(this@login,signup::class.java))
                finish()
            }

        }
        spannableString.setSpan(clickable,23,30,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
        dontHave?.text=spannableString
        dontHave?.movementMethod= LinkMovementMethod.getInstance()
    }
}