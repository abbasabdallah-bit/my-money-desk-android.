package com.mymoneydesk.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Base64;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
  private static final int PICK_FILE=41, SAVE_FILE=42;
  private WebView web;
  private ValueCallback<Uri[]> pendingFiles;
  private byte[] pendingExport;
  @Override public void onCreate(Bundle state) {
    super.onCreate(state);
    web = new WebView(this);
    web.setWebViewClient(new WebViewClient() {
      @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        Uri uri=request.getUrl();
        if ("appassets.androidplatform.net".equals(uri.getHost()) && "/index.html".equals(uri.getPath())) {
          try { InputStream stream=getAssets().open("index.html"); return new WebResourceResponse("text/html", "UTF-8", stream); }
          catch (Exception ignored) { return null; }
        }
        return super.shouldInterceptRequest(view,request);
      }
    });
    WebSettings settings=web.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setAllowFileAccess(false);
    settings.setAllowContentAccess(false);
    web.addJavascriptInterface(new Object() {
      @JavascriptInterface public void saveFile(String filename, String base64, String mime) {
        try {
          pendingExport=Base64.decode(base64,Base64.DEFAULT);
          runOnUiThread(() -> {
            Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(mime == null ? "application/octet-stream" : mime.split(";")[0]);
            intent.putExtra(Intent.EXTRA_TITLE,filename);
            startActivityForResult(intent,SAVE_FILE);
          });
        } catch(Exception ignored) { }
      }
    }, "MoneyDeskNative");
    web.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
        if(pendingFiles!=null)pendingFiles.onReceiveValue(null);
        pendingFiles=callback;
        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"text/csv","text/comma-separated-values","application/json","text/plain"});
        startActivityForResult(intent,PICK_FILE);
        return true;
      }
    });
    setContentView(web);
    web.loadUrl("https://appassets.androidplatform.net/index.html");
  }
  @Override protected void onActivityResult(int request,int result,Intent data) {
    super.onActivityResult(request,result,data);
    if(request==PICK_FILE && pendingFiles!=null) {
      pendingFiles.onReceiveValue(result==RESULT_OK && data!=null && data.getData()!=null ? new Uri[]{data.getData()} : null);
      pendingFiles=null;
    }
    if(request==SAVE_FILE && pendingExport!=null) {
      if(result==RESULT_OK && data!=null && data.getData()!=null) {
        try(OutputStream out=getContentResolver().openOutputStream(data.getData())) { if(out!=null)out.write(pendingExport); }
        catch(Exception ignored) { }
      }
      pendingExport=null;
    }
  }
  @Override public void onBackPressed() { if(web.canGoBack())web.goBack(); else super.onBackPressed(); }
}
