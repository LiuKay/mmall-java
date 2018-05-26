package fun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kay on 2018/3/21.
 */
public class PrintTest {

    /**
     * kay
     * 传什么打印什么
     * @param s
     * @param n
     * @return
     */
    private static String print(String s,int n){
        if (n < 1) {
            System.out.println("打印次数不能小于1");
            return null;
        }
        if (n == 1) {
            System.out.println(s);
            return s;
        }
        String t = print(s, n - 1) + s;
        System.out.println(t);
        return t;
    }

    public static void main(String[] args) {
       // print("&",0);
        List list = new ArrayList();

    }
}
