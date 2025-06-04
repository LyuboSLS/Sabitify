package hr.algebra.sabitify

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.sabitify.databinding.ActivityLoginBinding
import hr.algebra.sabitify.framework.startActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var btn: Button
    private lateinit var register: TextView

    private lateinit var binding: ActivityLoginBinding // Update this to match your layout file

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the layout to activity_login.xml

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()

        // Initialize views using ViewBinding
        emailTextView = binding.etEmail
        passwordTextView = binding.etPassword
        btn = binding.btnLogin

        // Set onClickListener for the login button
        btn.setOnClickListener {
            loginUserAccount()
        }
        register = binding.tvRegister
        register.setOnClickListener {
            startActivity<RegistrationActivity>()
        }
    }

    private fun loginUserAccount() {
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        // Validate input fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email!!", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!!", Toast.LENGTH_LONG).show()
            return
        }

        // Authenticate user with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!!", Toast.LENGTH_LONG)
                        .show()
                    startActivity<SplashScreenActivity>()
                } else {
                    Toast.makeText(applicationContext, "Login failed!!", Toast.LENGTH_LONG).show()
                }
            }

    }
}