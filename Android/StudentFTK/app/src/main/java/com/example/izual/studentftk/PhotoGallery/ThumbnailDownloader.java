package com.example.izual.studentftk.PhotoGallery;

import android.widget.ImageView;

import java.util.logging.Handler;

/**
 * Created by Djombo Yacho on 01.04.2015.
 */
public class ThumbnailDownloader<T> {
    private Listener<ImageView> listener;
    private Object looper;

    public ThumbnailDownloader(Handler handler) {

    }

    public void setListener(Listener<ImageView> listener) {
        this.listener = listener;
    }

    public Listener<ImageView> getListener() {
        return listener;
    }

    public void start() {

    }

    public Object getLooper() {
        return looper;
    }

    public void clearQueue() {

    }

    public void quit() {

    }

    public void queueThumbnail(T imageView, Object url) {

    }

    public void queueThumbnailForPreload(Object url) {

    }

    public static class Listener<T> {
    }
}
