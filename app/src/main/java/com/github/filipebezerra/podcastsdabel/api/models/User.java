package com.github.filipebezerra.podcastsdabel.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * .
 *
 * @author Filipe Bezerra
 * @version #, 05/12/2015
 * @since #
 */
public class User {
    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("permalink")
    @Expose
    public String permalink;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("last_modified")
    @Expose
    public String lastModified;

    @SerializedName("uri")
    @Expose
    public String uri;

    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;

    @SerializedName("avatar_url")
    @Expose
    public String avatarUrl;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("first_name")
    @Expose
    public String firstName;

    @SerializedName("last_name")
    @Expose
    public String lastName;

    @SerializedName("full_name")
    @Expose
    public String fullName;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("website")
    @Expose
    public String website;

    @SerializedName("online")
    @Expose
    public Boolean online;

    @SerializedName("track_count")
    @Expose
    public Integer trackCount;

    @SerializedName("playlist_count")
    @Expose
    public Integer playlistCount;

    @SerializedName("public_favorites_count")
    @Expose
    public Integer publicFavoritesCount;

    @SerializedName("followers_count")
    @Expose
    public Integer followersCount;

    @SerializedName("followings_count")
    @Expose
    public Integer followingsCount;
}
