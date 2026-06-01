package com.mikeeckels.obdash.update

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import com.mikeeckels.obdash.BuildConfig
import com.mikeeckels.obdash.R
import android.widget.Toast
import androidx.core.content.edit

class UpdateManager(private val activity: AppCompatActivity) {
    private val checker = UpdateChecker()
    private val downloader = UpdateDownloader(activity)
    private val prefs = activity.getSharedPreferences("updates", Context.MODE_PRIVATE)

    private companion object {
        const val PREF_LAST_CHECK = "last_update_check"
        const val CHECK_INTERVAL_MS = 6 * 60 * 60 * 1000L //6 hours
    }

    fun checkForUpdates(force: Boolean = false, manualCheck: Boolean = false) {
        if (!force && !manualCheck && !shouldCheck()) { return }
        prefs.edit { putLong(PREF_LAST_CHECK, System.currentTimeMillis()) }

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
                if (manualCheck || force) {
                    Toast.makeText(activity, R.string.update_failed, Toast.LENGTH_SHORT).show()
                }
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
            //setActionTextColor(activity.getColor(R.color.black))
            setAction(R.string.download) { downloader.downloadApk(downloadUrl, version) }
        }.show()
    }

    private fun shouldCheck(): Boolean {
        val lastCheck = prefs.getLong(PREF_LAST_CHECK, 0L)
        return System.currentTimeMillis() - lastCheck > CHECK_INTERVAL_MS
    }
}