package seamonster.kraken.androidep7.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import seamonster.kraken.androidep7.data.sources.remote.RemoteDataSource
import seamonster.kraken.androidep7.data.sources.remote.TokenController
import seamonster.kraken.androidep7.data.sources.remote.UserRestController
import javax.inject.Singleton

@Module
object DataModule {

    @Provides
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSource()

    @Provides
    @Singleton
    fun provideUserDataSource(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): UserRestController = remoteDataSource.buildApi(UserRestController::class.java, context)

    @Provides
    @Singleton
    fun provideTokenDataSource(
        remoteDataSource: RemoteDataSource,
        context: Context
    ): TokenController = remoteDataSource.buildApi(TokenController::class.java, context)
}