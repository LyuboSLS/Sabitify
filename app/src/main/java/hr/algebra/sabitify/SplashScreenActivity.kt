package hr.algebra.sabitify

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.sabitify.api.SabitifyWorker
import hr.algebra.sabitify.databinding.ActivitySplashScreenBinding
import hr.algebra.sabitify.framework.applyAnimation
import hr.algebra.sabitify.framework.getBooleanPreferences
import hr.algebra.sabitify.framework.isOnline
import hr.algebra.sabitify.framework.startActivity
import kotlinx.coroutines.Runnable

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.sabitify.data_imported"
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
        if (getBooleanPreferences(DATA_IMPORTED)) {
            callDelayed(DELAY) {
                startActivity<HostActivity>()
            }
        } else {
            if (isOnline()) {
                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(SabitifyWorker::class.java)
                    )
                }
            } else {
                binding.tvSplash.text = getString(R.string.no_internet_connection)
                callDelayed(DELAY) {
                    finish()
                }
            }


        }
    }

    fun callDelayed(time: Long, work: Runnable) {
        Handler(Looper.getMainLooper()).postDelayed(
            work,
            time
        )
    }
}
