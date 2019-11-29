package com.brace.android.b31.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.brace.android.b31.R;
import com.brace.android.b31.activity.ui.t.TFragment;

public class TActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TFragment.newInstance())
                    .commitNow();
        }
    }
}
