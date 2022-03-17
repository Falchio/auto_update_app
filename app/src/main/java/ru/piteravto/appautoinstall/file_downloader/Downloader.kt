package ru.piteravto.appautoinstall.file_downloader

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object Downloader {
    private val scope = CoroutineScope(Dispatchers.IO + Job())

    fun downloadFileByLink(link: String, contentFile: File) {
        scope.launch(Dispatchers.IO) {
            download(link, contentFile)
        }
    }

    @Synchronized
    private fun download(link: String, contentFile: File) {
        try {
            URL(link).openStream().use { inputStream ->
                contentFile.createNewFile()
                val fileOutput = FileOutputStream(contentFile)
                fileOutput.use {
                    it.write(inputStream.readBytes())
                    it.flush()
                }
            }
//            ApkInstaller.install(contentFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}