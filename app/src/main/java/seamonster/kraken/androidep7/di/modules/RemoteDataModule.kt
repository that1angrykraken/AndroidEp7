package seamonster.kraken.androidep7.di.modules

import dagger.Module
import dagger.Provides
import seamonster.kraken.androidep7.data.sources.api.PublicApi
import seamonster.kraken.androidep7.data.sources.RemoteDataSource
import seamonster.kraken.androidep7.data.sources.api.NotificationApi
import seamonster.kraken.androidep7.data.sources.api.TimeSheetApi
import seamonster.kraken.androidep7.data.sources.api.TokenApi
import seamonster.kraken.androidep7.data.sources.api.TrackingApi
import seamonster.kraken.androidep7.data.sources.api.UserApi
import javax.inject.Singleton

@Module
object RemoteDataModule {

    @Provides
    @Singleton
    fun provideUserApi(remoteDataSource: RemoteDataSource): UserApi {
        return remoteDataSource.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenApi(remoteDataSource: RemoteDataSource): TokenApi {
        return remoteDataSource.create(TokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimeSheetApi(remoteDataSource: RemoteDataSource): TimeSheetApi {
        return remoteDataSource.create(TimeSheetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTrackingApi(remoteDataSource: RemoteDataSource): TrackingApi {
        return remoteDataSource.create(TrackingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationsApi(remoteDataSource: RemoteDataSource): NotificationApi {
        return remoteDataSource.create(NotificationApi::class.java)
    }
}