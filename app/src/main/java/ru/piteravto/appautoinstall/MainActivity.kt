package ru.piteravto.appautoinstall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.piteravto.appautoinstall.apk_checker.ApkInstaller
import ru.piteravto.appautoinstall.databinding.ActivityMainBinding
import java.io.File

//TODO: протестировать установку приложения самого себя
//TODO: протестировать загрузку файлов apk по ссылке
//TODO: получать название загруженного файла
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = BuildConfig.VERSION_NAME
    }

    override fun onResume() {
        super.onResume()
        loadFile()
    }

    private fun loadFile() {
        val destination = getFilesUrl()
//        Downloader.downloadFileByLink(url, destination)
        val version = ApkInstaller.install(destination, this)
    }

    private fun getFilesUrl(): File {
        val cacheDir = File(this.filesDir, "apk")
        if (!cacheDir.exists()) cacheDir.mkdir()
        val targetFile = File(cacheDir, "apk.apk")
        if (!targetFile.exists()) targetFile.createNewFile()
        return targetFile
    }
}