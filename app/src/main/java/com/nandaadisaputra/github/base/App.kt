// Kelas App adalah kelas utama aplikasi yang mengextends kelas Application.
// Kelas ini digunakan untuk menginisialisasi aplikasi dan menyediakan konteks global bagi seluruh aplikasi.
package com.nandaadisaputra.github.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Menandai kelas App sebagai kelas aplikasi yang menggunakan Hilt untuk dependency injection.
@HiltAndroidApp
class App: Application() {
    // Kelas ini berfungsi sebagai titik awal untuk Hilt yang akan mengelola dependency injection secara otomatis di seluruh aplikasi.
}
