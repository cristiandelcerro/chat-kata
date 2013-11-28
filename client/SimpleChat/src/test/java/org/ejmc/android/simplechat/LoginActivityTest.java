package org.ejmc.android.simplechat;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricTestRunner.class)
public class LoginActivityTest {

    private LoginActivity loginActivity;
    private Button loginButton;
    private EditText userNameText;
    private EditText passwordText;

    @Before
    public void setUp() throws Exception {
        loginActivity = new LoginActivity();
        loginActivity.onCreate(null);

        loginButton = (Button)loginActivity.findViewById(R.id.loginButton);
        userNameText = (EditText)loginActivity.findViewById(R.id.userName);
        passwordText = (EditText)loginActivity.findViewById(R.id.userPassword);
    }

    @Test
    public void shouldHaveProperAppName() {
        String appName = new LoginActivity().getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("SimpleChat"));
    }

    @Test
    public void withoutUserAndPasswordNotStartChat() {
        setUserAndPassword("", "", false);
        loginButton.performClick();
        assertActivityNotStarted();
    }

    @Test
    public void withoutUserNotStartChat() {
        setUserAndPassword("", "sdafdasf", false);
        loginButton.performClick();
        assertActivityNotStarted();
    }
    @Test
    public void withoutPasswordNotStartChat() {
        setUserAndPassword("skldjflaskd", "", false);
        loginButton.performClick();
        assertActivityNotStarted();
    }

    @Test
    public void withtUserAndPasswordStartChat() {
        setUserAndPassword("skldjflaskd", "sdfldsajdfl", true);
        loginButton.performClick();
        assertActivityStarted(ChatActivity.class);
    }

    private void setUserAndPassword(String userName, String userPassword, boolean expectedResult) {
        LoginPresenter mockedLoginPresenter = mock(LoginPresenter.class);
        when(mockedLoginPresenter.login(userName, userPassword)).thenReturn(expectedResult);
        loginActivity.setLoginPresenter(mockedLoginPresenter);

        userNameText.setText(userName);
        passwordText.setText(userPassword);
    }

    private void assertActivityStarted(Class<? extends Activity> classToStart) {
        ShadowActivity shadowActivity = shadowOf(loginActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(classToStart.getName()));
    }

    private void assertActivityNotStarted() {
        ShadowActivity shadowActivity = shadowOf(loginActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertNull(startedIntent);
    }
}