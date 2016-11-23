package com.andifni.jgtcscanner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        textView = (TextView) findViewById(R.id.endpoint_txt);
        TextView about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("About");
                builder.setMessage("Created by : Andi (andifni@gmail.com)");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (PreferencesManager.getInstance(this).getEndPoint().isEmpty() && textView.getText().toString().isEmpty()) {
            Toast.makeText(this, "End Point cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Save changes?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (textView.getText().toString().isEmpty()){
                        Toast.makeText(SettingsActivity.this, "End Point cannot be empty!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PreferencesManager.getInstance(SettingsActivity.this).setEndPoint(textView.getText().toString());
                    Toast.makeText(SettingsActivity.this, "End point has changed!", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                    SettingsActivity.super.onBackPressed();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    SettingsActivity.super.onBackPressed();
                }
            });
            alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }
    }
}
