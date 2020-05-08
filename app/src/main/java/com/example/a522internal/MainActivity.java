package com.example.a522internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_NAME = "LoginFile";
    private final String CHECK_BOX_PREFERENCES = "check";
    EditText login;
    EditText password;
    Button btnOk;
    CheckBox checkBox;
    Button btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        btnOk = findViewById(R.id.btnOk);
        checkBox = findViewById(R.id.checkBox);
        btnRegistration = findViewById(R.id.btnRegistration);


        final SharedPreferences settings = getSharedPreferences(CHECK_BOX_PREFERENCES, Context.MODE_PRIVATE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.edit().putBoolean(CHECK_BOX_PREFERENCES, isChecked).apply();
            }
        });
        checkBox.setChecked(settings.getBoolean(CHECK_BOX_PREFERENCES, false));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean externalStorageChecked = checkBox.isChecked();
                if (externalStorageChecked) {
                    checkUser(new File(getExternalFilesDir(null), FILE_NAME));
                } else {
                    checkUser(new File(getFilesDir(), FILE_NAME));
                }
            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean externalStorageChecked = checkBox.isChecked();

                if (externalStorageChecked) {
                    saveUser(new File(getExternalFilesDir(null), FILE_NAME));
                } else {
                    saveUser(new File(getFilesDir(), FILE_NAME));
                }
            }
        });
    }

    private void checkUser(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String login = bufferedReader.readLine();

            String loginText = this.login.getText().toString();

            if (!loginText.equals(login)) {
                Toast.makeText(MainActivity.this, R.string.tos, Toast.LENGTH_SHORT).show();
                return;
            }

            String password = bufferedReader.readLine();
            String passwordText = this.password.getText().toString();

            if (!passwordText.equals(password)) {
                Toast.makeText(MainActivity.this, R.string.tos1, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.tos2, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, R.string.tos, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUser(File file) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            String loginText = this.login.getText().toString();
            String passwordText = this.password.getText().toString();

            bufferedWriter.write(loginText + "\n" + passwordText);
            Toast.makeText(MainActivity.this, R.string.tos3, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, R.string.tos4, Toast.LENGTH_SHORT).show();
        }
    }
}
