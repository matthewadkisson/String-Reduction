
/**
 *
 * @author Matthew
 */
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReduction {
    private static String t ="";
    
    public static StringBuffer removeSpaces(StringBuffer s) {
        String temp = s.toString();//converts to string to remove space
        temp = temp.replaceAll("\\s+", "");//because I couldnt find an equivilent
        s = new StringBuffer(temp);//StringBuffer operation
        return s;
    }

    public static StringBuffer removeParenthesis(StringBuffer s, int pos1, int pos2) {
        s.deleteCharAt(pos1);
        s.deleteCharAt(pos2 - 2);
        return s;
    }

    public static StringBuffer RemovePar(StringBuffer exp) {
        String temp = exp.toString();
        Pattern pattern1 = Pattern.compile("\\(-?\\d+\\)");
        Matcher matcher1 = pattern1.matcher(temp);
        while (matcher1.find()) {
            exp = removeParenthesis(exp, matcher1.start(), matcher1.end());
            String temp2 = exp.toString();
            matcher1 = pattern1.matcher(temp2);
        }
        return exp;
    }

    public static StringBuffer EvalExpression(StringBuffer s, int pos1, int pos2, int priority) {
        String temp1;
        if (priority == 1) {//Multiplication
            int ph1;
            int split1;
            String fNum1, sNum1;
            temp1 = s.substring(pos1, pos2);   //This and the accompanying conditional statment below is to account for
            split1 = temp1.indexOf('*');        //an issue that would arise in a problem such as -5-(-6*-4)*(-2)
            fNum1 = temp1.substring(0, split1); //In such a case that it simplifies such as -5-24*-2 the statement
            sNum1 = temp1.substring(split1 + 1, temp1.length());// in the else block would read -24 instead of 24 like it should
            int first1 = Integer.parseInt(fNum1);//be just 24
            int second1 = Integer.parseInt(sNum1);
            ph1 = first1 * second1;
            if (pos1 - 1 > -1 && Character.isDigit(s.charAt(pos1 - 1)) && ph1 >= 0) {
                temp1 = "+" + Integer.toString(ph1);
            } else {
                temp1 = Integer.toString(ph1);
            }
            s = s.replace(pos1, pos2, temp1);
        } else if (priority == 2) {//Division
            int ph2;
            int split2;//index where the expression is split
            String fNum2, sNum2;//These are temporary placeholder to hold the numbers
            temp1 = s.substring(pos1, pos2);
            split2 = temp1.indexOf('/');
            fNum2 = temp1.substring(0, split2);
            sNum2 = temp1.substring(split2 + 1, temp1.length());
            int first2 = Integer.parseInt(fNum2);
            int second2 = Integer.parseInt(sNum2);
            ph2 = first2 / second2;
            if (pos1 - 1 > -1 && Character.isDigit(s.charAt(pos1 - 1)) && ph2 >= 0) {
                temp1 = "+" + Integer.toString(ph2);
            } else {
                temp1 = Integer.toString(ph2);
            }
            s = s.replace(pos1, pos2, temp1);
        } else if (priority == 3) {//Addition
            int ph3;
            int split3;
            String fNum3, sNum3;
            temp1 = s.substring(pos1, pos2);
            split3 = temp1.indexOf('+');
            fNum3 = temp1.substring(0, split3);
            sNum3 = temp1.substring(split3 + 1, temp1.length());
            int first3 = Integer.parseInt(fNum3);
            int second3 = Integer.parseInt(sNum3);
            ph3 = first3 + second3;
            if (pos1 - 1 > -1 && Character.isDigit(s.charAt(pos1 - 1)) && ph3 >= 0) {
                temp1 = "+" + Integer.toString(ph3);
            } else {
                temp1 = Integer.toString(ph3);
            }
            s = s.replace(pos1, pos2, temp1);
        } else if (priority == 4) {
            temp1 = s.substring(pos1, pos2);
            Pattern pattern = Pattern.compile("\\-?\\d\\-");
            Matcher matcher = pattern.matcher(temp1);
            int split4;
            if (matcher.find()) {
                split4 = matcher.end() - 1;
            } else {
                split4 = temp1.indexOf('-');
            }
            String fNum4 = temp1.substring(0, split4);
            String sNum4 = temp1.substring(split4 + 1, temp1.length());
            int first4 = Integer.parseInt(fNum4);
            int second4 = Integer.parseInt(sNum4);
            temp1 = Integer.toString(first4 - second4);
            s = s.replace(pos1, pos2, temp1);
        }
        return s;
    }

    public static StringBuffer Reduce(StringBuffer exp, int print) {
        Pattern containsMul = Pattern.compile("\\-?\\d+\\*-?\\d+");
        Pattern containsDiv = Pattern.compile("\\-?\\d+\\/-?\\d+");
        Pattern containsAdd = Pattern.compile("\\-?\\d+\\+-?\\d+");
        Pattern containsSub = Pattern.compile("\\-?\\d+\\--?\\d+");
        Matcher Mul = containsMul.matcher(exp);
        Matcher Div = containsDiv.matcher(exp);
        while (Mul.find() == true || Div.find() == true) {
            Mul = containsMul.matcher(exp); //Something funny is happening and I have to repeat these in order for 
            Div = containsDiv.matcher(exp); //it to work. Otherwise they return false when they should be true
            while (Mul.find() == true && Div.find() == true) {
                if (Mul.start() < Div.start()) {
                    exp = EvalExpression(exp, Mul.start(), Mul.end(), 1);
                } else {
                    exp = EvalExpression(exp, Div.start(), Div.end(), 2);
                }
                Mul = containsMul.matcher(exp);
                Div = containsDiv.matcher(exp);
            }
            Mul = containsMul.matcher(exp);
            Div = containsDiv.matcher(exp);
            if (Mul.find() == true) {
                exp = EvalExpression(exp, Mul.start(), Mul.end(), 1);
            } else if (Div.find() == true) {
                exp = EvalExpression(exp, Div.start(), Div.end(), 2);
            }
            Mul = containsMul.matcher(exp);
            Div = containsDiv.matcher(exp);
            if (print == 1) {
                t = t+ exp.toString() +"\n";//System.out.println("A "+t+exp.toString()+"\n");
            }
        }
        /**
         * *******************************************************
         */
        Matcher Add = containsAdd.matcher(exp);
        Matcher Sub = containsSub.matcher(exp);
        while (Add.find() == true || Sub.find() == true) {
            Add = containsAdd.matcher(exp);
            Sub = containsSub.matcher(exp);
            while (Add.find() == true && Sub.find() == true) {
                if (Add.start() < Sub.start()) {
                    exp = EvalExpression(exp, Add.start(), Add.end(), 3);
                } else {
                    exp = EvalExpression(exp, Sub.start(), Sub.end(), 4);
                }
                Add = containsAdd.matcher(exp);
                Sub = containsSub.matcher(exp);
            }
            Add = containsAdd.matcher(exp);
            Sub = containsSub.matcher(exp);
            if (Add.find() == true) {
                exp = EvalExpression(exp, Add.start(), Add.end(), 3);
            } else if (Sub.find() == true) {
                exp = EvalExpression(exp, Sub.start(), Sub.end(), 4);
            }
            Add = containsAdd.matcher(exp);
            Sub = containsSub.matcher(exp);
            if (print == 1) {
                t = t + exp.toString() +"\n";//System.out.println("A "+t+exp.toString()+"\n");
            }
        }

        return exp;
    }

    public static StringBuffer Eval(StringBuffer expression, int action) {
        StringBuffer exp = new StringBuffer(expression);
        exp = RemovePar(removeSpaces(exp));
        Pattern StillHasPar = Pattern.compile("\\([^()]*\\)");
        Matcher Par = StillHasPar.matcher(exp);
        while (Par.find() == true) {
            StringBuffer temp = new StringBuffer(exp.substring(Par.start(), Par.end()));
            String temp2 = Reduce(temp, 0).toString();
            //System.out.println(temp2);
            //t = exp.toString() +"\n";
            exp.replace(Par.start(), Par.end(), temp2);
            exp = RemovePar(removeSpaces(exp));
            Par = StillHasPar.matcher(exp);
            //System.out.println(exp);
            t = t + exp.toString() +"\n";//System.out.println("A "+t+exp.toString()+"\n");
        }
        exp = Reduce(exp,1);
        //System.out.println("T is ");
        //System.out.println(t);
        StringBuffer s = new StringBuffer(t);
        t="";
        if(action == 1){
            return s;
        }
        else{
            return exp;
        }
    }

}