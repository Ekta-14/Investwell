package com.example.investwell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class home : AppCompatActivity()
{
    //lateinit var btn_sign_out:Button
    val fragmentManager=supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?)
    {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val lastClickedFragmentId=AppPrefrences.getLastClickedFragmentId()

        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        if(lastClickedFragmentId==-1) {
            supportFragmentManager.beginTransaction().add(R.id.container,Fragment1.newInstance()).commit()
            bottom_nav.selectedItemId=R.id.nav_fragment1
        }
        else
        {
            when(lastClickedFragmentId) {
                0 -> {
                    bottom_nav.selectedItemId = R.id.nav_fragment1
                    supportFragmentManager.beginTransaction()
                        .add(R.id.container, Fragment1.newInstance()).commit()
                }

                1 -> {
                bottom_nav.selectedItemId = R.id.nav_fragment2
                        supportFragmentManager . beginTransaction ().add(
                    R.id.container,
                    Fragment2.newInstance()
                ).commit()
            }
            }
        }

        bottom_nav.setOnItemSelectedListener { menuItem ->

            val navController=Navigation.findNavController(this,R.id.container)

            when (menuItem.itemId) {

                R.id.nav_fragment1 -> {
                    navController.navigate(R.id.action_fragment1_to_fragment2)
                    //inflateFragment(Fragment1.newInstance())
                    //AppPrefrences.saveLastClickedFragment(Fragment1.newInstance().id)
                }
                R.id.nav_fragment2 -> {
                    navController.navigate(R.id.action_fragment2_to_fragment1)
                    //inflateFragment(Fragment2.newInstance())
                  //  AppPrefrences.saveLastClickedFragment(Fragment2.newInstance().id)
                }
            }
            true
        }
    }


//    private fun inflateFragment(fragment: Fragment) {
//
//        val transaction=fragmentManager.beginTransaction()
//        transaction.add(R.id.container,fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }


    //if you dont want to use addToBackStack(null) then use this function



//        val tv_name_display=findViewById<TextView>(R.id.tv_name_display)
//        val email=findViewById<TextView>(R.id.tv_email_display)
//        val occupation=findViewById<TextView>(R.id.tv_occupation_display)
//        btn_sign_out=findViewById(R.id.btn_sign_out)
//
//
//        val emailRecieved =intent.getStringExtra("userEmail")
//
//        CoroutineScope(Dispatchers.IO).launch {
//
//            if (emailRecieved != null) {
//                val user=DatabaseConst.database.UserDao().getUserThroughEmail(emailRecieved)
//                tv_name_display.text=user?.name.toString()
//                email.text=user?.email.toString()
//                occupation.text=user?.occupation.toString()
//            }
//        }
//
//        btn_sign_out.setOnClickListener { signOutUser() }


  //  }


    //methods for signing out the user
    private fun signOutUser()
    {
        FirebaseAuth.getInstance().signOut()
        revokeAccess()
        startActivity(Intent(this, login::class.java))
        finish()
    }
    private fun revokeAccess() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                AppPrefrences.init(this)
                AppPrefrences.putBooleanLogin(PrefConstants.isUserLoggedIn,false)
            }
    }
}