package com.zsh.shopclient.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler{
	public static final String APPID = "wx879cac76e4a5b179";
	final String PARTJER_ID = "1454217302@1454217302";
	public static final String PARTNER_KEY = "0fcec5ac276a0028dbd72452f43e0bb5";
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	
	private Button gotoBtn, regBtn, launchBtn, checkBtn, scanBtn;
	
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try {
        	api.handleIntent(getIntent(), this);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			//goToGetMsg();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			//goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		default:
			break;
		}
	}

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {
		String result = null;
		
		Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();
		
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Intent intent = new Intent();
			intent.putExtra("state",true);
			setResult(Activity.RESULT_OK,intent);
			finish();
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			break;
		case BaseResp.ErrCode.ERR_UNSUPPORT:
			result = "不支持错误";
			break;
		default:
			result = "errcode_unknown";
			break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(cn.sharesdk.wechat.utils.WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(cn.sharesdk.wechat.utils.WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof cn.sharesdk.wechat.utils.WXAppExtendObject)) {
			cn.sharesdk.wechat.utils.WXAppExtendObject obj = (cn.sharesdk.wechat.utils.WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}
}