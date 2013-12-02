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
        String userName = readEditText(R.id.userName);
        String userPassword = readEditText(R.id.userPassword);

        if (!loginPresenter.login(userName, userPassword))
            viewFailedLoginMessage();

        else
            startChatActivity(userName, userPassword);

    }

    private String readEditText(int id) {
        EditText editTextToRead = (EditText)findViewById(id);
        return editTextToRead.getText().toString();
    }

    private void viewFailedLoginMessage() {
        Toast toast = Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos.",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void startChatActivity(String userName, String userPassword) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userPassword", userPassword);
        startActivity(intent);
    }

    void setLoginPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }
}