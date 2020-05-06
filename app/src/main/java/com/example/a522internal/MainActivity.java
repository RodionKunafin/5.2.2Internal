package com.example.a522internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText login;
    EditText password;
    Button btnOk;
    Button btnRegistration;
    CheckBox checkBox;
    Button btnSave;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        btnOk = findViewById(R.id.btnOk);
        btnRegistration = findViewById(R.id.btnRegistration);
        checkBox = findViewById(R.id.checkBox);
        btnSave = findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    SharedPreferences settings = getSharedPreferences("check", 0);
                    settings.edit().putBoolean("check",true).commit();
                    Toast toast = Toast.makeText(getApplicationContext(),"Данные сохранены во внешнее хранилище", Toast.LENGTH_LONG);
                    toast.show();
                    Loadtxt();
                }
                else {

                }

            }

        });




        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("LoginFile", MODE_PRIVATE)))) {
                    writer.write("Login");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("PasswordFile", MODE_PRIVATE)))) {
                    writer.write("Password");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(openFileInput("LoginFile")));
                    StringBuilder sb = new StringBuilder();
                    String line = reader.readLine();
                    Toast toast = Toast.makeText(getApplicationContext(),"Данные сохранены", Toast.LENGTH_LONG);
                    toast.show();
                    while (line != null) {
                        sb.append(line);
                        line = reader.readLine();
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    reader = new BufferedReader(new InputStreamReader(openFileInput("PasswordFile")));
                    StringBuilder sb = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = reader.readLine();
                    }
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    public void Loadtxt() {
        if (isExternalStorageWriteble()) {
            File file = new File(getApplicationContext().getExternalFilesDir(null),"log.txt");
        }
    }
    public boolean isExternalStorageWriteble() {
        String state = Environment.getExternalStorageState();
        return Environment.DIRECTORY_DOWNLOADS.equals(state);
    }
    private boolean load() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("check", false);
    }

}
