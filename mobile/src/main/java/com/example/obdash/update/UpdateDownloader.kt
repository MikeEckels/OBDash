package com.example.obdash.update

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.example.obdash.R
import androidx.core.net.toUri

class UpdateDownloader(private val activity: AppCompatActivity) {
    private var receiverRegistered = false
    private var currentDownloadId: Long = -1L

    private val downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L) ?: return
                if (downloadId != currentDownloadId) { return }

                showInstallDialog(downloadId)
                unregisterReceiver()
            }
        }

    fun downloadApk(downloadUrl: String, version: String) {
        registerReceiver()
        val fileName = "${activity.getString(R.string.app_name)}-v$version.apk"
        val request = DownloadManager.Request(downloadUrl.toUri()).apply {
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setMimeType("application/vnd.android.package-archive")
            setTitle(activity.getString(R.string.app_name))
            setDescription(activity.getString(R.string.new_version))
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val downloadManager = ContextCompat.getSystemService(activity, DownloadManager::class.java) ?: return

        currentDownloadId = downloadManager.enqueue(request)
        Toast.makeText(activity, R.string.downloading, Toast.LENGTH_SHORT).show()
    }

    private fun registerReceiver() {
        if (receiverRegistered) { return }

        ContextCompat.registerReceiver(
            activity,
            downloadReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
            ContextCompat.RECEIVER_EXPORTED
        )

        receiverRegistered = true
    }

    private fun unregisterReceiver() {
        if (!receiverRegistered) { return }

        try {
            activity.unregisterReceiver(downloadReceiver)
        } catch (_: Exception) { }

        receiverRegistered = false
    }

    private fun showInstallDialog(downloadId: Long) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.download_complete)
            .setMessage(R.string.install_dialog)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int -> installApk(downloadId) }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }.show()
    }

    private fun installApk(downloadId: Long) {
        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = downloadManager.getUriForDownloadedFile(downloadId) ?: return
        val installIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        installIntent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        installIntent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, "com.android.vending")
        activity.startActivity(installIntent)
    }
}