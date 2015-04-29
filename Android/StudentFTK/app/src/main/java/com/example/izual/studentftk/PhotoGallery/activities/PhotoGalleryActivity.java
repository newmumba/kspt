package com.example.izual.studentftk.PhotoGallery.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.izual.studentftk.PhotoGalleryFragment;
import com.example.izual.studentftk.R;
import com.example.izual.studentftk.SingleFragmentActivity;

public class PhotoGalleryActivity  extends SingleFragmentActivity {
    private FragmentManager supportFragmentManager;

    ImageView im;
    private View menuInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void biggerView(View v)
    {

        switch (v.getId())
        {
            case R.id.image1: im.setImageResource(R.drawable.im1);
                break;
            case R.id.image2: im.setImageResource(R.drawable.im2);
                break;
            case R.id.image3: im.setImageResource(R.drawable.im3);
                break;
            case R.id.image4: im.setImageResource(R.drawable.im4);
                break;
            case R.id.image5: im.setImageResource(R.drawable.im5);
                break;
            case R.id.image6: im.setImageResource(R.drawable.im6);
                break;
            case R.id.image7: im.setImageResource(R.drawable.im7);
                break;
        }

        //im.setImageResource(View.VISIBLE);
    }

    private void findViewById(Object selected) {

    }


    @Override
    public void onNewIntent(Intent intent) {
        PhotoGalleryFragment fragment = (PhotoGalleryFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i("PhotoGalleryActivity", "Received a new search query: " + query);


        }

        fragment.updateItems();
    }

    @Override
    public Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void startSearch(String initialQuery, boolean selectedInitialQuery, Bundle appSearchData, boolean globalSearch) {
        super.startSearch(initialQuery, true, appSearchData, globalSearch);
    }


    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public View getMenuInflater() {
        return menuInflater;
    }
}
