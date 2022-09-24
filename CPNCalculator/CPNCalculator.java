// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T2, Assignment 5
 * Name: Ryan Sturgess
 * Username: sturgeryan
 * ID: 300618020
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/** 
 * Calculator for Cambridge-Polish Notation expressions
 * (see the description in the assignment page)
 * User can type in an expression (in CPN) and the program
 * will compute and print out the value of the expression.
 * The template provides the method to read an expression and turn it into a tree.
 * You have to write the method to evaluate an expression tree.
 *  and also check and report certain kinds of invalid expressions
 */

public class CPNCalculator{

    /**
     * Setup GUI then run the calculator
     */
    public static void main(String[] args){
        CPNCalculator calc = new CPNCalculator();
        calc.setupGUI();
        calc.runCalculator();
    }

    /** Setup the gui */
    public void setupGUI(){
        UI.addButton("Clear", UI::clearText); 
        UI.addButton("Quit", UI::quit); 
        UI.setDivider(1.0);
    }

    /**
     * Run the calculator:
     * loop forever:  (a REPL - Read Eval Print Loop)
     *  - read an expression,
     *  - evaluate the expression,
     *  - print out the value
     * Invalid expressions could cause errors when reading or evaluating
     * The try-catch prevents these errors from crashing the program - 
     *  the error is caught, and a message printed, then the loop continues.
     */
    public void runCalculator(){
        UI.println("Enter expressions in pre-order format with spaces");
        UI.println("eg   ( * ( + 4 5 8 3 -10 ) 7 ( / 6 4 ) 18 )");
        while (true){
            UI.println();
            try {
                GTNode<ExpElem> expr = readExpr();
                double value = evaluate(expr);
                UI.println(" -> " + value);
            }catch(Exception e){UI.println("Something went wrong! "+e);}
        }
    }

    /**
     * Evaluate an expression and return the value
     * Returns Double.NaN if the expression is invalid in some way.
     * If the node is a number
     *  => just return the value of the number
     * or it is a named constant
     *  => return the appropriate value
     * or it is an operator node with children
     *  => evaluate all the children and then apply the operator.
     */
    public double evaluate(GTNode<ExpElem> expr){
        if (expr==null){
            return Double.NaN;
        }

        if(!Double.isNaN(expr.getItem().value)) {
            return expr.getItem().value;
        }
        else if(expr.getItem().operator.equalsIgnoreCase("PI")){
            return Math.PI;
        }
        else if(expr.getItem().operator.equalsIgnoreCase("E")){
            return Math.E;
        }
        else if(!expr.getItem().operator.equals("#") && expr.numberOfChildren() > 0) {
            return operator(expr);
        }

        return Double.NaN;
    }

    /**
     * Calculates the value with the operator
     * of the parents node
     * @param expr current node of the tree
     * @return NaN if the arguement is invaild or double if the arguement is vaild
     */
    public double operator(GTNode<ExpElem> expr) {
        List<Double> tmpList = new ArrayList<Double>();
        double total = 0;
        for (int i = 0; i < expr.numberOfChildren(); i++) {
            tmpList.add(evaluate(expr.getChild(i)));
        }
        switch (expr.getItem().operator) {
            case "*":
                for (Double double1 : tmpList) {
                    if(total != 0) {
                        total *= double1;
                    }
                    if(total == 0) {
                        total += double1;
                    }
                }
                return total;
            case "+":
                for (Double double1 : tmpList) {
                    total += double1;
                }
                return total;
            case "/":
                for (Double double1 : tmpList) {
                    if(total != 0) {
                        total /= double1;
                    }
                    if(total == 0) {
                        total += double1;
                    }
                }
                return total;
            case "-":
                for (Double double1 : tmpList) {
                    if(total != 0) {
                        total -= double1;
                    }
                    if(total == 0) {
                        total += double1;
                    }
                }
                return total;
            case "^":
                if(tmpList.size() != 2) {
                    UI.println("Only 2 values can be accepted for \"^\".");
                    return Double.NaN;
                }
                return Math.pow(tmpList.get(0), tmpList.get(1));
            case "sqrt":
                if(tmpList.size()!=1) {
                    UI.println("Only 1 value can be accepted for sqrt.");
                    return Double.NaN;
                }
                return Math.sqrt(tmpList.get(0));
            case "avg":
                if(tmpList.size() <= 0) {
                    UI.println("Not enough for an average. Min number of numbers: 1");
                    return Double.NaN;
                }
                for (Double double1 : tmpList) {
                    total += double1;
                }
                return total /= tmpList.size();
            case "log":
                if(tmpList.size() < 1 || tmpList.size() > 2) {
                    UI.println("Too many number for log. Only 1 or 2 values can get accepted.");
                    return Double.NaN;
                }
                if(tmpList.size() == 1) {
                    return Math.log10(tmpList.get(0));
                }  
                else {
                    return Math.log(tmpList.get(0)) / Math.log(tmpList.get(1));
                }            
            case "ln":
                if(tmpList.size()!=1) {
                    UI.println("Only 1 value can be accepted for natural log.");
                    return Double.NaN;
                }
                return Math.log(tmpList.get(0));
            case "dist":
                if(tmpList.size()<4 || tmpList.size()>6 || tmpList.size()==5) {
                    UI.println("Does not have 4 or 6 values");
                    return Double.NaN;
                }
                if(tmpList.size()==4){
                    double x = Math.pow((tmpList.get(2)-tmpList.get(0)), 2);
                    double y = Math.pow((tmpList.get(3)-tmpList.get(1)), 2);
                    return Math.sqrt(x+y);
                }
                if(tmpList.size()==6) {
                    double x = Math.pow((tmpList.get(3)-tmpList.get(0)), 2);
                    double y = Math.pow((tmpList.get(4)-tmpList.get(1)), 2);
                    double z = Math.pow((tmpList.get(5)-tmpList.get(2)), 2);
                    return Math.sqrt(x+y+z);
                }
            case "sin":
                if(tmpList.size() != 1) {
                    UI.println("Only 1 value can be inputted for sin.");
                    return Double.NaN;
                }
                return Math.sin(tmpList.get(0));
            case "cos":
                if(tmpList.size() != 1) {
                    UI.println("Only 1 value can be inputted for cos.");
                    return Double.NaN;
                }
                return Math.cos(tmpList.get(0));
            case "tan":
                if(tmpList.size() != 1) {
                    UI.println("Only 1 value can be inputted for tan.");
                    return Double.NaN;
                }
                return Math.tan(tmpList.get(0));
            default: 
                UI.println("The opertor inputted "+ expr.getItem().operator + " was not found");
                return Double.NaN;
        }
    }

    /**
     * Prints out the conversion of CPN to normal infix notation
     * @param expr cuurent node of the tree
     * @return 
     */
    public String printExpr(GTNode<ExpElem> expr) {

        return "";
    }

    /** 
     * Reads an expression from the user and constructs the tree.
     */ 
    public GTNode<ExpElem> readExpr(){
        String expr = UI.askString("expr:");
        return readExpr(new Scanner(expr));   // the recursive reading method
    }

    /**
     * Recursive helper method.
     * Uses the hasNext(String pattern) method for the Scanner to peek at next token
     */
    public GTNode<ExpElem> readExpr(Scanner sc){
        if (sc.hasNextDouble()) {                     // next token is a number: return a new node
            return new GTNode<ExpElem>(new ExpElem(sc.nextDouble()));
        }
        else if (sc.hasNext("\\(")) {                 // next token is an opening bracket
            sc.next();                                // read and throw away the opening '('
            if(sc.hasNext("\\)")) {
                UI.println("Empty Brackets Found");
                return null;
            }
            
            ExpElem opElem = new ExpElem(sc.next());  // read the operator
            GTNode<ExpElem> node = new GTNode<ExpElem>(opElem);  // make the node, with the operator in it.
            while (! sc.hasNext("\\)")){              // loop until the closing ')'
                GTNode<ExpElem> child = readExpr(sc); // read each operand/argument
                node.addChild(child);                 // and add as a child of the node
            }
            sc.next();                                // read and throw away the closing ')'
            return node;
        }
        else {                                        // next token must be a named constant (PI or E)
                                                      // make a token with the name as the "operator"
            return new GTNode<ExpElem>(new ExpElem(sc.next()));
        }
    }

}

