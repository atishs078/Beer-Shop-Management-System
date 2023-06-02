package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Check_Stock extends AppCompatActivity {
    TableLayout tableLayout;
    FirebaseFirestore fstore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stock);
        tableLayout=findViewById(R.id.mytable);
        fstore=FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadData();
    }

    private void loadData() {
        CollectionReference collectionReference = fstore.collection("Stock");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // Clear data except for the first row
                    int childCount = tableLayout.getChildCount();
                    if (childCount > 1) {
                        tableLayout.removeViews(1, childCount - 1);
                    }

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String Name = documentSnapshot.getString("Name");
                        String noofBottel = documentSnapshot.getString("NoOfBottel");
                        String Quantity = documentSnapshot.getString("Quantity");
                        String Type = documentSnapshot.getString("Type");
                        TableRow tableRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        tableRow.setLayoutParams(layoutParams);
                        TextView textView = createTextView(Name);
                        TextView textView1 = createTextView(Quantity);
                        TextView textView2 = createTextView(Type);
                        TextView textView3 = createTextView(noofBottel);
                        tableRow.addView(textView);
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);
                        tableRow.addView(textView3);
                        tableLayout.addView(tableRow);
                        addCellBorders(textView);
                        addCellBorders(textView1);
                        addCellBorders(textView2);
                        addCellBorders(textView3);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Check_Stock.this, "Error in Fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(Check_Stock.this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return textView;
    }

    private void addCellBorders(TextView textView) {
        textView.setBackgroundResource(R.drawable.tableborder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            progressDialog.setMessage("Refreshing Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
