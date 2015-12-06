package com.github.filipebezerra.podcastsdabel.api.service;

import com.github.filipebezerra.podcastsdabel.api.models.Podcast;
import com.github.filipebezerra.podcastsdabel.api.models.User;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 05/12/2015
 * @since #
 */
public interface SoundCloudService {
    String BASE_URL = "http://api.soundcloud.com/";

    @GET("users/{id}")
    Observable<User> user(@Path("id") int userId);

    @GET("playlists/{id}")
    Observable<Podcast> playlist(@Path("id") int playlistId);
}