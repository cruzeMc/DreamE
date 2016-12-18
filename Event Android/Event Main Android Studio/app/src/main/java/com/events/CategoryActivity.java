package com.events;

import android.os.Bundle;

import com.events.custom.CustomActivity;

import com.events.ui.CategoryDetail;

public class CategoryActivity extends CustomActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment();
    }
    private void addFragment()	{
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new CategoryDetail()).commit();
    }
}
