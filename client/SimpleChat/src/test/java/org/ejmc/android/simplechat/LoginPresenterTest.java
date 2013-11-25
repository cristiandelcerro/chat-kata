package org.ejmc.android.simplechat;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class LoginPresenterTest {

    private LoginPresenter loginPresenter;

    @Before
    public void setUp() throws Exception {
        loginPresenter = new LoginPresenter();
    }
    @Test
    public void withoutUserAndPasswordNotStartChat() {
        assertFalse(loginPresenter.login("", ""));
    }

    @Test
    public void withoutUserNotStartChat() {
        assertFalse(loginPresenter.login("", "sadfhkasjf"));
    }
    @Test
    public void withoutPasswordNotStartChat() {
        assertFalse(loginPresenter.login("asdfdasf", ""));
    }

    @Test
    public void withtUserAndPasswordStartChat() {
        assertTrue(loginPresenter.login("asdfasf", "asdfasfsa"));
    }
}
