package com.izhar.melsha;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.izhar.melsha.activities.ErrorActivity;
import com.izhar.melsha.adapters.PurchasedAdapter;
import com.izhar.melsha.models.PurchasedModel;
import com.izhar.melsha.models.SoldModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public String getUrl(Context context) {
        return context.getSharedPreferences("url", Context.MODE_PRIVATE).getString("url", "");
    }

    public List<SoldModel> getSold(Context context, String branch) {
        List<SoldModel> solds = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.getUrl(context) +
                "?action=getBranchItemSold&branch=" + branch,
                response -> {
                    try {
                        solds.clear();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            SoldModel sold = new SoldModel();
                            sold.setId(String.valueOf(object.get("id")));
                            sold.setCode(object.getString("code"));
                            sold.setName(object.getString("name"));
                            sold.setType(object.getString("type"));
                            sold.setModel(object.getString("model"));
                            sold.setDate(object.getString("date"));
                            sold.setFrom_store(object.getString("branch"));
                            if (object.get("size").toString().equalsIgnoreCase("") || object.get("size").toString().startsWith("#")) {
                                sold.setSize("0");
                            } else {
                                sold.setSize(object.getString("size"));
                            }
                            if (object.get("quantity").toString().equalsIgnoreCase("") || object.get("quantity").toString().startsWith("#")) {
                                sold.setQuantity(0);
                            } else {
                                sold.setQuantity(object.getInt("quantity"));
                            }
                            if (object.get("pr_price").toString().equalsIgnoreCase("") || object.get("pr_price").toString().startsWith("#")) {
                                sold.setPurchased_price(0);
                            } else {
                                sold.setPurchased_price(object.getInt("pr_price"));
                            }
                            if (object.get("sell_price").toString().equalsIgnoreCase("") || object.get("sell_price").toString().startsWith("#")) {
                                sold.setSold_price(0);
                            } else {
                                sold.setSold_price(object.getInt("sell_price"));
                            }
                            if (object.get("profit").toString().equalsIgnoreCase("") || object.get("profit").toString().startsWith("#")) {
                                sold.setProfit(0);

                            } else {
                                sold.setProfit(object.getInt("profit"));
                            }
                            solds.add(0, sold);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        context.startActivity(new Intent(context, ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            System.out.println(error.getMessage());
            solds.clear();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        return solds;
    }

    public List<PurchasedModel> getPurchased(Context context, String branch) {
        List<PurchasedModel> purchases = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.getUrl(context) +
                "?action=getBranchItemPurchased&branch=" + branch,
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        context.startActivity(new Intent(context, ErrorActivity.class).putExtra("error", e.toString()));
                    }

                }, error -> {
            System.out.println(error.getMessage());
            purchases.clear();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        
        return purchases;
    }
}
