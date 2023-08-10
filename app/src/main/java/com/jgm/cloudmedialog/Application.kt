package com.jgm.cloudmedialog

import android.app.Application

class CloudMediaLog: Application()
{
    override fun onCreate()
    {
        super.onCreate()
        Backend.initialize(applicationContext)
    }
}