package com.nandaadisaputra.github.injection

import android.content.Context
import com.crocodic.core.BuildConfig
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nandaadisaputra.github.api.ApiService
import com.nandaadisaputra.github.data.constant.Const
import com.nandaadisaputra.github.data.room.database.UserDatabase
import com.nandaadisaputra.github.datastore.DataStorePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext

// Modul Hilt untuk menyediakan dependensi yang diperlukan oleh aplikasi
@InstallIn(SingletonComponent::class)  // Ini memastikan modul ini diinstal di level aplikasi (singleton)
@Module
class DataModule {
    // Menyediakan instance UserDatabase yang akan digunakan di seluruh aplikasi
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = UserDatabase.getDatabase(context)
    // Menyediakan instance FavoriteUsersDao yang digunakan untuk operasi CRUD pada tabel favorit
    @Provides
    fun provideFavoriteDao(appDatabase: UserDatabase) = appDatabase.favoriteDao()
    // Menyediakan instance Gson untuk konversi objek ke JSON dan sebaliknya
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    // Menyediakan instance CoreSession untuk sesi aplikasi (misalnya untuk mengambil token pengguna)
    @Provides
    fun provideSession(@ApplicationContext context: Context) = CoreSession(context)
    // Menyediakan instance DataStorePreference untuk akses SharedPreferences melalui DataStore
    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context) : DataStorePreference {
        return DataStorePreference(context)
    }
    // Menyediakan instance OkHttpClient untuk melakukan HTTP request dengan pengaturan SSL dan intersep token
    @Provides
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {
        // Membuat SSLContext dengan trust manager yang memungkinkan koneksi tidak aman
        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager) // Menggunakan custom SSLContext
            .connectTimeout(90, TimeUnit.SECONDS)  // Timeout untuk koneksi
            .readTimeout(90, TimeUnit.SECONDS) // Timeout untuk pembacaan data
            .writeTimeout(90, TimeUnit.SECONDS) // Timeout untuk penulisan data
            .addInterceptor { chain -> // Menambahkan interceptor untuk menambah header Authorization
                val original = chain.request()
                val token = session.getString(Const.TOKEN.API_TOKEN) // Mengambil token dari session
                Timber.d("token: $token")
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token") // Menambahkan header Authorization
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)  // Melanjutkan permintaan dengan header yang sudah dimodifikasi
            }
// Jika aplikasi dalam mode debug, menambahkan logging untuk melihat request/response
        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY // Menampilkan body request/response
            okHttpClient.addInterceptor(interceptors)
        }

        return okHttpClient.build() // Membangun dan mengembalikan OkHttpClient
    }
    // Menyediakan instance ApiService untuk komunikasi dengan API GitHub
    // TODO: add base url
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        val gson = GsonBuilder()
            .setLenient()
            .create() // Membuat instance Gson dengan konversi lebih fleksibel
        return Retrofit.Builder()
            .baseUrl("http://api.github.com/") // URL dasar untuk API GitHub
            .addConverterFactory(ScalarsConverterFactory.create()) // Menambahkan converter untuk tipe data skalar
            .addConverterFactory(GsonConverterFactory.create(gson)) // Menambahkan converter untuk konversi JSON
            .client(okHttpClient)  // Menggunakan OkHttpClient yang telah dikonfigurasi
            .build().create(ApiService::class.java) // Membangun Retrofit dan membuat instance ApiService
    }
}