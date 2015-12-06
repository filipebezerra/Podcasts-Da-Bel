
package com.github.filipebezerra.podcastsdabel.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

@Parcel
public class Track {
    @SerializedName("id")
    @Expose
    public Long id;

    @SerializedName("created_at")
    @Expose
    public String createdAt;

    @SerializedName("duration")
    @Expose
    public Long duration;

    @SerializedName("commentable")
    @Expose
    public Boolean commentable;

    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("original_content_size")
    @Expose
    public Long originalContentSize;

    @SerializedName("last_modified")
    @Expose
    public String lastModified;

    @SerializedName("tag_list")
    @Expose
    public String tagList;

    @SerializedName("streamable")
    @Expose
    public Boolean streamable;

    @SerializedName("downloadable")
    @Expose
    public Boolean downloadable;

    @SerializedName("genre")
    @Expose
    public String genre;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("original_format")
    @Expose
    public String originalFormat;

    @SerializedName("license")
    @Expose
    public String license;

    @SerializedName("uri")
    @Expose
    public String uri;

    @SerializedName("permalink_url")
    @Expose
    public String permalinkUrl;

    @SerializedName("artwork_url")
    @Expose
    public String artworkUrl;

    @SerializedName("waveform_url")
    @Expose
    public String waveformUrl;

    @SerializedName("stream_url")
    @Expose
    public String streamUrl;

    @SerializedName("playback_count")
    @Expose
    public Integer playbackCount;

    @SerializedName("download_count")
    @Expose
    public Integer downloadCount;

    @SerializedName("favoritings_count")
    @Expose
    public Integer favoritingsCount;

    @SerializedName("comment_count")
    @Expose
    public Integer commentCount;

    @SerializedName("attachments_uri")
    @Expose
    public String attachmentsUri;

    public Track() {
        // no args
    }
}
