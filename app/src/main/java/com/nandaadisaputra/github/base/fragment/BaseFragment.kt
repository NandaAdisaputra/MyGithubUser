// Kelas ini merupakan fragment dasar yang mengextends CoreFragment dan menggunakan ViewDataBinding.
// Kelas ini diharapkan digunakan sebagai dasar untuk fragment lain, yang akan menangani binding view.
package com.nandaadisaputra.github.base.fragment

import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.fragment.CoreFragment

// Kelas BaseFragment menerima parameter generic VB yang merupakan tipe dari ViewDataBinding.
// Layout resource yang diterima akan digunakan untuk mengatur tampilan fragment.
open class BaseFragment<VB: ViewDataBinding>(layoutRes: Int): CoreFragment<VB>(layoutRes) {
    // Kelas ini tidak menambah fungsionalitas baru, hanya menginherit CoreFragment dan menambahkan dukungan untuk ViewDataBinding
    // sehingga fragment yang menggunakannya dapat dengan mudah mengakses dan mengelola binding data untuk layout mereka.
}
