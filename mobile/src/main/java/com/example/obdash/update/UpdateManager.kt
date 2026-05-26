package com.example.obdash.update

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.example.obdash.BuildConfig
import com.example.obdash.R
import android.widget.Toast

class UpdateManager(private val activity: AppCompatActivity) {
    private val checker = UpdateChecker()
    private val downloader = UpdateDownloader(activity)

    fun checkForUpdates(force: Boolean = false, manualCheck: Boolean = false) {
        activity.lifecycleScope.launch {
            val result = checker.fetchLatestRelease()

            result.onSuccess { updateInfo ->
                val newAvailable = checker.isNewAvailable(current = BuildConfig.VERSION_NAME, latest = updateInfo.version)

                if (force || newAvailable) {
                    showUpdateSnackbar(updateInfo.downloadUrl, updateInfo.version)
                }else if (manualCheck) {
                    Toast.makeText(activity, R.string.no_updates, Toast.LENGTH_SHORT).show()
                }
            }

            result.onFailure {
                Toast.makeText(activity, R.string.update_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateSnackbar(downloadUrl: String, version: String) {
        Snackbar.make(
            activity.findViewById(android.R.id.content),
            R.string.new_version,
            Snackbar.LENGTH_LONG
        ).apply {
            duration = 10_000
            setActionTextColor(activity.getColor(R.color.black))
            setAction(R.string.download) { downloader.downloadApk(downloadUrl, version) }
        }.show()
    }
}