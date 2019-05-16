package com.example.alua.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alua.Classes.User;
import com.example.alua.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText loginET, passwordET;
    Button loginButton, authorizeButton;
    String toast;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passwordET);
        loginButton = findViewById(R.id.login_button);
        authorizeButton = findViewById(R.id.authorize_button);
        Paper.init(LoginActivity.this);
        fillStrings();
        authorizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AuthorizationActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty()) {
                    if (user.getLogin().equals(loginET.getText().toString()) && user.getPassword().equals(passwordET.getText().toString())) {
                        Paper.book().write("UserLoggedIn", user);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fillStrings(){
        if (Paper.book().contains("language")) {
            if (Paper.book().read("language").equals("kz")) {
                toast = getString(R.string.wrong_data_kz);
                loginButton.setText(getString(R.string.login_kz));
                authorizeButton.setText(getString(R.string.sign_up_kz));
            } else if (Paper.book().read("language").equals("ru")) {
                toast = getString(R.string.wrong_data_ru);
                loginButton.setText(getString(R.string.login_ru));
                authorizeButton.setText(getString(R.string.sign_up_ru));
            } else {
                toast = getString(R.string.wrong_data_en);
                loginButton.setText(getString(R.string.login_en));
                authorizeButton.setText(getString(R.string.sign_up_en));
            }
        } else {
            toast = getString(R.string.wrong_data_en);
            loginButton.setText(getString(R.string.login_en));
            authorizeButton.setText(getString(R.string.sign_up_en));
        }
    }
}
