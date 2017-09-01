package com.example.android.map.RouteDesign;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.map.MapsActivity;
import com.example.android.map.R;


/**
 * Created by Howard on 2017/7/22.
 */

public class SearchResult extends AppCompatActivity {

    private static final String TAG = SearchResult.class.getSimpleName();
    private Context mContext = SearchResult.this;
//    private String URL_SEARCH = "http://mysmartbus.herokuapp.com/search/";
    private double longitude;
    private double latitude;
    private String answerYouEnter;
    MapsActivity mapsActivity = new MapsActivity();
    //private static SearchParameter result = new SearchParameter(null, null);
    private ViewPager viewPager;
//    private FragmentPagerAdapter ;



    @Override
    protected void onCreate(Bundle InsatnceSavedState){
        super.onCreate(InsatnceSavedState);
        setContentView(R.layout.search_result);
//        SectionsPagerAdapter mSectionsPagerAdapter;
        Log.d(TAG, "onCreate starting.");
        setupViewPager();
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
    //public SearchParameter getResult(){ return result;}

    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment( new List_Directly_Route());     //index 0
        adapter.addFragment( new List_Google_Route());     //index 1
        ViewPager viewPager = (ViewPager)findViewById(R.id.containerViewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("直達");
        tabLayout.getTabAt(1).setText("轉乘");
    }
}

