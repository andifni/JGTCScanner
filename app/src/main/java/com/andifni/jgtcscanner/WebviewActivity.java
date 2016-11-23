package com.andifni.jgtcscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WebviewActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar progressBar;
    String endpoint;
    public static final int INTERNET_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        INTERNET_PERMISSION);

        }

        setContentView(R.layout.activity_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setProgress(0);

        webview = (WebView) findViewById(R.id.web);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
//                setTitle("Loading...");
                progressBar.setProgress(progress * 100); //Make the bar disappear after URL is loaded

                // Return the app name after finish loading
                if(progress >= 100) {
                    progressBar.setProgress(0);
                }
            }
        });
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);

        endpoint = PreferencesManager.getInstance(WebviewActivity.this).getEndPoint();
//        webview.loadUrl("http://www.scele.cs.ui.ac.aa.com");
        String data = getIntent().getStringExtra(MainActivity.KEY_DATA);
        postSomething(endpoint,data);

        Button scan = (Button) findViewById(R.id.again_btn);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesManager.getInstance(WebviewActivity.this).getEndPoint().isEmpty()) {
                    Toast.makeText(WebviewActivity.this, "End Point is empty, please enter an end point", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WebviewActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return;
                }
                IntentIntegrator intent = new IntentIntegrator(WebviewActivity.this);
                intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intent.setPrompt("Place a ticket's QR Code inside the viewfinder rectangle to scan\nThis screen will automatically closed in 15 seconds");
                intent.setTimeout(15000);
                intent.setCaptureActivity(QRCapturerActivity.class);
                intent.setCameraId(0);  // Use a specific camera of the device
                intent.setBeepEnabled(true);
                intent.setBarcodeImageEnabled(false);
                intent.initiateScan();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to connect to Internet", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class MyWebViewClient extends WebViewClient {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                postSomething(endpoint,result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void postSomething(String loc, String data) {
        String postData;
        try {
            postData = "data="+ URLEncoder.encode(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            postData = "data="+ data;
            e.printStackTrace();
        }
        webview.postUrl(loc, postData.getBytes());
    }

}
