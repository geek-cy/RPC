package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @author Cy
 * @date 2021/7/9 13:03
 */
public class test {
    public static void main(String[] args){

        BufferedReader bfr = null;

        FileWriter out = null;

        Date d = new Date();
        d.getTime();
        try {
            //输入流
            FileReader read = new FileReader("D:\\input.txt");

            //输出流
            out = new FileWriter("D:\\output.txt");

            //缓冲流
            bfr = new BufferedReader(read);

            StringBuilder sb = new StringBuilder();
            String line = null;

            //包装流可以读取一行
            while((line = bfr.readLine()) != null){
                System.out.println(line);
                sb.append(line);
            }

            char[] chars = sb.toString().toCharArray();
            Arrays.sort(chars);
            for(char str: chars){
                out.write(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bfr != null){
                    bfr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(out != null){
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
