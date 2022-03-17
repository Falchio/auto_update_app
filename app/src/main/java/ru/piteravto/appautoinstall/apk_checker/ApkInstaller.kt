package ru.piteravto.appautoinstall.apk_checker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import ru.piteravto.appautoinstall.App
import ru.piteravto.appautoinstall.BuildConfig
import java.io.File

object ApkInstaller {

    fun install(apkFile: File, activity: Activity) {
        val currentVersion = BuildConfig.VERSION_CODE
        val apkVersion = getApkVersion(apkFile)
        if (currentVersion < apkVersion) {
            val packageManager = App.context.packageManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!packageManager.canRequestPackageInstalls()) {
                    val context = App.context
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    activity.startActivityForResult(intent, 1234)
                    return
                }
            }
            //необходимо использовать FileProvider
            installApk(apkFile)
        }
    }

    private fun installApk(apkFile: File) {
        val type = "application/vnd.android.package-archive"
        val uri: Uri = getFileUri(apkFile)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setupInstallIntent(uri, type)
        App.context.startActivity(intent)
    }

    private fun Intent.setupInstallIntent(uri: Uri, type: String) {
        setDataAndType(uri, type)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    private fun getFileUri(apkFile: File): Uri {
        val context = App.context
        val data: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context, "${context.packageName}.provider", apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }
        return data
    }

    private fun getApkVersion(apkFile: File): Int {
        val packageManager = App.context.packageManager
        val flag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.content.pm.PackageManager.INSTALL_REASON_USER
        } else {
            0
        }
        val info: PackageInfo? = packageManager.getPackageArchiveInfo(apkFile.absolutePath, flag)
        return info?.versionCode ?: 0
    }
}