package com.example.myapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// ─────────────────────────────────────────────────────────
// Import ADS — jika enable_ads = false, baris ini
// akan di-comment otomatis oleh GitHub Actions
// ─────────────────────────────────────────────────────────
// ADS_START
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
// ADS_END

// ─────────────────────────────────────────────────────────
// Import BILLING — jika enable_billing = false, baris ini
// akan di-comment otomatis oleh GitHub Actions
// ─────────────────────────────────────────────────────────
// BILLING_START
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
// BILLING_END

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ADS_START
        initAds()
        // ADS_END

        // BILLING_START
        initBilling()
        // BILLING_END
    }

    // ─────────────────────────────────────────────────────
    // Fungsi ADS — jika enable_ads = false,
    // seluruh blok ini akan di-comment otomatis
    // ─────────────────────────────────────────────────────
    // ADS_START
    private fun initAds() {
        MobileAds.initialize(this) { initStatus ->
            // Ads sudah siap
        }
        val adRequest = AdRequest.Builder().build()
        // Pasang banner atau interstitial di sini
    }
    // ADS_END

    // ─────────────────────────────────────────────────────
    // Fungsi BILLING — jika enable_billing = false,
    // seluruh blok ini akan di-comment otomatis
    // ─────────────────────────────────────────────────────
    // BILLING_START
    private fun initBilling() {
        val billingClient = BillingClient.newBuilder(this)
            .setListener { billingResult, purchases -> }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Billing siap
                }
            }
            override fun onBillingServiceDisconnected() {
                // Coba sambung ulang
            }
        })
    }
    // BILLING_END
}
