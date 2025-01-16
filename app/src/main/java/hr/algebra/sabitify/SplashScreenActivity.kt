package hr.algebra.sabitify

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.nasa.framework.applyAnimation
import hr.algebra.nasa.framework.callDelayed
import hr.algebra.nasa.framework.getBooleanPreference
import hr.algebra.nasa.framework.isOnline
import hr.algebra.nasa.framework.startActivity
import hr.algebra.sabitify.api.SabitifyWorker
import hr.algebra.sabitify.databinding.ActivitySplashScreenBinding


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
        if (getBooleanPreference(DATA_IMPORTED)) {
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
}
