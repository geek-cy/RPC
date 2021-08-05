import java.lang.ref.SoftReference;

/**
 * @author Cy
 * @date 2021/6/19 15:12
 */
public class SoftReferenceTest {

    private static class Bigger{
        public int[] values;
        public String name;
        public Bigger(String name){
            this.name = name;
            values = new int[1024];
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        public static void main(String[] args) throws InterruptedException {
            int count = 100000;
            SoftReference[] values = new SoftReference[count];
            for(int i = 0;i < count;i++){
                values[i] = new SoftReference<Bigger>(new Bigger("Bigger"+i));
            }
            for(int i = 0;i < 100000;i++){
                System.out.println(values[i].get());
            }
        }
    }
}
