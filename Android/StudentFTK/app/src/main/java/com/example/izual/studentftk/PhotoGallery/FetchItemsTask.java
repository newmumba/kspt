package com.example.izual.studentftk.PhotoGallery;

import java.util.ArrayList;

/**
 * Created by Djombo Yacho on 01.04.2015.
 */
public interface FetchItemsTask {
    void onPostExecute(FetchItemsTask.GalleryItemCollection collection);

    public class GalleryItemCollection {
        private ArrayList<com.example.izual.studentftk.PhotoGallery.activities.GalleryItem> items;
        private String total;







        public GalleryItemCollection(ArrayList<GalleryItem> items) {

        }


        public ArrayList<com.example.izual.studentftk.PhotoGallery.activities.GalleryItem> getItems() {
            return items;
        }

        public String getTotal() {
            return total;
        }

        private class GalleryItem {
        }
    }
}
