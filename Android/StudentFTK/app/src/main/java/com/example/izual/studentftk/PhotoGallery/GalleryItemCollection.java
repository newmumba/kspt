package com.example.izual.studentftk.PhotoGallery;

import com.example.izual.studentftk.PhotoGallery.activities.GalleryItem;

import java.util.ArrayList;

public class GalleryItemCollection {

    @Getter @Setter private ArrayList<GalleryItem> items;
    @Getter @Setter private int total;

    public GalleryItemCollection() {
        this.items = new ArrayList<GalleryItem>();
        this.total = total;
    }

    public int getSize() {
        return items.size();
    }

    public void setItems(ArrayList<GalleryItem> items) {
        this.items = items;
    }

    public ArrayList<GalleryItem> getItems() {
        return items;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
