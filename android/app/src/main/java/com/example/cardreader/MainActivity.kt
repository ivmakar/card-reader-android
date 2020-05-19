package com.example.cardreader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel : MainViewModel by viewModel()

        button.setOnClickListener {
            pickFromGallery()
        }

        get.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            GlobalScope.launch {
                val resp = viewModel.get()
                withContext(Dispatchers.Main) {
                    var text = ""
                    resp.image.forEach { text += it + "\n" }
                    textView.text = text
                    progressbar.visibility = View.GONE
                }
            }
        }
    }

    private fun pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val viewModel : MainViewModel by viewModel()
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

                    progressbar.visibility = View.VISIBLE
                    //data.getData returns the content URI for the selected Image

                    val selectedImage: Uri? = data?.data
                    imageView.setImageURI(selectedImage)

                    GlobalScope.launch {
                        //Convert to Base64
                        val bitmap = MediaStore.Images.Media.getBitmap(this@MainActivity.contentResolver, selectedImage)

                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

                        val resp = viewModel.scanCard(Base64.encodeToString(byteArray, Base64.DEFAULT))

                        withContext(Dispatchers.Main) {
                            var text = ""
                            resp.image.forEach { text += it + "\n" }
                            textView.text = text
                            progressbar.visibility = View.GONE
                        }
                    }
                }
        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 1
    }
}
