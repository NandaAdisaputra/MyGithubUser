package com.nandaadisaputra.github.data.constant

// Kelas Const berisi berbagai konstanta yang digunakan di seluruh aplikasi
class Const {
    // Objek Cons berisi konstanta umum yang digunakan dalam aplikasi
    object Cons {
        const val TAG = "github_app"  // Tag untuk log atau debugging
        const val NULL = "null"  // String yang merepresentasikan null
        const val EMPTY = ""  // String kosong
        const val SEARCH = "Search User..."  // Placeholder untuk kolom pencarian pengguna
        const val DETAIL_USER = "DETAIL USER"  // Teks untuk halaman detail pengguna
        const val FAVORITE_USER = "FAVORITE USER"  // Teks untuk halaman pengguna favorit
    }

    // Objek TOKEN berisi konstanta yang terkait dengan token API
    object TOKEN {
        const val API_TOKEN = "token"  // Nama key untuk token API
    }

    // Objek Constants berisi konstanta yang digunakan untuk pengaturan aplikasi
    object Constants {
        const val THEME_KEY = "theme_key"  // Key untuk menyimpan preferensi tema
        const val DARK_MODE_KEY = "dark_mode_key"  // Key untuk menyimpan preferensi mode gelap
    }

    // Objek Likes berisi konstanta yang berkaitan dengan status suka/tidak suka
    object Likes {
        const val LIKE = "LIKE"  // Status "LIKE"
        const val UN_LIKE = "UN LIKE"  // Status "UN LIKE"
        const val LOADING_LIKE = "Success Add Favorites"  // Pesan ketika berhasil menambahkan favorit
        const val LOADING_UNLIKE = "Success Delete Favorites"  // Pesan ketika berhasil menghapus favorit
    }

    // Objek User berisi konstanta yang terkait dengan data pengguna
    object User {
        const val D_USERNAME = "username"  // Key untuk menyimpan nama pengguna
        const val D_ID = "id"  // Key untuk menyimpan ID pengguna
        const val D_AVATAR = "avatar"  // Key untuk menyimpan URL avatar pengguna
    }
}