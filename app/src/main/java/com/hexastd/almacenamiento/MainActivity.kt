package com.hexastd.almacenamiento

import android.content.Context
import android.os.Bundle
import java.io.File
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hexastd.almacenamiento.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnGetSandboxPath.setOnClickListener {
            val path = filesDir.absolutePath
            binding.tvSandboxPath.text = "Ruta absoluta:\n$path"
        }

        binding.btnWritePrivateFile.setOnClickListener {
            val filename = "datos_usuario.txt"
            val fileContents = binding.etFileContent.text.toString()
            try {
                openFileOutput(filename, Context.MODE_PRIVATE).use { output ->
                    output.write(fileContents.toByteArray())
                }
                binding.tvPrivateFilesOutput.text = "Guardado correctamente en $filename"
            } catch (e: Exception) {
                binding.tvPrivateFilesOutput.text = "Error: ${e.message}"
            }
        }

        binding.btnReadPrivateFile.setOnClickListener {
            val filename = "datos_usuario.txt"
            try {
                openFileInput(filename).bufferedReader().use { reader ->
                    val text = reader.readText()
                    binding.tvPrivateFilesOutput.text = text
                }
            } catch (e: Exception) {
                binding.tvPrivateFilesOutput.text = "Error: ${e.message}"
            }
        }

        binding.btnListSandboxFiles.setOnClickListener {
            try {
                val arrayArchivos = fileList()
                if (arrayArchivos.isEmpty()) {
                    binding.tvPrivateFilesOutput.text = "No hay archivos"
                } else {
                    val builder = StringBuilder()
                    arrayArchivos.forEach { filename ->
                        builder.append(filename).append("\n")
                    }
                    binding.tvPrivateFilesOutput.text = builder.toString().trim()
                }
            } catch (e: Exception) {
                binding.tvPrivateFilesOutput.text = "Error: ${e.message}"
            }
        }

        binding.btnCreateCacheFile.setOnClickListener {
            try {
                val cacheFile = File.createTempFile("session_tmp", ".dat", cacheDir)
                cacheFile.writeText("ID_Sesion: 0x99283")
                binding.tvCacheOutput.text = "Temporal: ${cacheFile.name}\nRuta: ${cacheFile.absolutePath}"
            } catch (e: Exception) {
                binding.tvCacheOutput.text = "Error: ${e.message}"
            }
        }
    }
}