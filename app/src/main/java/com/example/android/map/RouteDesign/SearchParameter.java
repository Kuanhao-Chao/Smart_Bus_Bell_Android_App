package com.example.android.map.RouteDesign;

import java.util.List;

/**
 * Created by Howard on 2017/7/22.
 */

public class SearchParameter {

    private List<SearchParameter_directly> mSearchParameter_directly;
    private List<SearchParameter_Google> mSearchParameter_google;
    public SearchParameter ( List<SearchParameter_directly> searchParameter_directly, List<SearchParameter_Google> searchParameter_google){
        mSearchParameter_directly = searchParameter_directly;
        mSearchParameter_google = searchParameter_google;
    }

    public List<SearchParameter_directly> getmSearchParameter_directly(){return mSearchParameter_directly;}
    public List<SearchParameter_Google> getmSearchParameter_google(){return mSearchParameter_google;}

}