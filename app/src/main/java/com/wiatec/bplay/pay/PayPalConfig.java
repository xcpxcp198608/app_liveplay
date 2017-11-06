package com.wiatec.bplay.pay;

import android.content.Context;
import android.content.Intent;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalService;

import static com.px.common.utils.CommonApplication.context;


public class PayPalConfig{

    private static final String CLIENT_ID = "AUIoEwXGBWT4jsSRICOU5HTTXrop7OFsEQrIyANkbwFxL982Yu2eSMkolwAa8E0zVslo6d2E7U0mWvvz";
    public static final int PAY_REQUEST_CODE = 0X1026;
    private static final String PRODUCTION_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static PayPalConfiguration configuration;

    static {
        configuration = new PayPalConfiguration()
                .environment(PRODUCTION_ENVIRONMENT)
                .clientId(CLIENT_ID)
                .acceptCreditCards(true);
    }


    public static PayPalConfiguration getConfiguration(){
        return configuration;
    }
    /**
     * 在需要调用支付页面的onCreate中启动paypal服务
     */
    public static void startPayPalService(Context context){
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        context.startService(intent);
    }

    /**
     * 在调用支付服务的页面的onDestroy中停止paypal服务
     */
    public static void  stopPayPalService(Context context){
        context.stopService(new Intent(context, PayPalService.class));
    }
}
