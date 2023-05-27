
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class CalculatorLogic {

    //Call other methods and returns a String
    public String callProcessMethods(String notation){
        try {
            List<String> expression = separateOperandAndOperators(notation);
            List<String> finalAnswerList = orderAndProcess(expression);//Final answer
            String finalAnswerString = String.join("", finalAnswerList);//List to string
            double finalAnswerDouble = Double.parseDouble(finalAnswerString);//Parse the value of String
            return formatResult(finalAnswerDouble);//Print and Format Double
        }catch (Exception e){
            return "Error";
        }
    }



    //Formatting returns double if double else integer also adds comma
    public  String formatResult(double input) {
        if(input==0){
            return "0";
        }
        DecimalFormat formatter;
        if (input == (int) input) {
            formatter = new DecimalFormat("#,###");
        } else {
            formatter = new DecimalFormat("#,###.00");
        }
        return formatter.format(input);
    }


    //Separates operations and numbers
    public  List<String> separateOperandAndOperators(String notation){
        notation = notation.replaceAll(",","");

        List<String> expression = new ArrayList<>();
        StringBuilder isDigit = new StringBuilder();

        //Separate numbers from operators
        for(int i=0;i<notation.length();i++){
            if(notation.charAt(0)=='-'&&i==0){//Checks if notation starts with a negative
                isDigit.append("-");//Assigns a negative sign to isDigit variable
            }

            else if(Character.isDigit(notation.charAt(i))||notation.charAt(i)=='.'){
                isDigit.append(notation.charAt(i));// Add a char(Digit) to isDigit variable

                if(i==(notation.length()-1)){//This adds last number in notation;
                    expression.add(isDigit.toString());
                }

            }else {
                expression.add(isDigit.toString());//Add isDigit to list
                isDigit = new StringBuilder();//Resets the value of isDigit
                if(notation.charAt(i)=='-'&&(!Character.isDigit(notation.charAt(i-1))&&notation.charAt(i-1)!=')')){
                    //If operator exist before subtract sign, add as negative sign to isDigit
                    isDigit.append("-");
                }else{
                    expression.add(String.valueOf(notation.charAt(i)));//Add operator to the list
                }
            }
        }


        String [] unimportant = {" ",""};
        expression.removeAll(List.of(unimportant));//remove unwanted characters
        //Returns a separated numbers and operators
        return expression;
    }



    //Order the operation and loops until the list size is == 1
    public  List<String> orderAndProcess(List<String> expression) {
        while(expression.size()>1){
            //In order of operation;
            boolean hasOpenParenthesis = expression.contains("(");
            boolean hasExponent = expression.contains("^");
            boolean hasMultiplication = expression.contains("*");
            boolean hasDivision = expression.contains("/");
            boolean hasAddition = expression.contains("+");
            boolean hasSubtraction = expression.contains("-");

            if (hasOpenParenthesis){
                computeParenthesis(expression);

            } else if (hasExponent) {
                computeEMDAS(expression, "^");

            } else if (hasMultiplication && hasDivision) {
                //Execute if both Product and Quotient exist
                int product = expression.indexOf("*");
                int quotient = expression.indexOf("/");
                //Finds the smallest index of operation

                if (product < quotient) {
                    computeEMDAS(expression, "*");
                } else {
                    computeEMDAS(expression, "/");
                }

            } else if (hasMultiplication) {
                computeEMDAS(expression, "*");
            } else if (hasDivision) {
                computeEMDAS(expression, "/");


            } else if (hasAddition && hasSubtraction) {
                //Order if two same value operation exist in same notation
                int sum = expression.indexOf("+");
                int diff = expression.indexOf("-");

                if (sum < diff) {
                    computeEMDAS(expression, "+");
                } else {
                    computeEMDAS(expression, "-");
                }

            } else if (hasAddition) {
                computeEMDAS(expression, "+");
            } else {
                computeEMDAS(expression, "-");
            }
        }

        return expression;
    }



    //Finds the close pair of parenthesis
    public  int findCloseParenthesis(List<String> expression, int open){
        List<String> sublist = expression.subList(open,expression.size());
        return sublist.indexOf(")") + open;
    }


    //Parenthesis Process
    public  List<String> computeParenthesis(List<String> expression){
        int open = expression.lastIndexOf("(");//Find the last occurring open parenthesis
        int close = findCloseParenthesis(expression,open);

        orderAndProcess(expression.subList(open+1,close));//Gets the equation inside Parenthesis

        //Original List = 1+(2+3)
        //updated List = 1+(5)
        //This update the indexes in list
        open = expression.lastIndexOf("("); //update the index values
        close = findCloseParenthesis(expression,open);

        boolean isLeftDigit = false;
        boolean isRightDigit = false;

        //Checks if characters before and after parenthesis is a digit or not
        if(open!=0){
            String leftCharacter = expression.get(open-1);
            isLeftDigit = leftCharacter.matches(".*\\d+.*")||leftCharacter.matches(".[)].");
            //regex: digit/close parenthesis/decimal point
        }

        if(close!=expression.size()-1){
            String rightCharacter = expression.get(close+1);
            isRightDigit = rightCharacter.matches(".*\\d+.*");
            //regex: digit/decimal point
        }


        //Replace ')' with '*' if digit==true else remove ')'
        if(isRightDigit){
            expression.set(close,"*");
        }else {
            expression.remove(close);
        }

        if(isLeftDigit){//
            expression.set(open,"*");
        }else {
            expression.remove(open);
        }

        return expression;
    }



    //computeEMDAS Process
    public  List<String> computeEMDAS(List<String> expression, String operation){
        int operationPosition = expression.indexOf(operation);//get the index of parameter operation
        double result = 0;

        if(expression.contains("^")){
            //Right to Left Operation: Use for exponent to exponent expression
            operationPosition = expression.lastIndexOf("^");
        }

        double leftDigit = Double.parseDouble(expression.get(operationPosition -1));//Parse left digit of operation
        double rightDigit = Double.parseDouble(expression.get(operationPosition +1));//Parse right digit of operation

        switch (operation){//Operate base on operation
            case "^" -> result = Math.pow(leftDigit ,rightDigit);
            case "*"-> result = leftDigit * rightDigit;
            case "/"-> result = leftDigit / rightDigit;
            case "+"-> result = leftDigit + rightDigit;
            case "-"-> result = leftDigit - rightDigit;
        }

        //Formatting
        String formatResult = String.format("%.2f", result);//Format result to 2 decimal places

        //List Manipulation
        expression.subList(operationPosition-1,operationPosition+2).clear();//clear left digit to right digit
        expression.add(operationPosition-1,formatResult);//Add result to expression

        return expression;//Return to orderAndProcess
    }
}




