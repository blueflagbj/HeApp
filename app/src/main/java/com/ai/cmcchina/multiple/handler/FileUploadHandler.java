package com.ai.cmcchina.multiple.handler;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import com.ai.cmcchina.crm.util.StringUtil;
import com.ai.cmcchina.multiple.util.FileUploadUtil;
import com.ai.cmcchina.multiple.util.ImgeUploadUtil;
import com.heclient.heapp.App;

import java.io.IOException;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;

/* renamed from: com.ai.cmcchina.multiple.handler.FileUploadHandler */
public abstract class FileUploadHandler extends Handler implements Runnable {
    public static final int MSG_ERROR = 0;
    public static final int MSG_SUCCESS = 1;
    private Activity context;
    protected String dataAction;
    protected String[] files;
    protected boolean isImage = true;
    protected ProgressDialog pDlg;
    protected Map<String, String> params;

    public abstract void onError(String str);

    public abstract void onSuccess(String str);

    public FileUploadHandler(Activity context2, String dataAction2, Map<String, String> params2, String[] files2) {
        this.context = context2;
        this.dataAction = dataAction2;
        this.params = params2;
        this.files = files2;
    }

    public FileUploadHandler(Activity context2, Map<String, String> params2, String[] files2, boolean isImage2) {
        this.context = context2;
        this.params = params2;
        this.files = files2;
        this.isImage = isImage2;
    }

    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String result = (String) msg.obj;
        if (result != null) {
            if (msg.what == 0) {
                onError(result);
            } else if (msg.what == 1) {
                onSuccess(result);
            }
        }
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        String result;
        try {
            String esopServerAddr = (String) ((App) this.context.getApplication()).getParams().get("ESOP_SERVER_ADDRESS");
            if (StringUtil.isBlank(esopServerAddr)) {
                throw new Exception("服务器地址获取失败！");
            }
            if (this.isImage) {
                result = ImgeUploadUtil.postWithAttachment(esopServerAddr, 40, this.params, this.files, true);
            } else {
                result = FileUploadUtil.post(esopServerAddr + this.params.get("BUSINESS_ACTION") + "?action=" + this.params.get("BUSINESS_METHOD") + "&opId=" + this.params.get("opId"), this.files, this.params);
            }
            sendMessage(obtainMessage(1, result));
        } catch (ClientProtocolException e) {

          //  G3Logger.debug(e);
            sendMessage(obtainMessage(0, "请检查网络" + e.getMessage()));
        } catch (IOException e2) {
          //  G3Logger.debug(e2);
            sendMessage(obtainMessage(0, "连接服务器出错，请检查网络"));
        } catch (Exception e3) {
          //  G3Logger.debug(e3);
            sendMessage(obtainMessage(0, e3.getMessage()));
        }
    }
}