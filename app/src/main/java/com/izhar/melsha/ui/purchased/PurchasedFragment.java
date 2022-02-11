package com.izhar.melsha.ui.purchased;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.izhar.melsha.R;
import com.izhar.melsha.Utils;
import com.izhar.melsha.adapters.PurchasedAdapter;
import com.izhar.melsha.adapters.PurchasedTabAdapter;
import com.izhar.melsha.adapters.TabAdapter;
import com.izhar.melsha.models.PurchasedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PurchasedFragment extends Fragment {
    View root;
    /*private RecyclerView recycler;
    private TextView no_store;
    private List<PurchasedModel> purchases = new ArrayList<>();
    private PurchasedAdapter adapter;
    private ProgressBar progress;*/

    PurchasedTabAdapter tabAdapter;
    TabLayout tab;
    ViewPager view;
    View mIndicator;
    private int indicatorWidth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_purchased, container, false);
        tab = root.findViewById(R.id.tab);
        view = root.findViewById(R.id.viewpager);

        mIndicator = root.findViewById(R.id.indicator);
        tabAdapter = new PurchasedTabAdapter(getFragmentManager(), tab.getTabCount());
        view.setAdapter(tabAdapter);
        tab.setupWithViewPager(view);
        //view.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab.post(() -> {
            indicatorWidth = tab.getWidth() / tab.getTabCount();
            //Assign new width
            FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            mIndicator.setLayoutParams(indicatorParams);
        });
        return root;
    }

    /*void getPurchased() {
        Utils utils = new Utils();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, utils.getUrl() +
                "?action=getAllItemPurchased",
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            PurchasedModel purchased = new PurchasedModel();
                            purchased.setId(String.valueOf(object.get("id")));
                            purchased.setCode(object.getString("code"));
                            purchased.setName(object.getString("name"));
                            purchased.setType(object.getString("type"));
                            purchased.setModel(object.getString("model"));
                            purchased.setDate(object.getString("date"));
                            purchased.setStore(object.getString("branch"));
                            if (object.get("size").toString().equalsIgnoreCase("")) {
                                purchased.setSize("0");
                            } else {
                                purchased.setSize(object.getString("size"));
                            }
                            if (object.get("quantity").toString().equalsIgnoreCase("")) {
                                purchased.setQuantity(0);
                            } else {
                                purchased.setQuantity(object.getInt("quantity"));
                            }
                            if (object.get("pr_price").toString().equalsIgnoreCase("")) {
                                purchased.setPurchased_price(0);
                            } else {
                                purchased.setPurchased_price(object.getInt("pr_price"));
                            }

                            purchases.add(0, purchased);
                        }
                        adapter = new PurchasedAdapter(getContext(), purchases);
                        recycler.setAdapter(adapter);
                        progress.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            Snackbar.make(root, "Unable to load the data.", Snackbar.LENGTH_LONG)
                    .setAction("Retry", v -> {
                        getPurchased();
                    })
                    .show();
            error.printStackTrace();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }*/
}