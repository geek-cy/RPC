package dfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Cy
 * @date 2021/6/21 14:14
 */
public class Test1 {
    public static void main(String[] args) {
        Test1 test1 = new Test1();
        test1.findSubsequences(new int[]{4,6,7,7});
    }
    List<List<Integer>> list = new ArrayList<>();
    public List<List<Integer>> findSubsequences(int[] nums) {
        // 同一层元素或同一树枝元素不能使用
        boolean[] used = new boolean[201];
        // 取过的元素不能重复使用
        int startIndex = 0;
        List<Integer> path = new ArrayList<>();
        dfs(nums,0,path);
        return list;
    }
    public void dfs(int[] nums,int startIndex,List<Integer> path){
        if(path.size() > 1) list.add(new ArrayList<>(path));
        Set<Integer> set = new HashSet<>();
        for(int i = startIndex;i < nums.length;i++){
            if(set.contains(nums[i])|| !path.isEmpty() && path.get(path.size()-1) > nums[i]) continue;
            set.add(nums[i]);
            path.add(nums[i]);
            dfs(nums,i+1,path);
            path.remove(path.size()-1);
        }
    }
}
