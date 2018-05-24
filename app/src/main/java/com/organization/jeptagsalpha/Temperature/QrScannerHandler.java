package com.organization.jeptagsalpha.Temperature;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.main.TemperatureHome;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScannerHandler extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mscanner;
   Help handler;
    String Data;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner_handler);
        Log.d("HELLO<><><><>","qr scanner called");
        mscanner= new ZXingScannerView(this);
        mscanner = new ZXingScannerView(this);
        setContentView(mscanner);
        mscanner.setResultHandler(this);

//                handler.ZINGSCANNERVIEW=1;

        // Start camera on resume
        mscanner.startCamera();
        Help.CAMERA_STATUS=1;
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.d("HELLO<><><><>", rawResult.getText()); // Prints scan results
        // Prints the scan format (qrcode, pdf417 etc.)
        Log.d("HELLO<><><><>", rawResult.getBarcodeFormat().toString());
        Data = rawResult.getText();

        Toast.makeText(this, Data, Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(Scanner_Activity.this);
//        builder.setTitle("Scan Result");
//        builder.setMessage(rawResult.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();
        mscanner.stopCameraPreview();
        mscanner.stopCamera();
       Help.DATA=Data;
        Intent i=new Intent(this,TemperatureHome.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
    public void onPause() {

        super.onPause();
        if(Help.CAMERA_STATUS==1){
            mscanner.stopCamera();
        }
        // Stop camera on pause
        Log.d("Hello<><><><>", "OnPause of scanner is called");
    }

}
