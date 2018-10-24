package com.example.taamefl2.qr_memory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {

    private Bitmap bild;
    private String loesungsWort;

    // Fill solution to array
    private String[][] loesungsArray = new String[25][25]; //@fixme: better to use arraylist or even different solution
    private String[] wortPaar = new String[2];
    private int fuellIndex = 0;
    private int counter = 0;

    final Context context = this;
    private LogBuch logging = new LogBuch();

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

        final EditText loesungsText = findViewById(R.id.loesungsText);

        final Button loesungArrayErstellen = findViewById(R.id.arrayFuellen);
        loesungArrayErstellen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loesungSpeichern(loesungsText.getText().toString());
                loesungsText.setText("");
            }
        });

        final Button logBuchButton = findViewById(R.id.inLogBuchEintragen);

        logBuchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean logbookInstalled = logging.checkIfLogbookInstalled(context);
                if (logbookInstalled) {
                    //@todo: get rid of all null-null pairs in loesungsArray
                    // or @fixme: work with arraylist
                    logging.passDataToLogbook(context, loesungsArray);
                }
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

    private void loesungSpeichern(String loesung){
        if (counter == 0){
            wortPaar[0] = loesung;
            counter++;
        }
        if (counter == 1){
            wortPaar[1] = loesung;
            loesungsArray[fuellIndex] = wortPaar;
            fuellIndex++;
        }
    }
}
