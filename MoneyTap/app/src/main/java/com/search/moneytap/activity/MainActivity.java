package com.search.moneytap.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.search.moneytap.R;
import com.search.moneytap.RecyclerTouchListener;
import com.search.moneytap.SearchAdapter;
import com.search.moneytap.SearchItem;
import com.search.moneytap.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<SearchItem> searchItemList = new ArrayList<>();
    private String wikiUrl = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpslimit=10&gpssearch=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        initViews();
        loadJSON(wikiUrl + "Sachin T");


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                String url = "http://en.wikipedia.org/?curid=" + searchItemList.get(position).getTitle();

                openInAppBrowser(url);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

//setting the response in recycler view

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        searchAdapter = new SearchAdapter(searchItemList, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(searchAdapter);
    }

    // calling wiki api
    private void loadJSON(String query) {

// checking cache for data
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(query);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");

                try {
                    JSONObject jsonObject = new JSONObject(data);
                    for (int i = 0; i < jsonObject.getJSONObject("query").getJSONArray("pages").length(); i++) {
                        JSONObject jsonPages = new JSONObject(jsonObject.getJSONObject("query").getJSONArray("pages").get(i).toString());
                        prepareRecyclerData(jsonPages.getString("pageid"),
                                jsonPages.getJSONObject("thumbnail").getString("source"),
                                jsonPages.getString("title"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
            wikiRequest(query);
        }

    }


    void wikiRequest(String query) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                query, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //storing credentials in shared preferences
                Log.d("response", response.toString());
                try {
                    for (int i = 0; i < response.getJSONObject("query").getJSONArray("pages").length(); i++) {
                        JSONObject jsonObject = new JSONObject(response.getJSONObject("query").getJSONArray("pages").get(i).toString());
                        prepareRecyclerData(jsonObject.getString("pageid"),
                                jsonObject.getJSONObject("thumbnail").getString("source"),
                                jsonObject.getString("title"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Response", error.toString());

            }

        }) {


        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonObjReq.setShouldCache(true);
        //caching the request
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadJSON(wikiUrl+ query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return true;
            }
        });
    }

    void prepareRecyclerData(String title, String icon, String desc) {

        searchItemList.add(new SearchItem(title, icon, desc));
        searchAdapter.notifyDataSetChanged();
    }

    //opening webview
    private void openInAppBrowser(String url) {
        Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


}
