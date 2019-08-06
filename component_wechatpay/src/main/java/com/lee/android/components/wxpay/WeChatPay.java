package com.lee.android.components.wxpay;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.json.JSONObject;

/**
 * @Auther:BryceLee leezx1993@163.com
 * @Date: 2019-08-06
 * @Desc:https://github.com/BryceLee/android-components-wechatpay
 */
public class WeChatPay {
  private String appId;
  private String partnerId;
  private String prepayId;
  private String nonceStr;
  private String timeStamp;
  private String packageValue;
  private String sign;
  private String extData;
  private Context context;
  private static volatile IWXAPI api = null;

  public static synchronized IWXAPI getWxapiInstance(Context context, String WechatAppId) {
    if (api == null) {
      synchronized (WeChatPay.class) {
        if (api == null) {
          api = WXAPIFactory.createWXAPI(context, WechatAppId);
        }
      }
    }
    return api;
  }

  public static class Builder {
    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String packageValue;
    private String sign;
    private Context context;

    public Builder with(Context context) {
      this.context = context;
      return this;
    }

    public Builder setAppid(String appid) {
      this.appId = appid;
      return this;
    }

    public Builder setPartnerId(String partnerId) {
      this.partnerId = partnerId;
      return this;
    }

    public Builder setPrepayId(String prepayId) {
      this.prepayId = prepayId;
      return this;
    }

    public Builder setNonceStr(String nonceStr) {
      this.nonceStr = nonceStr;
      return this;
    }

    public Builder setTimeStamp(String timeStamp) {
      this.timeStamp = timeStamp;
      return this;
    }

    public Builder setPackageValue(String packageValue) {
      this.packageValue = packageValue;
      return this;
    }

    public Builder setSign(String sign) {
      this.sign = sign;
      return this;
    }

    public WeChatPay create() {
      WeChatPay weChatPay = new WeChatPay();
      weChatPay.setContext(this.context);
      weChatPay.setAppId(this.appId);
      weChatPay.setPartnerId(this.partnerId);
      weChatPay.setPrepayId(this.prepayId);
      weChatPay.setNonceStr(this.nonceStr);
      weChatPay.setTimeStamp(this.timeStamp);
      weChatPay.setPackageValue(this.packageValue);
      weChatPay.setSign(this.sign);
      return weChatPay;
    }

    public WeChatPay createFromJson(String jsonStr) {
      if (this.context == null) {
        throw new NullPointerException("Set Context Please!");
      }
      //if you json key is differenct ,you can overrider this method or use Builder Parttern Api
      try {
        String content = new String(jsonStr);
        Log.e("get server pay params:", content);
        JSONObject json = new JSONObject(content);
        if (null != json && !json.has("retcode")) {
          this.appId = json.getString("appid");
          this.partnerId = json.getString("partnerid");
          prepayId = json.getString("prepayid");
          nonceStr = json.getString("noncestr");
          timeStamp = json.getString("timestamp");
          packageValue = json.getString("package");
          sign = json.getString("sign");
        } else {
          Log.d("WechatPay", "createFromJson_返回错误" + json.getString("retmsg"));
        }
      } catch (Exception e) {
        Log.e("WechatPay", "createFromJson_异常：" + e.getMessage());
      }
      return create();
    }
  }

  public void pay() {
    IWXAPI iwxapi = getWxapiInstance(context, appId);
    if (iwxapi.isWXAppInstalled()) {
      PayReq req = new PayReq();
      req.appId = this.appId;
      req.partnerId = this.partnerId;
      req.prepayId = this.prepayId;
      req.nonceStr = this.nonceStr;
      req.timeStamp = this.timeStamp;
      req.packageValue = this.packageValue;
      req.sign = this.sign;
      //req.extData			= "app data"; // optional
      iwxapi.sendReq(req);
    } else {
      Toast.makeText(this.context, "请安装微信客户端", Toast.LENGTH_SHORT).show();
    }
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getPrepayId() {
    return prepayId;
  }

  public void setPrepayId(String prepayId) {
    this.prepayId = prepayId;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getPackageValue() {
    return packageValue;
  }

  public void setPackageValue(String packageValue) {
    this.packageValue = packageValue;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getExtData() {
    return extData;
  }

  public void setExtData(String extData) {
    this.extData = extData;
  }

  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }
}

