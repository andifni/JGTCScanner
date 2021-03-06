package com.andifni.jgtcscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_DATA = "DATA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scan = (Button) findViewById(R.id.scan_btn);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesManager.getInstance(MainActivity.this).getEndPoint().isEmpty()) {
                    Toast.makeText(MainActivity.this, "End Point is empty, please enter an end point", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return;
                }
                IntentIntegrator intent = new IntentIntegrator(MainActivity.this);
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

        Button goBtn = (Button) findViewById(R.id.go_btn);
        final TextView code = (TextView) findViewById(R.id.code_txt);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesManager.getInstance(MainActivity.this).getEndPoint().isEmpty()) {
                    Toast.makeText(MainActivity.this, "End Point is empty, please enter an end point", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return;
                }
                if (code.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Code cannot empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra(KEY_DATA, code.getText().toString());
                startActivity(intent);
            }
        });

        TextView setting = (TextView) findViewById(R.id.setting_btn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
                intent.putExtra(KEY_DATA, result.getContents());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
