

public class ETtest{
    public static void main(String[] args){
        ExpressionTree et = new ExpressionTree("34 2 - 5 *");
        System.out.println("Prefix: " + et.prefix());
        System.out.println("Infix: " + et.infix());
        System.out.println("Postfix: " + et.postfix());
        System.out.println("Eval: " + et.eval());
    }
}