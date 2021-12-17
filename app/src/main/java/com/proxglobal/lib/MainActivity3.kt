package com.proxglobal.lib

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.proxglobal.purchase.ProxPurchase
import com.proxglobal.purchase.function.PurchaseListioner

class MainActivity3 : BaseActivity() {
    lateinit var button1 : Button
    lateinit var button2 : Button
    lateinit var button3 : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        setButtonContent()

        ProxPurchase.getInstance().setPurchaseListioner(object : PurchaseListioner {
            override fun onProductPurchased(productId: String?, transactionDetails: String?) {
                transactionDetails?.let {
                    Log.d(TAG, "Message: $it")
                }
            }

            override fun displayErrorMessage(errorMsg: String?) {
                errorMsg?.let {
                    Log.d(TAG, "Message: $it")
                }
            }

            override fun onUserCancelBilling() {

            }

        })
    }

    private fun setButtonContent() {
        button1 = findViewById(R.id.btn_purchase_test1)
        button2 = findViewById(R.id.btn_purchase_test2)
        button3 = findViewById(R.id.btn_purchase_test3)

        button1.text = ProxPurchase.getInstance().getPrice(BuildConfig.id_test1_purchase)
        button2.text = ProxPurchase.getInstance().getPriceSub(BuildConfig.id_test2_subs)
        button3.text = ProxPurchase.getInstance().getPriceSub(BuildConfig.id_test3_subs)

        Toast.makeText(this, ProxPurchase.getInstance().checkPurchased().toString(), Toast.LENGTH_SHORT).show()
        button1.setOnClickListener {
            ProxPurchase.getInstance().purchase(this, BuildConfig.id_test1_purchase)
        }

        button2.setOnClickListener {
            ProxPurchase.getInstance().subscribe(this, BuildConfig.id_test2_subs)
        }

        button3.setOnClickListener {
            ProxPurchase.getInstance().subscribe(this, BuildConfig.id_test3_subs)
        }
    }
}