/* 
 * ************************************
 * Expression Tree class
 * Name: David Gabriel Millares Bellido
 * UNI: DGM2148
 * Date: 21-Jul-2021
 * ************************************
 */

import java.util.Stack;
import java.io.*;

public class ExpressionTree implements ExpressionTreeInterface {

    // Instance Variables
    private ExpressionNode root;
    // Constructor
    public ExpressionTree(String expression){
        try{
            treeBuilder(expression);
        }
        catch(RuntimeException e){
            System.out.println("Invalid input ");
        }
    } // end of Constructor
    
    // Interface Methods
    
    public int eval(){
        if(root == null){
            return 0;
        }
        return eval(root);
    } // end of Eval method
    
	public String postfix(){
        if(root == null){
            return null;
        }
        String pf = postfix(root);
        return pf.substring(0, pf.length()-1);
        
    } // end of postfix method
    
	public String prefix(){
        if(root==null){
            return null;
        }
        String pf = prefix(root);
        return pf.substring(0, pf.length()-1);
        
    } // end of prefix method
    
	public String infix(){
        if(root==null){
            return null;
        }
        String pf = infix(root);
        return pf.substring(0, pf.length()-1) + " )";
        
    } // end of infix method
    
    // My helper methods
    
    private int eval(ExpressionNode node){
        if(node.left == null && node.right == null){
            return Integer.parseInt(node.element);
        }
        else{
            return eval(node.element, eval(node.left), eval(node.right));
        }
    } // end of recursive eval OVERLOADED method
    
    private int eval(String operator, int x, int y){
        if(operator.equals("+")){
            return x+y;
        }
        else if(operator.equals("*")){
            return x*y;
        }
        else if(operator.equals("-")){
            return x-y;
        }
        return x/y;
    } // end of third eval OVERLOADED Method
    
    private void treeBuilder(String exp){
        Stack<ExpressionNode> organizer = new Stack<ExpressionNode>();
        for(int i=0; i<exp.length(); i++){
            int k=i+1;
            while(k!=exp.length()&&!exp.substring(k, k+1).equals(" ")){
                k++;
            }
            String test = exp.substring(i, k);
            if(isOperator(test)){
                ExpressionNode x = (ExpressionNode) organizer.pop(),
                    y = (ExpressionNode) organizer.pop();
                organizer.push(treeBuilder(test, x, y));
            }
            else{
                ExpressionNode n = new ExpressionNode(Integer.parseInt(test));
                organizer.push(n);
            }
            i=k;
        }
        rootBuilder(organizer);
    } // end of treeBuilder method
    
    private ExpressionNode treeBuilder(String t, ExpressionNode r, ExpressionNode l){
        
        ExpressionNode operator = new ExpressionNode(t, l, r);
        return operator;
        
    } // end of treeBuilder helper method
    
    private boolean isOperator(String test){
        if(test.equals("+")||test.equals("-")||test.equals("*")||test.equals("/")){
            return true;
        }
        return false;
    } // end of isOperator method
    
    private void rootBuilder(Stack organizer){
        if(organizer.size() > 1 || organizer.empty()){
            throw new RuntimeException();
        }
        else{
            root = (ExpressionNode) organizer.pop();
        }
    } // end of rootBuilder method
    
    private String postfix(ExpressionNode node){
        if(node == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(postfix(node.left));
        sb.append(postfix(node.right));
        sb.append(node.element + " ");
        return sb.substring(0);
    } // end of postfix helper method
    
    private String prefix(ExpressionNode node){
        if(node == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(node.element + " ");
        sb.append(prefix(node.left));
        sb.append(prefix(node.right));
        return sb.substring(0);
    } // end of prefix helper method
    
    private String infix(ExpressionNode node){
        if(node == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(node.right != null && node.left != null){
            sb.append("( ");
        }
        sb.append(infix(node.left));
        sb.append(node.element + " ");
        sb.append(infix(node.right));
        if(node.right != null && node.left != null){
            sb.append(") ");
        }
        return sb.substring(0); 
    } // end of infix helper method
    
    // Nested Class
    
    private static class ExpressionNode{
        /* In an Expression tree, leafs are integers
         * if a operator is a leaf, then there was an input error */
        public ExpressionNode(Integer leaf){
            element = leaf.toString();
            left = null;
            right = null;
        } // end of leaf constructor
        
        public ExpressionNode(String op, ExpressionNode l, ExpressionNode r){
            left = l;
            right = r;
            element = op;
        } // end of interior node constructor
        
        public String element;
        public ExpressionNode left;
        public ExpressionNode right;
    } // end of ExpressionNode class
    
} // end of ExpressionTree class