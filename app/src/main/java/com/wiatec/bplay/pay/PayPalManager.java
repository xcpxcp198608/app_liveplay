package com.wiatec.bplay.pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import java.math.BigDecimal;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.px.common.utils.Logger;
import org.json.JSONException;
;

/**
 * Created by patrick on 31/10/2017.
 * create time : 10:16 AM
 */


public class PayPalManager {

    /**
     * activity pay
     */
    public static void pay(Activity activity, PayInfo payInfo){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(payInfo.getPrice()),
                payInfo.getCurrency(),
                payInfo.getDescription(),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalConfig.getConfiguration());
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        activity.startActivityForResult(intent, PayPalConfig.PAY_REQUEST_CODE);
    }

    /**
     * fragment pay
     */
    public static void pay(Fragment fragment, PayInfo payInfo){
        try {
            PayPalPayment payment = new PayPalPayment(new BigDecimal(payInfo.getPrice()),
                    payInfo.getCurrency(),
                    payInfo.getDescription(),
                    PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent = new Intent(fragment.getContext(), PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, PayPalConfig.getConfiguration());
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            fragment.startActivityForResult(intent, PayPalConfig.PAY_REQUEST_CODE);
        }catch (Exception e){
            Logger.d(e.getMessage());
            Logger.d(e.getMessage());
        }
    }

    /**
     * 支付页面的onActivityResult中调用，支付页面需要实现OnPayResultListener接口，在实现方法中处理回调逻辑
     */
    public static void payResult(int requestCode, int resultCode, Intent data,
                          OnPayResultListener onPayResultListener) {
        if (requestCode == PayPalConfig.PAY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                onPayResultListener.customerCancel("user cancel pay");
                return;
            }
            if (resultCode == Activity.RESULT_OK) {
                try {
                    PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (paymentConfirmation != null) {
                        String paymentId = paymentConfirmation.toJSONObject().getJSONObject("response").getString("id");
                        Logger.d(paymentId);
                        if (!TextUtils.isEmpty(paymentId)) {
                            onPayResultListener.paySuccess(paymentId);
                        }
                        // 保留订单付款信息
                        //savePaymentInfo();
                        // 展示成功界面
                        //showSuccessful();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public interface OnPayResultListener {
        void paySuccess(String paymentId);
        void customerCancel(String error);
    }
}