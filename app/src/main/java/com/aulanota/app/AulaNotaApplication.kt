package com.aulanota.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** @HiltAndroidApp activa Hilt y crea el contenedor de dependencias de toda la app. */
@HiltAndroidApp
class AulaNotaApplication : Application()
