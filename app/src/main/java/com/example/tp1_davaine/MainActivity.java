package com.example.tp1_davaine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndSendSMS();
            }
        });
    }

    public void checkPermissionAndSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            sendSMS();
        }
    }

    public void sendSMS() {
        EditText editTextPhoneNumber = findViewById(R.id.edit_text_number);
        EditText editTextMessage = findViewById(R.id.edit_text_message);

        String numero = editTextPhoneNumber.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
            numero = numero.replaceAll("\\s+", ""); // pour enlever les espaces si différents numéros sont saisis avec des espaces
            String numeros[] = numero.split(";"); // créer une liste de numéros
            SmsManager smsManager = getSystemService(SmsManager.class);
            if (smsManager != null) {
                for (String num : numeros) {

                    smsManager.sendTextMessage(num, null, message, null, null);
                }
                if (numeros.length >= 2) {
                    Toast.makeText(this, "SMS envoyé aux différents destinaires", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "SMS envoyé à l'unique destinataire", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Erreur lors de l'obtention du SmsManager", Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "Permission d'envoyer des SMS refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
