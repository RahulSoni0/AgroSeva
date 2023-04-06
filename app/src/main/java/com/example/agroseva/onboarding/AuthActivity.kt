package com.example.agroseva.onboarding

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agroseva.R
import com.example.agroseva.databinding.ActivityAuthBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class AuthActivity : AppCompatActivity() {
    var AUTHUI_REQUEST_CODE = 10001
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }
        binding.btnEmailSignIn.setOnClickListener { view -> handleEmailLogin(view) }
        binding.btnGoogleSignIn.setOnClickListener { view -> handleGoogleLogin(view) }

        binding.btnPhoneNoSignIn.setOnClickListener { view -> handlePhoneLogin(view) }


    }

    private fun handlePhoneLogin(view: View?) {
        val providers = Arrays.asList(
            PhoneBuilder().build()
        )
        val intent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setLogo(R.drawable.panda_meditation).setAlwaysShowSignInMethodScreen(false)
                .setTheme(R.style.YellowTheme).setIsSmartLockEnabled(false).build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    private fun handleGoogleLogin(view: View?) {

        val providers = Arrays.asList(
            GoogleBuilder().build()
        )
        val intent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setLogo(R.drawable.panda_meditation).setAlwaysShowSignInMethodScreen(false)
                .setIsSmartLockEnabled(false).setTheme(R.style.BlueTheme).build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)

    }

    private fun handleEmailLogin(view: View?) {

        val providers = Arrays.asList(EmailBuilder().build())
        val intent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setLogo(R.drawable.panda_meditation).setAlwaysShowSignInMethodScreen(false)
                .setTheme(R.style.GreenTheme).setIsSmartLockEnabled(false).build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    //    public void handleLoginRegister(View view) {
    //
    //        List<AuthUI.IdpConfig> providers = Arrays.asList(
    //                new AuthUI.IdpConfig.EmailBuilder().build(),
    //                new AuthUI.IdpConfig.GoogleBuilder().build(),
    //                new AuthUI.IdpConfig.PhoneBuilder().build()
    //        );
    //
    //        Intent intent = AuthUI.getInstance()
    //                .createSignInIntentBuilder()
    //                .setAvailableProviders(providers)
    //                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
    //                .setLogo(R.drawable.panda_meditation)
    //                .setAlwaysShowSignInMethodScreen(true)
    //                .setIsSmartLockEnabled(false)
    //                .build();
    //
    //        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    //    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(ContentValues.TAG, "onActivityResult: " + user.toString())
                Toast.makeText(this, "Singin Successfull", Toast.LENGTH_SHORT).show()
                //Checking for User (New/Old)
                if (user!!.metadata!!.creationTimestamp == user.metadata!!.lastSignInTimestamp) {
                    //This is a New User
                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show()
                } else {
                    //This is a returning user
                    Toast.makeText(this, "Welcome back ", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, OnboardingActivity::class.java)
                intent.putExtra("userKey", user.uid);
                startActivity(intent)
                finish()
            } else {
                // Signing in failed
                val response = IdpResponse.fromResultIntent(data)
                if (response == null) {
                    Log.d(
                        ContentValues.TAG,
                        "onActivityResult: the user has cancelled the sign in request"
                    )
                } else {
                    Log.e(ContentValues.TAG, "onActivityResult: ", response.error)
                }
            }
        }
    }
}