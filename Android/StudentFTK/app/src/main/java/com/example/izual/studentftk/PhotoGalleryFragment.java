package com.example.izual.studentftk;

import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.izual.studentftk.PhotoGallery.EndlessScrollListener;
import com.example.izual.studentftk.PhotoGallery.FlickrFetchr;
import com.example.izual.studentftk.PhotoGallery.activities.GalleryItem;
import com.example.izual.studentftk.PhotoGallery.ThumbnailDownloader;
import com.google.android.gms.internal.x;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Djombo Yacho on 01.04.2015.
 */
public class PhotoGalleryFragment extends Fragment {


    private GridView gridView;
    private TextView pageNumberView;

    private ArrayList<GalleryItem> items;
    private ThumbnailDownloader<ImageView> thumbnailThread;
    private int count;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        updateItems();

        thumbnailThread = new ThumbnailDownloader<ImageView>(new Handler() {
            @Override
            public void close() {

            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord record) {

            }
        });
        thumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        thumbnailThread.start();
        thumbnailThread.getLooper();
        Log.i("PhotoGalleryFragment", "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        LinearLayout gridViewHeader = (LinearLayout) view.findViewById(R.id.gridViewHeader);
        pageNumberView = (TextView) gridViewHeader.findViewById(R.id.pageNumber);

        setPageNumber(1);

        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setOnScrollListener(new EndlessScrollListener() {
            public void onLoadMore(int page, int totalItemsCount) {
                new FetchItemsTask().execute(page);
                setPageNumber(page);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thumbnailThread.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailThread.quit();
        Log.i("PhotoGalleryFragment", "Background thread destroyed");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Pull out the SearchView
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            // Get the data from out searchable.xml as a SearchableInfo
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();
            SearchableInfo searchInfo = searchManager.getSearchableInfo(name);

            searchView.setSearchableInfo(searchInfo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity()).
                        edit().
                        putString(FlickrFetchr.PREF_SEARCH_QUERY, null).
                        commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    getActivity().invalidateOptionsMenu();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        }
        else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    public void updateItems() {
        this.items = null;
        new FetchItemsTask().execute(1);
    }

    private void setPageNumber(int page) {
        pageNumberView.setText("Page: " + page);
    }

    public int getCount() {
        return count;
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, com.example.izual.studentftk.PhotoGallery.FetchItemsTask.GalleryItemCollection> {
        @Override
        protected com.example.izual.studentftk.PhotoGallery.FetchItemsTask.GalleryItemCollection doInBackground(Integer... params) {
            Activity activity = getActivity();
            if (activity == null) {

            }

            String query = PreferenceManager.getDefaultSharedPreferences(activity).
                    getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
            Log.i("PhotoGalleryFragment", "FetchItemsTask received query: " + query);

            if (query != null) {
                return new FlickrFetchr().search(query, params[0]);
            }
            else {
                return new FlickrFetchr().fetchItems(params[0]);
            }
        }

        @Override
        protected void onPostExecute(com.example.izual.studentftk.PhotoGallery.FetchItemsTask.GalleryItemCollection collection) {
            if (PhotoGalleryFragment.this.items == null) {
                PhotoGalleryFragment.this.items = collection.getItems();
                Toast.makeText(getActivity(), "" + collection.getTotal() + " results",  Toast.LENGTH_SHORT).show();
                setupAdapter();
            }
            else {
                PhotoGalleryFragment.this.items.addAll(collection.getItems());
                ((ArrayAdapter) gridView.getAdapter()).notifyDataSetChanged();
            }
        }

        public x execute(int page) {
            return null;
        }
    }

    private void setupAdapter() {
        if (getActivity() != null || gridView != null) {
            gridView.setAdapter(new GalleryItemAdapter(items));
        }
    }




        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.im1);
            GalleryItem item = getItem(position);
            thumbnailThread.queueThumbnail(imageView, item.getUrl());

            preloadPreviousAndNextTenItems(position);

            return convertView;
        }

    private void preloadPreviousAndNextTenItems(int position) {
        int start = position - 10;
        if (start < 0) {
            start = 0;
        }

        int end = position + 10;
        if (end >= getCount()) {
            end = getCount() - 1;
        }

        for (int i = start; i <= end; i++) {
            if (i != position) {
                GalleryItem itemToCache = getItem(i);
                thumbnailThread.queueThumbnailForPreload(itemToCache.getUrl());
            }
        }
    }

    private GalleryItem getItem(int i) {
        return null;
    }

    private class GalleryItemAdapter implements ListAdapter {
        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}



    class PollService {
        public static void setServiceAlarm(Activity activity, boolean shouldStartAlarm) {

        }

        public static boolean isServiceAlarmOn(Activity activity) {
            return false;
        }
    }

