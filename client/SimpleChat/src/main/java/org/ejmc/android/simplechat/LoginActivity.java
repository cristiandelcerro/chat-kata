package org.ejmc.android.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginPresenter = new LoginPresenter();
    }

    public void onLoginButtonClicked(View view) {
        EditText userNameButton = (EditText)findViewById(R.id.userName);
        EditText userPasswordButton = (EditText)findViewById(R.id.userPassword);
        String userName = userNameButton.getText().toString();
        String userPassword = userPasswordButton.getText().toString();

        if (!loginPresenter.login(userName, userPassword)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos.",
                                         Toast.LENGTH_SHORT);
            toast.show();
        }

        else {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userPassword", userPassword);
            startActivity(intent);
        }


    }

    public void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }
}