package com.example.gallery

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){

    private var imageRecycler:RecyclerView?=null
    private var progressBar: ProgressBar?=null
    private var allPictures:ArrayList<Image>?=null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        val zdj = addPicture()
        Log.d("CHUJ",zdj.toString())
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

        imageRecycler=findViewById(R.id.image_recycler)
        progressBar=findViewById(R.id.recycler_progress)

        imageRecycler?.layoutManager=GridLayoutManager(this,3)
        imageRecycler?.setHasFixedSize(true)

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101
            )
        }
        allPictures= ArrayList()
        if(allPictures!!.isEmpty())
        {
            progressBar?.visibility= View.VISIBLE
            allPictures=getAllImages()
            imageRecycler?.adapter=ImageAdapter(this,allPictures!!)
            progressBar?.visibility=View.GONE

        }

    }

    private fun getAllImages(): ArrayList<Image>? {
        val zdj = addPicture()
        Log.d("CHUJ",zdj.toString())
        val images=ArrayList<Image>()

        val allImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media.DISPLAY_NAME)

        var cursor=this@MainActivity.contentResolver.query(allImageUri,projection,null,null,null)

        try {
            cursor!!.moveToFirst()
            do{
                val image=Image()
                image.imagePath=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                images.add(image)
            }while(cursor.moveToNext())
            cursor.close()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }
        return images
    }

    private fun addPicture(): Uri?
    {
        val resolver = applicationContext.contentResolver
        val audioCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        val newPictureDetails = ContentValues().apply{
            put(MediaStore.Images.Media.DISPLAY_NAME, "corgi.jpg")
        }

        val myImageUri = resolver.insert(audioCollection, newPictureDetails)
        return myImageUri
    }
}