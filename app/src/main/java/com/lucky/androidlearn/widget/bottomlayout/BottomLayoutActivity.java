package com.lucky.androidlearn.widget.bottomlayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lucky.androidlearn.R;


public class BottomLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomlayout_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TestFragmentAdapter mAdapter;
        private ViewPager mPager;
        private MainBottomTabLayout mTabLayout;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bottomlayout_main, container, false);
            setupViews(rootView);
            return rootView;
        }

        private void setupViews(View view) {
            mAdapter = new TestFragmentAdapter(getFragmentManager());
            mPager = (ViewPager) view.findViewById(R.id.tab_pager);
            mPager.setAdapter(mAdapter);
            mTabLayout = (MainBottomTabLayout) view.findViewById(R.id.main_bottom_tablayout);
            mTabLayout.setViewPager(mPager);
        }
    }
}
