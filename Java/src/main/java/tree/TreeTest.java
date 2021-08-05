package tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Cy
 * @date 2021/6/27 14:21
 */
public class TreeTest {

    public static void main(String[] args) {
        TreeTest treeTest = new TreeTest();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(1);
        int rob = treeTest.rob(root);
        System.out.println(rob);
    }
    int count = 0;
    int count2 = 0;
    public int rob(TreeNode root) {
        // dp[i] = Math.max(dp[i-1],dp[i-2]+num[i]) 层数最多金额
        if(root == null) return 0;
        dfs(root);
        return Math.max(count,count2);
    }
    public void dfs(TreeNode root){
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int index = 0;
        while(!queue.isEmpty()){
            index++;
            int size = queue.size();
            for(int i = 0;i < size;i++){
                TreeNode node = queue.remove();
                if((index & 1) == 1) {
                    count += node.val;
                }
                else count2 += node.val;
                if(node.left != null) queue.add(node.left);
                if(node.right != null) queue.add(node.right);
            }
        }
    }
}
class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
}
