package com.nandaadisaputra.github.injection

import android.content.Context
import com.crocodic.core.BuildConfig
import com.crocodic.core.data.CoreSession
import com.crocodic.core.helper.okhttp.SSLTrust
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nandaadisaputra.github.api.ApiService
import com.nandaadisaputra.github.api.ApiServiceTwo
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


@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) = UserDatabase.getDatabase(context)

    @Provides
    fun provideFavoriteDao(appDatabase: UserDatabase) = appDatabase.favoriteDao()

    @Provides
    fun provideLoginDao(appDatabase: UserDatabase) = appDatabase.loginDao()

    @Provides
    fun provideGson(): Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    @Provides
    fun provideSession(@ApplicationContext context: Context) = CoreSession(context)

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context) : DataStorePreference {
        return DataStorePreference(context)
    }
    @Provides
    fun provideOkHttpClient(session: CoreSession): OkHttpClient {

        val unsafeTrustManager = SSLTrust().createUnsafeTrustManager()
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(unsafeTrustManager), null)

        val okHttpClient = OkHttpClient().newBuilder()
            .sslSocketFactory(sslContext.socketFactory, unsafeTrustManager)
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val token = session.getString(Const.TOKEN.API_TOKEN)
                Timber.d("token: $token")
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val interceptors = HttpLoggingInterceptor()
            interceptors.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(interceptors)
        }

        return okHttpClient.build()
    }

    // TODO: add base url
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl("http://api.github.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(ApiService::class.java)
    }

    @Provides
    fun provideApiServiceTwo(okHttpClient: OkHttpClient): ApiServiceTwo {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build().create(ApiServiceTwo::class.java)
    }
}