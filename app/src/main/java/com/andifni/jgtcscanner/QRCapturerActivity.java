package com.andifni.jgtcscanner;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRCapturerActivity extends CaptureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.small_scanner);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
