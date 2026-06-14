package com.arkhamcards.v2.data.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.interceptor.RetryOnErrorInterceptor
import com.apollographql.apollo.network.NetworkMonitor
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.arkhamcards.v2.data.objects.JsonElementAdapter
import com.arkhamcards.v2.type.Jsonb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val SERVER_URL = "gapi.arkhamcards.com/v1/graphql"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OptIn(ApolloExperimental::class)
    @Provides
    @Singleton
    fun provideApolloClient(@ApplicationContext context: Context): ApolloClient = ApolloClient.Builder()
        .serverUrl("https://$SERVER_URL")
        .addHttpInterceptor( object : HttpInterceptor {
            override suspend fun intercept(
                request: HttpRequest,
                chain: HttpInterceptorChain
            ): HttpResponse {
                val token = null // authTokenProvider.getToken()
                val newRequest = if (token.isNullOrBlank()) {
                    request
                } else {
                    request.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                }

                return chain.proceed(newRequest)
            }
        })
        .retryOnErrorInterceptor(RetryOnErrorInterceptor(NetworkMonitor(context)))
        .failFastIfOffline(true)
        .addCustomScalarAdapter(Jsonb.type, JsonElementAdapter)
        .build()

}