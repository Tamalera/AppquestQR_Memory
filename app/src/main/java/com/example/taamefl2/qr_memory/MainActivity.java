package com.example.taamefl2.qr_memory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {

    private Bitmap bild;
    private String loesungsWort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button fotoAufnehmenButton = findViewById(R.id.fotoAufnehmen);
        final ImageView bildAnzeigen = findViewById(R.id.fotoAnzeigen);
        final TextView textAnzeigen = findViewById(R.id.textAnzeigen);

        fotoAufnehmenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrCodeBildMachen();
                bildAnzeigen.setImageBitmap(bild);
                textAnzeigen.setText(loesungsWort);
            }
        });
    }

    public void qrCodeBildMachen() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE
                && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            String path = extras.getString(
                    Intents.Scan.RESULT_BARCODE_IMAGE_PATH);

            bild  = bitmapAusgeben(path);

            // Ein Bitmap zur Darstellung erhalten wir so:
            // Bitmap bmp = BitmapFactory.decodeFile(path)

            String code = extras.getString(
                    Intents.Scan.RESULT);

            loesungsWort  = codeAusgeben(code);
        }
    }

    private Bitmap bitmapAusgeben(String path){
        return BitmapFactory.decodeFile(path);
    }

    private String codeAusgeben(String code){
        return code;
    }
}

