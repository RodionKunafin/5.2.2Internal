package com.example.a522internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    EditText login;
    EditText password;
    Button btnOk;
    CheckBox checkBox;
    Button btnSave;
    Button btnRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        btnOk = findViewById(R.id.btnOk);
        checkBox = findViewById(R.id.checkBox);
        btnSave = findViewById(R.id.btnSave);
        btnRegistration = findViewById(R.id.btnRegistration);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    SharedPreferences settings = getSharedPreferences("check", 0);
                    settings.edit().putBoolean("check", true).commit();
                    Toast toast = Toast.makeText(getApplicationContext(), "Данные сохранены во внешнее хранилище", Toast.LENGTH_LONG);
                    toast.show();
                    Loadtxt();
                }
            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            {
                SharedPreferences settings = getSharedPreferences("check", Context.MODE_PRIVATE);
                settings.edit().putBoolean("check", false).apply();
                Toast.makeText(getApplicationContext(), "Данные сохранены во внутреннее хранилище", Toast.LENGTH_LONG).show();
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

            @Override
            public void onClick(View v) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getExternalFilesDir(null), "LoginFile")))) {
                    writer.write(login.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getExternalFilesDir(null), "PasswordFile")))) {
                    writer.write(password.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("LoginFile")))) {
                    String line = reader.readLine();

                    String loginText = login.getText().toString();

                    if (!loginText.equals(line)) {
                        Toast.makeText(MainActivity.this, "Пользователь не найден!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Пользователь не найден!", Toast.LENGTH_SHORT).show();
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("PasswordFile")))) {
                    String line = reader.readLine();

                    String passwordText = password.getText().toString();

                    if (!passwordText.equals(line)) {
                        Toast.makeText(MainActivity.this, "Пароль введён неверно!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Всё ок!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(openFileInput("LoginFile")));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            Toast toast = Toast.makeText(getApplicationContext(), "Данные сохранены", Toast.LENGTH_LONG);
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


    public void Loadtxt() {
        if (isExternalStorageWriteble()) {
            File file = new File(getApplicationContext().getExternalFilesDir(null), "log.txt");
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
