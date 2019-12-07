/*
 * ApiServiceModule.kt
 * Created by Sanjay.Sah
 */

package com.sanjay.authorslist.injection.module


import android.content.Context
import android.net.ConnectivityManager
import com.sanjay.authorslist.AuthorsApplication
import com.sanjay.authorslist.BuildConfig
import com.sanjay.authorslist.data.api.AuthorsListService
import com.sanjay.authorslist.data.api.HeaderInterceptor
import com.sanjay.authorslist.exception.NoNetworkException
import com.sanjay.authorslist.extensions.isConnected
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLPeerUnverifiedException


/**
 * @author Sanjay.Sah
 */

@Module
open class ApiServiceModule {

    @Provides
    @Named(BASE_URL)
    internal fun provideBaseUrl(): String {
        return BuildConfig.API_URL
    }

    @Provides
    @Singleton
    internal fun provideHeaderInterceptor(): HeaderInterceptor {
        return HeaderInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(headerInterceptor: HeaderInterceptor,
                                   httpInterceptor: HttpLoggingInterceptor, app: AuthorsApplication): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpInterceptor.apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE })

        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        return okHttpClientBuilder
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.addHeader("User-Agent","a")
                    if(!connectivityManager.isConnected) {
                        throw NoNetworkException
                    }
                    try {
                        chain.proceed(requestBuilder.build()).apply {
                        }
                    } catch (e: SocketTimeoutException) {
                        throw NoNetworkException
                    } catch (e: UnknownHostException) {
                        throw NoNetworkException
                    } catch (e: SSLPeerUnverifiedException) {
                        throw NoNetworkException
                    }
                }
                .build()
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRxJavaAdapterFactory(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(@Named(BASE_URL) baseUrl: String, converterFactory: Converter.Factory,
                                 callAdapterFactory: CallAdapter.Factory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(client)
                .build()
    }

    companion object {
        private const val BASE_URL = "base_url"
    }

    /* Specific services */
    @Provides
    @Singleton
    open fun provideService(retrofit: Retrofit): AuthorsListService {
        return retrofit.create(AuthorsListService::class.java)
    }
}
