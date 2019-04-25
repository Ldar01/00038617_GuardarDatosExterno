package com.example.a00038617_tareaalmacenamiento

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import android.util.Log.*
import java.io.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_ID_READ_PERMISSION = 100
    private val REQUEST_ID_WRITE_PERMISSION = 200

    private val fileName = "prueba.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton_guardar.setOnClickListener {
            askPermissionAndWriteFile()
        }
        boton_leer.setOnClickListener {
            askPermissionAndReadFile()
        }
    }

    fun askPermissionAndWriteFile() {
        val canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (canWrite) {
            this.writeFile()
        }
    }

    fun askPermissionAndReadFile() {
        val canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        if (canRead) {
            this.readFile()
        }
    }

    fun askPermission(requestId: Int, permissionName: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            val permission = ActivityCompat.checkSelfPermission(this, permissionName)


            if (permission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(
                        arrayOf(permissionName),
                        requestId
                )
                return false
            }
        }
        return true
    }

    private fun writeFile() {

        val extStore = Environment.getExternalStorageDirectory()

        val path = extStore.getAbsolutePath() + "/" + fileName
        i("ExternalStorageDemo", "Save to: $path")

        val datos = data.getText().toString()

        try {
            val myFile = File(path)
            myFile.createNewFile()
            val fOut = FileOutputStream(myFile)
            val myOutWriter = OutputStreamWriter(fOut)
            myOutWriter.append(datos)
            myOutWriter.close()
            fOut.close()

            Toast.makeText(applicationContext, fileName + " saved", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            when (requestCode) {
                REQUEST_ID_READ_PERMISSION -> {
                    run {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            readFile()
                        }
                    }
                    run {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            writeFile()
                        }
                    }
                }
                REQUEST_ID_WRITE_PERMISSION -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        writeFile()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "Permission Cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFile() {

        val extStore = Environment.getExternalStorageDirectory()
        val path = extStore.getAbsolutePath() + "/" + fileName
        i("ExternalStorageDemo", "Read file: $path")

        var linea = ""
        var fileContenido = ""
        try {
            val myFile = File(path)
            val fIn = FileInputStream(myFile)

            fileContenido = fIn.bufferedReader().use(BufferedReader::readText)

            contenido.text = fileContenido
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Toast.makeText(applicationContext, fileContenido, Toast.LENGTH_LONG).show()
    }
}
