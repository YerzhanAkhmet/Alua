package com.example.alua.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alua.Classes.User;
import com.example.alua.R;

import io.paperdb.Paper;

public class AuthorizationActivity extends AppCompatActivity {

    EditText loginET, passwordET;
    Button createButton, cancelButton;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        loginET = findViewById(R.id.loginET);
        passwordET = findViewById(R.id.passwordET);
        createButton = findViewById(R.id.create_button);
        cancelButton = findViewById(R.id.cancel_button);
        Paper.init(this);
        fillStrings();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationActivity.super.onBackPressed();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(loginET.getText().toString(), passwordET.getText().toString());
                Paper.book().write("UserLoggedIn", user);
                Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void fillStrings(){
        if (!loginET.getText().toString().isEmpty() && !passwordET.getText().toString().isEmpty()) {
            if (Paper.book().contains("language")) {
                if (Paper.book().read("language").equals("kz")) {
                    createButton.setText(getString(R.string.sign_up_kz));
                    cancelButton.setText(getString(R.string.cancel_kz));
                } else if (Paper.book().read("language").equals("ru")) {
                    createButton.setText(getString(R.string.sign_up_ru));
                    cancelButton.setText(getString(R.string.cancel_ru));
                } else {
                    createButton.setText(getString(R.string.sign_up_en));
                    cancelButton.setText(getString(R.string.cancel_en));
                }
            } else {
                createButton.setText(getString(R.string.sign_up_en));
                cancelButton.setText(getString(R.string.cancel_en));
            }
        } else {
            createButton.setText(getString(R.string.sign_up_en));
            cancelButton.setText(getString(R.string.cancel_en));
        }
    }
}
