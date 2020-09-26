package com.example.data.utils

import okhttp3.Interceptor
import okhttp3.Response

private const val USER_AGENT = "TestDiscogsApp/0.1 +http://example.com"
private const val USER_AGENT_HEADER = "User-Agent"
private const val AUTHORIZATION_HEADER = "Authorization"
private const val AUTHORIZATION_KEY = "Discogs key="
private const val AUTHORIZATION_SECRET = "secret="
private const val KEY = "YOUR KEY HERE"
private const val SECRET = "YOUR SECRET HERE"

class AuthInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader(USER_AGENT_HEADER, USER_AGENT)
            .addHeader(AUTHORIZATION_HEADER, "$AUTHORIZATION_KEY$KEY, $AUTHORIZATION_SECRET$SECRET")
            .build()
        return chain.proceed(request)
    }

}