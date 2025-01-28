package hr.algebra.sabitify

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import hr.algebra.sabitify.databinding.ActivityRegistrationBinding
import hr.algebra.sabitify.framework.startActivity

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var emailTextView: EditText? = null
    private var passwordTextView: EditText? = null
    private var btn: Button? = null
    private var progressbar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root) // Set the l


        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance()


        // initialising all views through id defined above
        emailTextView = findViewById(R.id.etEmail)
        passwordTextView = findViewById(R.id.etPassword)
        btn = findViewById(R.id.btnRegister)


        // Set on Click Listener on Registration button
        btn!!.setOnClickListener(View.OnClickListener { registerNewUser() })
    }

    private fun registerNewUser() {
        // show the visibility of progress bar


        // Take the value of two edit texts in Strings
        val email = emailTextView!!.text.toString()
        val password = passwordTextView!!.text.toString()


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter email!!",
                Toast.LENGTH_LONG
            )
                .show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                applicationContext,
                "Please enter password!!",
                Toast.LENGTH_LONG
            )
                .show()
            return
        }


        // create new user or register new user
        mAuth
            ?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Registration successful!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    startActivity<HostActivity>()
                } else {
                    // Registration failed

                    Toast.makeText(
                        applicationContext,
                        "Registration failed!!"
                                + " Please try again",
                        Toast.LENGTH_LONG
                    )
                        .show()


                }
            }
    }
}