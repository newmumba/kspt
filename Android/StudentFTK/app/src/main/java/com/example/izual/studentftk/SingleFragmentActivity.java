package com.example.izual.studentftk;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Djombo Yacho on 01.04.2015.
 */
public abstract class SingleFragmentActivity {
    private int contentView;
    private FragmentManager supportFragmentManager;

    public abstract void onNewIntent(Intent intent);

    public abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }





    public void startSearch(String initialQuery, boolean b, Bundle appSearchData, boolean globalSearch) {

    }

    public void setContentView(int contentView) {
        this.contentView = contentView;
    }

    public int getContentView() {
        return contentView;
    }

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public void onCreate(Bundle savedInstanceState) {

    }
}
