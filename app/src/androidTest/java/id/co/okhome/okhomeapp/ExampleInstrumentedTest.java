package id.co.okhome.okhomeapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import id.co.okhome.okhomeapp.lib.JoSharedPreference;
import id.co.okhome.okhomeapp.model.UserModel;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        UserModel m = new UserModel();
        m.credit = "100";

        JoSharedPreference.with(appContext).push("MyUserModel22", m);

        UserModel m2 = JoSharedPreference.with(appContext).get("MyUserModel22");

        JoSharedPreference.with(appContext).push("MyUserModel22", null);

        UserModel m3 = JoSharedPreference.with(appContext).get("MyUserModel22");

        int i = 0;
        i++;
        assertEquals("id.co.okhome.okhomeapp", appContext.getPackageName());
    }
}
