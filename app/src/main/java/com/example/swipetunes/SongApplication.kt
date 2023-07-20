package com.example.swipetunes

import android.app.Application

class SongApplication : Application() {
    val db by lazy { AppDatabase.getInstance(this) }
}