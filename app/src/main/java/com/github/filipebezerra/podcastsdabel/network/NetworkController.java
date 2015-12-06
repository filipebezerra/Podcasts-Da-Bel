package com.github.filipebezerra.podcastsdabel.network;

import android.content.Context;
import android.support.annotation.NonNull;
import com.github.filipebezerra.podcastsdabel.api.constant.Keys;
import com.github.filipebezerra.podcastsdabel.api.service.SoundCloudService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 05/12/2015
 * @since #
 */
public class NetworkController {
    private static final long HTTP_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String HTTP_CACHE_FILE_NAME = "http";
    private static SoundCloudService sService;

    private NetworkController() {
        // no instances
    }

    public static SoundCloudService soundCloundService(@NonNull Context context) {
        if (sService == null) {
            final OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            httpClient.setReadTimeout(30, TimeUnit.SECONDS);
            httpClient.setCache(createCache(context));
            httpClient.interceptors().clear();
            httpClient.interceptors().add(createLoggingInterceptor());
            httpClient.interceptors().add(createCustomInterceptor());

            final Retrofit retrofit = new Retrofit.Builder().
                    baseUrl(SoundCloudService.BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                    client(httpClient).
                    build();

            sService = retrofit.create(SoundCloudService.class);
        }
        return sService;
    }

    private static Cache createCache(@NonNull Context context) {
        return new Cache(new File(context.getCacheDir(), HTTP_CACHE_FILE_NAME),
                HTTP_CACHE_SIZE);
    }

    private static Interceptor createLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return interceptor;
    }

    private static Interceptor createCustomInterceptor() {
        return chain -> {
            Request original = chain.request();

            final HttpUrl httpUrl = original.httpUrl()
                    .newBuilder()
                    .addQueryParameter("client_id", Keys.SOUNDCLOUD_CLIENT_ID)
                    .build();

            final Request request = original.newBuilder()
                    .url(httpUrl)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };
    }
}
