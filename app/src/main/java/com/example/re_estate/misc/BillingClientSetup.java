package com.example.re_estate.misc;

import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.Arrays;
import java.util.List;

public class BillingClientSetup {
    public static final String IN_APP_PRODUCT_ID = "in_app_product_id";
    public static final String SUBSCRIPTION_PRODUCT_ID = "subscription_product_id";
    public static final List<String> PRODUCTS = List.of(IN_APP_PRODUCT_ID);
    public static final List<String> SUBSCRIPTIONS = List.of(SUBSCRIPTION_PRODUCT_ID);
    public static String googlePlayBaseUrl = "https://play.google.com/store/account/subscriptions";
    private static BillingClient instance;

    public static BillingClient getInstance(Context context, PurchasesUpdatedListener listener) {
        return instance == null ? setupBillingClient(context, listener) : instance;
    }

    public static BillingClient setupBillingClient(Context context, PurchasesUpdatedListener listener) {
        return BillingClient.newBuilder(context).enablePendingPurchases().setListener(listener).build();
    }
}
