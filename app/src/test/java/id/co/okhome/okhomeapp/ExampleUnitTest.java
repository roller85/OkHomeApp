package id.co.okhome.okhomeapp;

import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

//        Constructor c = SampleModel.class.getDeclaredConstructor();
//        c.setAccessible(true);


        Integer a= getParam("a");
        instance(this, Sampe2Model.class);


    }

    private void instance(Object obj, Class targetClass) throws Exception{
        print(obj.getClass().getName());

        if(targetClass.isMemberClass()){
            Constructor innerConstructor = targetClass.getDeclaredConstructor(obj.getClass());
            Object newObject = innerConstructor.newInstance(obj);

            print(newObject);

            Sampe2Model m = (Sampe2Model) newObject;
            m.getText();

        }else{

        }
    }

    public <T> T getParam(String key){
        return (T)key;
    }

    private void print(Object s){
        final String tag = "SIBALSO";
        System.out.println(tag + " ::: " + s);
    }

    public class Sampe2Model extends SampleModel{

    }
}