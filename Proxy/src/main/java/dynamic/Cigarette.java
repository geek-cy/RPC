package dynamic;

/**
 * @Description
 * @Author Cy
 * @Date 2021/5/25 21:53
 */
public class Cigarette implements SellCigarette{
    @Override
    public void sell() {
        System.out.println("卖了烟");
    }
}
