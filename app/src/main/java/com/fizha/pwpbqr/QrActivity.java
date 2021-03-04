package com.fizha.pwpbqr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrActivity extends AppCompatActivity {
    ImageView viewQr;
    TextInputEditText txtQr;
    MaterialButton generateQr;
    TextView txt;

    String editTextValue;
    Bitmap bitmap;
    public final static int QR_CODE_WIDTH = 500;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewQr = findViewById(R.id.view_qr);
        txtQr = findViewById(R.id.txt_qr);
        txt = findViewById(R.id.txt);
        generateQr = findViewById(R.id.generate_qr);

        generateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextValue = String.valueOf(txtQr.getText());

                try {
                    bitmap = TextToImageEncode(editTextValue);
                    viewQr.setImageBitmap(bitmap);
//                    Toast.makeText(getApplicationContext(), "Generate QR Code Berhasil", Toast.LENGTH_LONG);
                    txt.setVisibility(View.GONE);
                } catch (WriterException e){
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Generate QR Code Gagal", Toast.LENGTH_LONG);
                }
            }
        });
    }

    Bitmap TextToImageEncode(String value) throws WriterException{
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    value, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QR_CODE_WIDTH, QR_CODE_WIDTH, null
            );
        } catch (IllegalArgumentException i){
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }
}