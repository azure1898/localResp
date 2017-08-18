package com.its.common.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
/**
 * 支付宝相关工具类
 * @author liuhl
 */
public class AlipayUtils {
    /**
     * 支付宝退款请求
     * 
     * @param out_trade_no
     *            订单支付时传入的商户订单号
     * @param refund_amount
     *            需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     */
    static public void alipayRefundRequest(String out_trade_no, String refund_amount) throws AlipayApiException {

        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
                AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(out_trade_no);
        model.setRefundAmount("0.01");
        model.setRefundReason(refund_amount);
        model.setOutRequestNo(out_trade_no);
        alipay_request.setBizModel(model);

        AlipayTradeRefundResponse alipay_response = client.execute(alipay_request);
        System.out.println(alipay_response.getBody());

    }
}
