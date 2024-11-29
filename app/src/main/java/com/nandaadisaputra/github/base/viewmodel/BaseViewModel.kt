// Kelas BaseViewModel merupakan turunan dari CoreViewModel.
// Kelas ini berfungsi sebagai kelas dasar bagi ViewModel lain dengan fungsionalitas umum terkait API seperti logout dan renew token.
package com.nandaadisaputra.github.base.viewmodel

import com.crocodic.core.base.viewmodel.CoreViewModel

// Kelas ini merupakan kelas ViewModel dasar yang mengextends CoreViewModel.
// Kelas ini akan mengimplementasikan fungsionalitas umum terkait manajemen API, yang bisa digunakan oleh kelas ViewModel lainnya.
open class BaseViewModel: CoreViewModel() {

    // Override fungsi apiLogout dari CoreViewModel, namun tidak ada implementasi di sini.
    // Fungsinya dapat diisi dengan logika untuk melakukan proses logout pengguna, seperti menghapus sesi atau token.
    override fun apiLogout() {
        // Implementasikan logika logout sesuai kebutuhan
    }

    // Override fungsi apiRenewToken dari CoreViewModel, namun tidak ada implementasi di sini.
    // Fungsinya dapat diisi dengan logika untuk memperbarui token API jika sudah kedaluwarsa.
    override fun apiRenewToken() {
        // Implementasikan logika untuk memperbarui token API
    }
}
