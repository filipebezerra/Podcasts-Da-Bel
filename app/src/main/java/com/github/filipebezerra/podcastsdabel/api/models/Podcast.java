
package com.github.filipebezerra.podcastsdabel.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Podcast {
    @SerializedName("duration")
    @Expose
    public Long duration;

    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;

    @SerializedName("genre")
    @Expose
    public String genre;

    @SerializedName("permalink")
    @Expose
    public String permalink;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("uri")
    @Expose
    public String uri;

    @SerializedName("label_name")
    @Expose
    public String labelName;

    @SerializedName("tag_list")
    @Expose
    public String tagList;

    @SerializedName("track_count")
    @Expose
    public Long trackCount;

    @SerializedName("user_id")
    @Expose
    public Long userId;

    @SerializedName("last_modified")
    @Expose
    public String lastModified;

    @SerializedName("license")
    @Expose
    public String license;

    @SerializedName("tracks")
    @Expose
    public List<Track> tracks = new ArrayList<>();

    @SerializedName("playlist_type")
    @Expose
    public String playlistType;

    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("release")
    @Expose
    public String release;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;

    @SerializedName("ean")
    @Expose
    public String ean;

    @SerializedName("streamable")
    @Expose
    public Boolean streamable;
}
