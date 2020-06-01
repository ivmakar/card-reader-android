package com.example.cardreader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cardreader.data.Request
import com.example.cardreader.data.Response
import com.example.cardreader.databinding.FragmentScanBinding
import kotlinx.android.synthetic.main.fragment_scan.*
import retrofit2.Call
import retrofit2.Callback
import java.io.ByteArrayOutputStream


class ScanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentScanBinding>(inflater, R.layout.fragment_scan, container, false)

        val viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        binding.viewModel = viewModel

        binding.scan.setOnClickListener {
            pickFromGallery()
        }

        binding.select.setOnClickListener {
            if (viewModel.imageUri.get() == null) {
                Toast.makeText(requireContext(), "Выберите фотографию визитки", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            progressbar.visibility = View.VISIBLE
            //Convert to Base64
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, viewModel.imageUri.get())

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()


            viewModel.networkApi.scanCard(Request(Base64.encodeToString(byteArray, Base64.DEFAULT))).enqueue(object :
                Callback<Response> {
                override fun onFailure(call: Call<Response>?, t: Throwable?) {
                    Toast.makeText(requireContext(), "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show()
                    progressbar.visibility = View.GONE
                }

                override fun onResponse(call: Call<Response>?, response: retrofit2.Response<Response>?) {
                    var text = ""
                    if (response != null && response.isSuccessful && response.body() != null) {
                        viewModel.splitedText = response.body()!!.text
                        response.body()!!.text.forEach { text += it + "\n" }
                        viewModel.text.set(text)
                        scanData()
                        progressbar.visibility = View.GONE
                    } else {
                        Toast.makeText(requireContext(), "Неверный ответ сервера", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        binding.email.setOnClickListener {
            if (viewModel.email.get().isNullOrEmpty()) {
                return@setOnClickListener
            }
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", viewModel.email.get(), null
                )
            )
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }

        binding.phone.setOnClickListener {
            if (viewModel.phone.get().isNullOrEmpty()) {
                return@setOnClickListener
            }
            val intent =
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + viewModel.phone.get()))
            startActivity(intent)
        }

        binding.site.setOnClickListener {
            if (viewModel.site.get().isNullOrEmpty()) {
                return@setOnClickListener
            }
            val url = viewModel.site.get()
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        return binding.root
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
        startActivityForResult(intent, MainActivity.GALLERY_REQUEST_CODE)
    }

    fun scanData() {
        val viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        val email = MainActivity.EMAIL_REGEX.toRegex().find(viewModel.text.get().toString())
        if (email != null && !email.value.isNullOrEmpty()) {
            viewModel.email.set(email.value)
        } else {
            viewModel.email.set("")
        }

        val phone = MainActivity.PHONE_REGEX.toRegex().findAll(viewModel.text.get().toString())
        viewModel.phone.set("")
        for (i in phone)
            if (!i.value.isNullOrEmpty()) {
                viewModel.phone.set(viewModel.phone.get() + "\n" + i.value)
            }
        viewModel.phone.set(viewModel.phone.get()?.trim())

        val site = MainActivity.SITE_REGEX.toRegex().find(viewModel.text.get().toString().toLowerCase())
        if (site != null && !site.value.isNullOrEmpty()) {
            viewModel.site.set(site.value)
        } else {
            viewModel.site.set("")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("OnActivityReesult", "fragment ${resultCode == Activity.RESULT_OK}")

        val viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                MainActivity.GALLERY_REQUEST_CODE -> {
                    val selectedImage: Uri? = data?.data
                    viewModel.imageUri.set(selectedImage)
                    Log.d("OnActivityReesult", "fragment ${viewModel.imageUri.get()}")
                }
            }
    }
}