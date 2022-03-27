package com.proxglobal.purchase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.proxglobal.purchase.function.BillingListener;
import com.proxglobal.purchase.function.PurchaseListioner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxPurchase {
    private static final String LICENSE_KEY = null;
    private static final String MERCHANT_ID = null;
    private static final String TAG = "ProxPurchase";

    //    public static final String PRODUCT_ID = "android.test.purchased";
    @SuppressLint("StaticFieldLeak")
    private static ProxPurchase instance;

    @SuppressLint("StaticFieldLeak")
    private String price = "1.49$";
    private String oldPrice = "2.99$";
    private String productId;
    private List<String> listSubcriptionId;
    private List<String> listINAPId;
    private PurchaseListioner purchaseListioner;
    private BillingListener billingListener;
    private Boolean isInitBillingFinish = false;
    private BillingClient billingClient;
    private List<SkuDetails> skuListINAPFromStore;
    private List<SkuDetails> skuListSubsFromStore;
    final private Map<String, SkuDetails> skuDetailsINAPMap = new HashMap<>();
    final private Map<String, SkuDetails> skuDetailsSubsMap = new HashMap<>();
    private boolean isAvailable;
    private boolean isListGot;
    private boolean isConsumePurchase = false;

    //tracking purchase adjust
    private String idPurchaseCurrent = "";
    private int typeIap;

    private boolean isPurchased = false;

    private Context mContext = null;

    public void setPurchaseListioner(PurchaseListioner purchaseListioner) {
        this.purchaseListioner = purchaseListioner;
    }

    /**
     * listener init billing app
     *
     * @param billingListener
     */
    public void setBillingListener(BillingListener billingListener) {
        this.billingListener = billingListener;
        if (isAvailable) {
            billingListener.onInitBillingListener(0);
            isInitBillingFinish = true;

        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Boolean getInitBillingFinish() {
        return isInitBillingFinish;
    }

    /**
     * listener init billing app with timeout
     *
     * @param billingListener
     * @param timeout
     */
    public void setBillingListener(BillingListener billingListener, int timeout) {
        this.billingListener = billingListener;
        if (isAvailable) {
            billingListener.onInitBillingListener(0);
            isInitBillingFinish = true;
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isInitBillingFinish) {
                    Log.e(TAG, "setBillingListener: timeout ");
                    isInitBillingFinish = true;
                    billingListener.onInitBillingListener(BillingClient.BillingResponseCode.ERROR);
                }
            }
        }, timeout);
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setConsumePurchase(boolean consumePurchase) {
        isConsumePurchase = consumePurchase;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, List<Purchase> list) {
            Log.e(TAG, "onPurchasesUpdated code: " + billingResult.getResponseCode());
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                for (Purchase purchase : list) {

                    List<String> sku = purchase.getSkus();
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                if (purchaseListioner != null)
                    purchaseListioner.onUserCancelBilling();
                Log.d(TAG, "onPurchasesUpdated:USER_CANCELED ");
            } else {
                Log.d(TAG, "onPurchasesUpdated:... ");
            }

            if(mContext != null) syncPurchaseState();
            else Log.i(TAG, "Can not sync because mContext is null");
        }
    };

    BillingClientStateListener purchaseClientStateListener = new BillingClientStateListener() {
        @Override
        public void onBillingServiceDisconnected() {
            isAvailable = false;
        }

        @Override
        public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
            Log.d(TAG, "onBillingSetupFinished:  " + billingResult.getResponseCode());

            if(mContext != null) syncPurchaseState();
            else Log.i(TAG, "Can not sync because mContext is null");

            if (billingListener != null && !isInitBillingFinish)
                billingListener.onInitBillingListener(billingResult.getResponseCode());

            isInitBillingFinish = true;
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                isAvailable = true;

                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(listINAPId).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                        if (list != null) {
                            Log.d(TAG, "onSkuINAPDetailsResponse: " + list.size());
                            skuListINAPFromStore = list;
                            isListGot = true;
                            addSkuINAPToMap(list);
                        }
                    }
                });

                params.setSkusList(listSubcriptionId).setType(BillingClient.SkuType.SUBS);
                billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
                        if (list != null) {
                            Log.d(TAG, "onSkuSubsDetailsResponse: " + list.size());
                            skuListSubsFromStore = list;
                            isListGot = true;
                            addSkuSubsToMap(list);
                        }
                    }
                });
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE || billingResult.getResponseCode() == BillingClient.BillingResponseCode.ERROR) {
                Log.e(TAG, "onBillingSetupFinished:ERROR ");

            }
        }
    };

    public static ProxPurchase getInstance() {
        if (instance == null) {
            instance = new ProxPurchase();
        }
        return instance;
    }

    private ProxPurchase() {

    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void addSubcriptionId(String id) {
        if (listSubcriptionId == null)
            listSubcriptionId = new ArrayList<>();
        listSubcriptionId.add(id);
    }

    public void addProductId(String id) {
        if (listINAPId == null)
            listINAPId = new ArrayList<>();
        listINAPId.add(id);
    }

    public void initBilling(final Application application) {
        mContext = application;

        listSubcriptionId = new ArrayList<>();
        listINAPId = new ArrayList<>();
        billingClient = BillingClient.newBuilder(application)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(purchaseClientStateListener);
    }

    public void initBilling(final Application application,
                            @Nullable List<String> listINAPId,
                            @Nullable List<String> listSubsId) {
        mContext = application;

        this.listSubcriptionId = (listSubsId == null) ? new ArrayList<>() : listSubsId;
        this.listINAPId = (listINAPId == null) ? new ArrayList<>() : listINAPId;

        billingClient = BillingClient.newBuilder(application)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(purchaseClientStateListener);
    }


    private void addSkuSubsToMap(List<SkuDetails> skuList) {
        for (SkuDetails skuDetails : skuList) {
            skuDetailsSubsMap.put(skuDetails.getSku(), skuDetails);
        }
    }

    private void addSkuINAPToMap(List<SkuDetails> skuList) {
        for (SkuDetails skuDetails : skuList) {
            skuDetailsINAPMap.put(skuDetails.getSku(), skuDetails);
        }
    }

    //check all id INAP + Subs
    public boolean isPurchased() {
        if (listINAPId != null) {
            Purchase.PurchasesResult result = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
            if (result.getResponseCode() == BillingClient.BillingResponseCode.OK && result.getPurchasesList() != null) {
                for (Purchase purchase : result.getPurchasesList()) {
                    for (String id : listINAPId) {
                        if (purchase.getSkus().contains(id)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (listSubcriptionId != null) {
            Purchase.PurchasesResult result = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
            if (result.getResponseCode() == BillingClient.BillingResponseCode.OK && result.getPurchasesList() != null) {
                for (Purchase purchase : result.getPurchasesList()) {
                    for (String id : listSubcriptionId) {
                        if (purchase.getSkus().contains(id)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // --------------------------- check purchase flow 2 ---------------------------
    /**
     * must call sync purchase state before call this method
     * @return
     */
    private boolean isSync = false;
    public boolean checkPurchased() {
        if(mContext == null) return false;
        if(!isSync) {
            syncPurchaseState();
        }
        return isPurchased;
    }

    public void syncPurchaseState() {
        if(mContext == null) return;
        getState();

        isSync = true;
    }

    private void savePurchaseState(boolean state) {
        Log.d(TAG, "savePurchaseState: " + state);
        isPurchased = state;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Purchase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_purchased", state).apply();
        editor.putLong("time_write", System.currentTimeMillis());
    }

    private boolean loadPreviousState() {
        return mContext.getSharedPreferences("Purchase", Context.MODE_PRIVATE).getBoolean("is_purchased", false);
    }

    private void getState() {
        isPurchased = loadPreviousState();

        if (listINAPId != null && listINAPId.size() > 0) {
            billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
                @Override
                public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                    Log.d(TAG, "onQueryPurchasesResponse: " + billingResult.getResponseCode());

                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            for (String id : listINAPId) {
                                if (purchase.getSkus().contains(id)) {
                                    savePurchaseState(true);
                                    return;
                                }
                            }
                        }
                        savePurchaseState(false);
                    }
                }
            });
        }
        if (listSubcriptionId != null && listSubcriptionId.size() > 0) {
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {
                @Override
                public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                    Log.d(TAG, "onQueryPurchasesResponse: 2 " + billingResult.getResponseCode());

                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            for (String id : listSubcriptionId) {
                                if (purchase.getSkus().contains(id)) {
                                    savePurchaseState(true);
                                    return;
                                }
                            }
                        }
                        savePurchaseState(false);
                    }
                }
            });
        }
    }
    // ---------------------------------------------------------------------------------


    //check  id INAP
    public boolean isPurchased(Context context, String productId) {
        Log.d(TAG, "isPurchased: " + productId);
        Purchase.PurchasesResult resultINAP = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (resultINAP.getResponseCode() == BillingClient.BillingResponseCode.OK && resultINAP.getPurchasesList() != null) {
            for (Purchase purchase : resultINAP.getPurchasesList()) {
                if (purchase.getSkus().contains(productId)) {
                    return true;
                }
            }
        }
        Purchase.PurchasesResult resultSubs = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
        if (resultSubs.getResponseCode() == BillingClient.BillingResponseCode.OK && resultSubs.getPurchasesList() != null) {
            for (Purchase purchase : resultSubs.getPurchasesList()) {
                if (purchase.getOrderId().equalsIgnoreCase(productId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void purchase(Activity activity) {
        if (productId == null) {
            Log.e(TAG, "Purchase false:productId null");
            Toast.makeText(activity, "Product id must not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        purchase(activity, productId);
    }


    public String purchase(Activity activity, String productId) {
        if (skuListINAPFromStore == null) {
            if (purchaseListioner != null)
                purchaseListioner.displayErrorMessage("Billing error init");
            return "";
        }
        SkuDetails skuDetails = skuDetailsINAPMap.get(productId);
//        for (int i = 0; i < skuListINAPFromStore.size(); i++) {
//            if (skuListINAPFromStore.get(i).getSku().equalsIgnoreCase(productId)) {
//                skuDetails = skuListINAPFromStore.get(i);
//            }
//        }
        if (skuDetails == null) {
            return "Product ID invalid";
        }

        idPurchaseCurrent = productId;
        typeIap = TYPE_IAP.PURCHASE;
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        BillingResult responseCode = billingClient.launchBillingFlow(activity, billingFlowParams);

        switch (responseCode.getResponseCode()) {

            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Billing not supported for type of request");
                return "Billing not supported for type of request";

            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                return "Error";

            case BillingClient.BillingResponseCode.ERROR:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Error completing request");
                return "Error completing request";

            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                return "Error processing request.";

            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                return "Selected item is already owned";

            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                return "Item not available";

            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                return "Play Store service is not connected now";

            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                return "Timeout";

            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Network error.");
                return "Network Connection down";

            case BillingClient.BillingResponseCode.USER_CANCELED:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Request Canceled");
                return "Request Canceled";

            case BillingClient.BillingResponseCode.OK:
                return "Subscribed Successfully";

            //}

        }
        return "";
    }

    public String subscribe(Activity activity, String SubsId) {

        if (skuListSubsFromStore == null) {
            if (purchaseListioner != null)
                purchaseListioner.displayErrorMessage("Billing error init");
            return "";
        }
        SkuDetails skuDetails = skuDetailsSubsMap.get(SubsId);

        idPurchaseCurrent = SubsId;
        typeIap = TYPE_IAP.SUBSCRIPTION;
        if (skuDetails == null) {
            return "SubsId invalid";
        }

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();

        BillingResult responseCode = billingClient.launchBillingFlow(activity, billingFlowParams);

        switch (responseCode.getResponseCode()) {

            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Billing not supported for type of request");
                return "Billing not supported for type of request";

            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                return "";

            case BillingClient.BillingResponseCode.ERROR:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Error completing request");
                return "Error completing request";

            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                return "Error processing request.";

            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                return "Selected item is already owned";

            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                return "Item not available";

            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
                return "Play Store service is not connected now";

            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                return "Timeout";

            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Network error.");
                return "Network Connection down";

            case BillingClient.BillingResponseCode.USER_CANCELED:
                if (purchaseListioner != null)
                    purchaseListioner.displayErrorMessage("Request Canceled");
                return "Request Canceled";

            case BillingClient.BillingResponseCode.OK:
                return "Subscribed Successfully";

            //}

        }
        return "";
    }

    public void consumePurchase() {
        if (productId == null) {
            Log.e(TAG, "Consume Purchase false:productId null ");
            return;
        }
        consumePurchase(productId);
    }

    public void consumePurchase(String productId) {
        Purchase pc = null;
        Purchase.PurchasesResult resultINAP = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
        if (resultINAP.getResponseCode() == BillingClient.BillingResponseCode.OK && resultINAP.getPurchasesList() != null) {
            for (Purchase purchase : resultINAP.getPurchasesList()) {
                if (purchase.getSkus().contains(productId)) {
                    pc = purchase;
                }
            }
        }
        if (pc == null)
            return;
        try {
            ConsumeParams consumeParams =
                    ConsumeParams.newBuilder()
                            .setPurchaseToken(pc.getPurchaseToken())
                            .build();

            ConsumeResponseListener listener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.e(TAG, "onConsumeResponse: OK");
                    }
                }
            };

            billingClient.consumeAsync(consumeParams, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePurchase(Purchase purchase) {

        //tracking adjust
        double price = getPriceWithoutCurrency(idPurchaseCurrent, typeIap);
        String currentcy = getCurrency(idPurchaseCurrent, typeIap);
//        AdjustApero.onTrackRevenuePurchase((float) price, currentcy);

        if (purchaseListioner != null)
            purchaseListioner.onProductPurchased(purchase.getOrderId(), purchase.getOriginalJson());
        if (isConsumePurchase) {
            ConsumeParams consumeParams =
                    ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.getPurchaseToken())
                            .build();

            ConsumeResponseListener listener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                    Log.d(TAG, "onConsumeResponse: " + billingResult.getDebugMessage());
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    }
                }
            };

            billingClient.consumeAsync(consumeParams, listener);
        } else {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                if (!purchase.isAcknowledged()) {
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                        @Override
                        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                            Log.d(TAG, "onAcknowledgePurchaseResponse: " + billingResult.getDebugMessage());
                        }
                    });
                }
            }
        }
    }

    //    public boolean handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        return bp.handleActivityResult(requestCode, resultCode, data);
//    }
//
    public String getPrice() {
        return getPrice(productId);
    }

    public String getPrice(String productId) {

        SkuDetails skuDetails = skuDetailsINAPMap.get(productId);
        if (skuDetails == null)
            return "";
        Log.e(TAG, "getPrice: " + skuDetails.getPrice());

        return skuDetails.getPrice();
    }

    public String getPriceSub(String productId) {
        SkuDetails skuDetails = skuDetailsSubsMap.get(productId);
        if (skuDetails == null)
            return "";
        return skuDetails.getPrice();
    }

    public String getIntroductorySubPrice(String productId) {
        SkuDetails skuDetails = skuDetailsSubsMap.get(productId);
        if (skuDetails == null) {
            return "";
        }
        return skuDetails.getPrice();
    }

    public String getCurrency(String productId, int typeIAP) {
        SkuDetails skuDetails = typeIAP == TYPE_IAP.PURCHASE ? skuDetailsINAPMap.get(productId) : skuDetailsSubsMap.get(productId);
        if (skuDetails == null) {
            return "";
        }
        return skuDetails.getPriceCurrencyCode();
    }

    public double getPriceWithoutCurrency(String productId, int typeIAP) {
        SkuDetails skuDetails = typeIAP == TYPE_IAP.PURCHASE ? skuDetailsINAPMap.get(productId) : skuDetailsSubsMap.get(productId);
        if (skuDetails == null) {
            return 0;
        }
        return skuDetails.getPriceAmountMicros();
    }
//
//    public String getOldPrice() {
//        SkuDetails skuDetails = bp.getPurchaseListingDetails(productId);
//        if (skuDetails == null)
//            return "";
//        return formatCurrency(skuDetails.priceValue / discount, skuDetails.currency);
//    }

    private String formatCurrency(double price, String currency) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(currency));
        return format.format(price);
    }

    private double discount = 1;

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }


    @IntDef({TYPE_IAP.PURCHASE, TYPE_IAP.SUBSCRIPTION})
    public @interface TYPE_IAP {
        int PURCHASE = 1;
        int SUBSCRIPTION = 2;
    }
}