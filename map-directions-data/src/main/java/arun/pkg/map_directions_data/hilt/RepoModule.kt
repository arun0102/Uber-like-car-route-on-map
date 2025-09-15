package arun.pkg.map_directions_data.hilt

import arun.pkg.map_directions_data.MapDirectionsRepositoryImpl
import arun.pkg.map_directions_data.service.MapDirectionsServices
import arun.pkg.map_directions_domain.MapDirectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Provides
    @Singleton
    internal fun provideMapDirectionsRepository(
        mapDirectionsServices: MapDirectionsServices
    ): MapDirectionsRepository {
        return MapDirectionsRepositoryImpl(mapDirectionsServices)
    }

    @Provides
    @Singleton
    internal fun provideMapDirectionsServices(): MapDirectionsServices {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()


        val builder = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
        return builder.create(MapDirectionsServices::class.java)
    }
}