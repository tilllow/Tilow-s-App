package com.hfad.exploreshopping;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ActivityQrCodeScanner extends AppCompatActivity {

    AppCompatButton btnScanNow;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // Check condition
        if (intentResult.getContents() != null) {
            String qrCode = intentResult.getContents();
            Intent intent = new Intent(ActivityQrCodeScanner.this, ActivityProductInfoPage.class);
            intent.putExtra("QR_CODE", qrCode);
            startActivity(intent);
        } else {
            Toast.makeText(ActivityQrCodeScanner.this, "OOPS...You did not scan anything", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);

        btnScanNow = findViewById(R.id.btnScanNow);

        btnScanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(ActivityQrCodeScanner.this);

                // Set prompt text
                intentIntegrator.setPrompt("For flash use volume up key");
                // Set beep
                intentIntegrator.setBeepEnabled(true);
                //Locked orientation
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class);
                //Initiate scan
                intentIntegrator.initiateScan();
            }
        });
    }
}