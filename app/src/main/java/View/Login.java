package View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.needfor.stockoverlay.R;
import Model.User;
import Module.DBAccess;

public class Login extends AppCompatActivity {

    TextInputEditText TextInputEditText_email, TextInputEditText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText_email = findViewById(R.id.TextInputEditText_Email);
        TextInputEditText_password = findViewById(R.id.TextInputEditText_Password);

        Button Button_Login = findViewById(R.id.Button_Login);
        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = TextInputEditText_email.getText().toString();
                String password = TextInputEditText_password.getText().toString();

                DBAccess DB = new DBAccess();
                DB.signIn(new User(email, password));
                Intent intent = new Intent(Login.this, Main.class);
                startActivity(intent);
            }

        });

        TextView search_psw = (TextView)findViewById(R.id.search_user);
        search_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search_psw = new Intent(Login.this, Search_User.class);
                startActivity(intent_search_psw);
            }
        });

        TextView sign_email = (TextView)findViewById(R.id.sign_email);
        search_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_search_psw = new Intent(Login.this, SignUp.class);
                startActivity(intent_search_psw);
            }
        });
    }

    /*class BtnOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Button_test:

                    break;
            }
        }
    } */
}
