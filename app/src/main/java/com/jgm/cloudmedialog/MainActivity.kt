package com.jgm.cloudmedialog

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21)
        {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupRecyclerView(item_list)
        setupAuthButton(UserData)
        UserData.isSignedIn.observe(this, Observer<Boolean> { isSignedUp ->
            if (isSignedUp)
            {
                fabAuth.setImageResource(R.drawable.ic_baseline_logout)
                fabAdd.show()
                mediaList.isVisible = true
                mediaList.isInvisible = false
                imageView.isVisible = false
                imageView.isInvisible = true
                label.text="My Collection"

            }
            else
            {
                fabAuth.setImageResource(R.drawable.ic_baseline_login)
                fabAdd.hide()
                mediaList.isVisible = false
                mediaList.isInvisible = true
                imageView.isVisible = true
                imageView.isInvisible = false
                label.text = "                 Log In"
            }
        })
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddMediaActivity::class.java))
        }
    }
    private fun setupRecyclerView(recyclerView: RecyclerView)
    {
        UserData.medias().observe(this, Observer<MutableList<UserData.Media>>
        {medias ->
            recyclerView.adapter = MediaRecyclerViewAdapter(medias)
        })
        val itemTouchHelper = ItemTouchHelper(SwipeCallback(this))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    companion object
    {
        private const val TAG = "MainActivity"
    }
    private fun setupAuthButton(userData: UserData)
    {
        fabAuth.setOnClickListener { view ->
            val authButton = view as ImageButton
            if (userData.isSignedIn.value!!)
            {
                authButton.setImageResource(R.drawable.ic_baseline_logout)
                mediaList.isVisible = true
                mediaList.isInvisible = false
                Backend.signOut()
            }
            else
            {
                authButton.setImageResource(R.drawable.ic_baseline_logout)
                mediaList.isVisible = true
                mediaList.isInvisible = false
                Backend.signIn(this)
            }
        }
    }
}