package hr.algebra.sabitify

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import hr.algebra.sabitify.databinding.ActivitySplashScreenBinding
import hr.algebra.sabitify.framework.applyAnimation
import hr.algebra.sabitify.framework.startActivity

private const val DELAY = 3000L
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()
        redirect()
    }

    private fun startAnimation() {
        binding.ivSplash.applyAnimation(R.anim.comein)
        binding.tvSplash.applyAnimation(R.anim.comein)
    }


    private fun redirect() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity<HostActivity>()
            },
            DELAY
        )

    }
}