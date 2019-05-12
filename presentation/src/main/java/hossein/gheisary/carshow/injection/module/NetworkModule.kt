package hossein.gheisary.carshow.injection.module

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hossein.gheisary.data.remote.core.*
import hossein.gheisary.data.remote.core.ApiRepository.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
abstract class NetworkModule {
    @Module
    companion object {
        @JvmStatic
        @Provides
        internal fun provideRestApi(retrofit: Retrofit): Restapi {
            return retrofit.create(Restapi::class.java)
        }

        @JvmStatic
        @Provides
        internal fun provideRestDataSource(restapi: Restapi): RestDataSource {
            return RestDataSource(restapi)
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideScheduler(): Scheduler {
            return AppScheduler()
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideExecutor(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        @JvmStatic
        @Provides
        fun provideRetrofit(client: OkHttpClient): Retrofit {
              return Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .client(client)
                        .build()
        }

        @JvmStatic
        @Provides
        internal fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build()
        }
    }
}