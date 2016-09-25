package id.co.okhome.okhomeapp;

import org.junit.Test;

import java.lang.reflect.Constructor;

import static java.lang.System.out;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    public static class Tv{
        int channel = 0;

        public void channelUp(){
            channel ++;
        }

        public int getCurrentChannel(){
            return channel;
        }
    }

    public static class JoTv extends Tv{

        @Override
        public void channelUp() {
            super.channelUp();
            channel += 10;
        }
    }


    @Test
    public void sdfdasf() throws Exception {

        Class fragmentClass = SampleModel.class;
        Constructor[] allConstructors = fragmentClass.getDeclaredConstructors();
        out.println("O " + " ::: " + "왓다" + allConstructors.length);
        SampleModel sampleModel = (SampleModel)allConstructors[0].newInstance();
        out.println(sampleModel.aa);
    }

    public <T> T getParam(String key){
        return (T)key;
    }

    private void print(Object s){
        final String tag = "SIBALSO";
        out.println(tag + " ::: " + s);
    }

    public class Sampe2Model extends SampleModel{

    }
}