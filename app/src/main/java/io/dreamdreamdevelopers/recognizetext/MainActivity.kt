package io.dreamdreamdevelopers.recognizetext

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText

class MainActivity : AppCompatActivity() {
    lateinit var img : ImageView
    lateinit var btnSelectImage : Button
    lateinit var btnRecognizeText : Button
    lateinit var txtDisplay : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img = findViewById(R.id.img)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnRecognizeText = findViewById(R.id.btnRecognize)
        txtDisplay = findViewById(R.id.txtdisplay)

        btnSelectImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Image"),1)
        }

        btnRecognizeText.setOnClickListener {
            val bitmap = (img.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val recognize = FirebaseVision.getInstance().onDeviceTextRecognizer
            txtDisplay?.setText(" ")
            recognize.processImage(image).addOnSuccessListener { firebaseVisionText ->
                RecognizeText(firebaseVisionText)

            }.addOnFailureListener{
                txtDisplay?.setText("Failed to Recognize Text")
            }
        }
    }

    private  fun RecognizeText(resulttext : FirebaseVisionText){
        if(resulttext.textBlocks.size == 0){
            txtDisplay?.setText("Data Not Found")
            return
        }
        for( block in resulttext.textBlocks){
            val text = block.text
            txtDisplay?.setText(txtDisplay!!.text.toString() + text )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            if(data != null){
                img.setImageURI(data!!.data)
            }
        }
    }
}