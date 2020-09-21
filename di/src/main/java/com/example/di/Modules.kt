package com.example.di

import com.example.data.datasource.SearchDataSource
import com.example.data.repository.SearchRepositoryImpl
import com.example.data.utils.AuthInterceptor
import com.example.domain.repository.SearchRepository
import com.example.presentation.viewmodel.ArtistViewModel
import com.example.presentation.viewmodel.SearchViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.discogs.com/"

class Modules {
    companion object {
        val viewModelModule = module {
            viewModel { SearchViewModel(get()) }
            viewModel { ArtistViewModel(get()) }
        }

        val apiModule = module {
            fun provideSearchDataSource(retrofit: Retrofit): SearchDataSource {
                return retrofit.create(SearchDataSource::class.java)
            }

            single { provideSearchDataSource(get()) }
        }

        val repositoryModule = module {
            fun provideSearchRepository(dataSource: SearchDataSource): SearchRepository =
                SearchRepositoryImpl(dataSource)

            single { provideSearchRepository(get()) }
        }

        val netModule = module {

            fun provideHttpClient(): OkHttpClient {
                val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(AuthInterceptor())
                return okHttpClientBuilder.build()
            }

            fun provideGson(): Gson {
                return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
            }

            fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(client)
                    .build()
            }

            single { provideHttpClient() }
            single { provideRetrofit(get(), get()) }
            single { provideGson() }

        }
    }
}