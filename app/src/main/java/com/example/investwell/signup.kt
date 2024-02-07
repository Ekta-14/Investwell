package com.example.investwell

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class signup : AppCompatActivity()
{
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        //window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val already_have=findViewById<TextView>(R.id.tv_already_have)
        setAlreadyHaveLoggedIn(already_have)

        val name=findViewById<EditText>(R.id.et_name)
        val email=findViewById<EditText>(R.id.et_email)
        val occupation=findViewById<EditText>(R.id.et_occupation)
        val pass=findViewById<EditText>(R.id.et_pass)
        val btn_signUp=findViewById<Button>(R.id.btn_sign_up)
        val day=findViewById<NumberPicker>(R.id.numberPickerDay)
        val month=findViewById<NumberPicker>(R.id.numberPickerMonth)
        val year=findViewById<NumberPicker>(R.id.numberPickerYear)


        btn_signUp.setOnClickListener { updateUserInDatabase(name,email,occupation,pass) }
    }

    private fun updateUserInDatabase(
        name: EditText?,
        email: EditText?,
        occupation: EditText?,
        pass: EditText
    ) {
        val name=name?.text.toString()
        val email=email?.text.toString()
        val occupation=occupation?.text.toString()
        val password=pass?.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val userRoom=DatabaseConst.database.UserDao().getUserThroughEmail(email)
            if(userRoom!=null)
            {
                runOnUiThread{
                    Toast.makeText(this@signup,"User already registered, Please login", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                DatabaseConst.database.UserDao().insert(UserModel(name, email, occupation, password.toInt(),))
                startActivity(Intent(this@signup, home::class.java).putExtra("userEmail", email))
            }
        }
    }

    private fun setAlreadyHaveLoggedIn(alreadyHave: TextView?) {
        val spannableString=SpannableString(alreadyHave?.text.toString())
        val clickableSpan=object:ClickableSpan(){
            override fun onClick(widget: View) {
                startActivity(Intent(this@signup,login::class.java))
                finish()
            }
        }
        spannableString.setSpan(clickableSpan,25,30,SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
        alreadyHave?.text=spannableString
        alreadyHave?.movementMethod= LinkMovementMethod.getInstance()
    }
}