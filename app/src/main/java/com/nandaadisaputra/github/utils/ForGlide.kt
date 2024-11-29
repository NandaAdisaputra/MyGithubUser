package com.nandaadisaputra.github.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nandaadisaputra.github.R

// Kelas ini digunakan untuk memuat gambar ke dalam ImageView menggunakan Glide.
class ForGlide {

    companion object {
        // Anotasi BindingAdapter memungkinkan kita untuk menghubungkan properti `imageUrl` pada XML
        // dengan metode `loadImage` untuk memuat gambar secara otomatis.
        @JvmStatic
        @BindingAdapter(value = ["imageUrl"], requireAll = false) // Menandakan bahwa `imageUrl` adalah properti yang akan diikat
        fun loadImage(view: ImageView, imageUrl: String?) {
            // Reset gambar yang ada pada ImageView
            view.setImageDrawable(null)

            // Mengecek apakah `imageUrl` tidak null
            imageUrl?.let {
                // Memuat gambar dengan Glide
                Glide
                    .with(view.context) // Mengambil konteks dari ImageView
                    .load(imageUrl) // Memuat gambar dari URL yang diberikan
                    .apply(RequestOptions.centerCropTransform()) // Mengatur gambar agar crop dan terpusat
                    .placeholder(R.drawable.img_empty) // Menampilkan placeholder jika gambar belum dimuat
                    .error(R.drawable.ic_block) // Menampilkan gambar error jika terjadi masalah saat memuat gambar
                    .into(view) // Menampilkan gambar yang dimuat ke dalam ImageView
            }
        }
    }
}
