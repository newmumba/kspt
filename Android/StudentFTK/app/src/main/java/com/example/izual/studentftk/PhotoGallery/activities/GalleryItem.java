package com.example.izual.studentftk.PhotoGallery.activities;

import com.example.izual.studentftk.PhotoGallery.Getter;
import com.example.izual.studentftk.PhotoGallery.Setter;

public class GalleryItem {

    @Getter
    @Setter
    private String caption;
    @Getter @Setter private String id;
    @Getter @Setter private String url;

    public GalleryItem() {
        this.caption = caption;
        this.id = id;
        this.url = url;
    }

    public String toString() {
        return caption;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
