# android-components-wechatpay
A android component of wechatpay for dev qucikly.
# Download
```
Step1 .Add it in your root build.gradle at the end of repositories:
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency :
dependencies {
	        implementation 'com.github.BryceLee:android-components-wechatpay:1.0'
	}
```
# Firstly (this step is necessary ，otherwise wechat pay sdk can not work)
### add the WXPayEntryActivity in your_app_packagename.wxapi.WXPayEntryActivity
```
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, "your wechat appid");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //add your code logic...
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                Log.d(TAG, "success");
            } else if (resp.errCode == BaseResp.ErrCode.ERR_SENT_FAILED || resp.errCode == BaseResp.ErrCode.ERR_COMM) {
                Log.d(TAG, "failed");
            } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                Log.d(TAG, "user cancel");
            }
        }
        finish();
    }
}
```
### add WXPayEntryActivity in your AndroidManifest.xml:
```
    <activity
        android:name=".wxapi.WXPayEntryActivity"
        android:exported="true"
        android:launchMode="singleTop"/> <!-- WX END -->
```
# How to use
## If your order message from server have the same data struture with wechat document,you can use this componet:
```
 WeChatPay weChatPay = new WeChatPay.Builder()
    .with(this)
    .setAppid(info.getAppid())
    .createFromJson("the json String of your order message from your service")
    weChatPay.pay();
```
## You can also use builder pattern api like this:
```
 WeChatPay weChatPay = new WeChatPay.Builder()
        .with(this)
        .setAppid(info.getAppid())
        .setPartnerId(info.getPartnerid())
        .setPrepayId(info.getPrepayid())
        .setNonceStr(info.getNoncestr())
        .setPackageValue(info.getPackages())
        .setSign(info.getSign())
        .setTimeStamp(info.getTimestamp())
        .create();
    weChatPay.pay();
```
## attention
### you should add configuration on the wechat background,for example:package name,signature and so on.
[wechat pay backgroud setting](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5)

[wechat pay document](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2)