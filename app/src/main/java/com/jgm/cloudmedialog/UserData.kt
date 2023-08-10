package com.jgm.cloudmedialog

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.datastore.generated.model.MediaData

object UserData
{
    private const val TAG = "UserData"
    private val _isSignedIn = MutableLiveData<Boolean>(false)
    var isSignedIn: LiveData<Boolean> = _isSignedIn
    fun setSignedIn(newValue : Boolean)
    {
        _isSignedIn.postValue(newValue)
    }
    private val _medias = MutableLiveData<MutableList<Media>>(mutableListOf())
    private fun <T> MutableLiveData<T>.notifyObserver()
    {
        this.postValue(this.value)
    }
    fun notifyObserver()
    {
        this._medias.notifyObserver()
    }
    fun medias() : LiveData<MutableList<Media>>  = _medias
    fun addMedia(m : Media)
    {
        val medias = _medias.value
        if (medias != null)
        {
            medias.add(m)
            _medias.notifyObserver()
        }
        else
        {
            Log.e(TAG, "addMedia : media collection is null !!")
        }
    }
    fun deleteMedia(at: Int) : Media?
    {
        val media = _medias.value?.removeAt(at)
        _medias.notifyObserver()
        return media
    }
    fun resetMedias()
    {
        this._medias.value?.clear()
        _medias.notifyObserver()
    }
    data class Media(val id: String, val name: String, val platform: String, val year: String, val format: String, val rating: String, val playstatus: String, var imageName: String? = null)
    {
        override fun toString(): String = name
        var image : Bitmap? = null
        val data : MediaData
            get() = MediaData.builder()
                .name(this.name)
                .platform(this.platform)
                .year(this.year)
                .format(this.format)
                .rating(this.rating)
                .playstatus(this.playstatus)
                .image(this.imageName)
                .id(this.id)
                .build()
        companion object
        {
            fun from(mediaData : MediaData) : Media
            {
                val result = Media(mediaData.id, mediaData.name, mediaData.platform, mediaData.year, mediaData.format, mediaData.rating, mediaData.playstatus, mediaData.image)
                if (mediaData.image != null)
                {
                    Backend.retrieveImage(mediaData.image!!)
                    {
                        result.image = it
                        with(UserData) { notifyObserver() }
                    }
                }
                return result
            }
        }
    }
}