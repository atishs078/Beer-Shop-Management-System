package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
EditText Offline,Online;
Button Submit;
TextView Total;
FirebaseAuth fAuth;
FirebaseFirestore fstore;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Offline=findViewById(R.id.offlinepay);
        Online=findViewById(R.id.onlinepay);
        Submit=findViewById(R.id.Submit);
        Total=findViewById(R.id.total);
        Offline.addTextChangedListener(textWatcher);
        Online.addTextChangedListener(textWatcher);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String offlinepay=Offline.getText().toString().trim();
                String onlinepay=Online.getText().toString().trim();
                String total=Total.getText().toString().trim();
                fAuth = FirebaseAuth.getInstance();
                fstore = FirebaseFirestore.getInstance();
                String user = fAuth.getCurrentUser().getUid();
                if (TextUtils.isEmpty(offlinepay)) {
                    Offline.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(onlinepay)) {
                    Online.setError("Required");
                    return;
            }
                progressDialog = new ProgressDialog(Payment.this);
                progressDialog.setMessage("Submitting Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                CollectionReference collectionReference = fstore.collection("Payment");
                Map<String,Object> pay=new HashMap<>();
                pay.put("Time", FieldValue.serverTimestamp());
                pay.put("Offline Mode",offlinepay);
                pay.put("Online Mode",onlinepay);
                pay.put("Total",total);
                collectionReference.add(pay).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressDialog.dismiss();
                        showCompletionDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Payment.this, "Something Went Wrong.....", Toast.LENGTH_SHORT).show();
                    }
                });
        }

    });
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String offlineText = Offline.getText().toString();
            String onlineText = Online.getText().toString();

            if (!offlineText.isEmpty() && !onlineText.isEmpty()) {
                try {
                    int offlineAmount = Integer.parseInt(offlineText);
                    int onlineAmount = Integer.parseInt(onlineText);
                    int result = offlineAmount + onlineAmount;
                    Total.setText(String.valueOf(result));
                } catch (NumberFormatException e) {
                    // Handle exception - Input is not a valid integer
                    e.printStackTrace();
                }
            } else {
                // Handle case when either or both EditText fields are empty
                Total.setText("");
            }
        }
    };
    private void showCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Data Submitted")
                .setMessage("The data has been submitted successfully.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), TodaySale.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}