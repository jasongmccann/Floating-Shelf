package com.jgm.cloudmedialog

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_media_activity.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class AddMediaActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_media_activity)
        addMedia.setOnClickListener {
            val media = UserData.Media(
                UUID.randomUUID().toString(),
                name?.text.toString(),
                platform?.text.toString(),
                year?.text.toString(),
                format?.text.toString(),
                rating?.text.toString(),
                playstatus?.text.toString()
            )

            if (this.mediaImagePath != null)
            {
                media.imageName = UUID.randomUUID().toString()
                media.image = this.mediaImage
                Backend.storeImage(this.mediaImagePath!!, media.imageName!!)
            }
            Backend.createMedia(media)
            UserData.addMedia(media)
            this.finish()
        }
        captureImage.setOnClickListener {
            val i = Intent(
                Intent.ACTION_GET_CONTENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, SELECT_PHOTO)
        }

        takeImage.setOnClickListener {
            val j = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
            startActivityForResult(j, SELECT_PHOTO)
        }

        image.shapeAppearanceModel = image.shapeAppearanceModel
            .toBuilder()
            .build()
    }
    companion object
    {
        private const val TAG = "AddMediaActivity"
        private const val SELECT_PHOTO = 100
    }
    private var mediaImagePath : String? = null
    private var mediaImage : Bitmap? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        when (requestCode)
        {
            SELECT_PHOTO -> if (resultCode == RESULT_OK)
            {
                val selectedImageUri: Uri? = imageReturnedIntent!!.data
                var imageStream: InputStream? =
                    contentResolver.openInputStream(selectedImageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val ivPreview: ImageView = findViewById<View>(R.id.image) as ImageView
                ivPreview.setImageBitmap(selectedImage)
                this.mediaImage = selectedImage
                imageStream = contentResolver.openInputStream(selectedImageUri)
                val tempFile = File.createTempFile("image", ".image")
                copyStreamToFile(imageStream!!, tempFile)
                this.mediaImagePath = tempFile.absolutePath
            }
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File)
    {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                while (true)
                {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
                output.close()
            }
        }
    }
}