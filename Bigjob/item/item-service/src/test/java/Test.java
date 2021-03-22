import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Test extends Thread{
   ThreadLocal threadLocal= new ThreadLocal<Thread>();

    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        System.out.println(date);
        System.out.println(format.replace("-",""));


    }

    @Override
    public void run() {
        threadLocal.set(Thread.currentThread());
        Object o = threadLocal.get();
        System.out.println(o);
    }
}
