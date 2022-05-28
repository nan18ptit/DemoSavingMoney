package com.example.savingmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.savingmoney.dal.SQLiteHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText eUser, ePassWord;
    private Button btLogin, btSignUp;
    private SQLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eUser = findViewById(R.id.eUser);
        ePassWord = findViewById(R.id.ePassword);
        btLogin = findViewById(R.id.btLogin);
        btSignUp = findViewById(R.id.btSingUp);
        db = new SQLiteHelper(this);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = eUser.getText().toString().trim();
                String pass = ePassWord.getText().toString().trim();
                if(user.equalsIgnoreCase("") || pass.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else{
                    Boolean checkAccount = db.checkAccount(user,pass);
                    if(checkAccount == true){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}