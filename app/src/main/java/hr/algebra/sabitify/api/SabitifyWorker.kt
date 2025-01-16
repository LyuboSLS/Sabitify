package hr.algebra.sabitify.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SabitifyWorker(
    private val context: Context,
    parameters: WorkerParameters
) : Worker(context, parameters) {

    override fun doWork(): Result {
        SabitivyFetcher(context).fetchItems()
        return Result.success()
    }
}