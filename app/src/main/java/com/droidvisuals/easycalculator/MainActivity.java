package com.droidvisuals.easycalculator;

import androidx.appcompat.app.AppCompatActivity;

//import android.content.Context;
// as  it was repetitive !

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

// imported

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
//import android.util.Log;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView result_tv,solution_tv;
    MaterialButton  button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton button_minus,button_plus,button_multiply,button_divide;
    MaterialButton button_open_bracket,button_close_bracket,button_equal;
    MaterialButton button_c,button_all_clear,button_dot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result_tv=findViewById(R.id.result_textView);
        solution_tv=findViewById(R.id.solution_textView);

        assignId(button0,R.id.button_0);
        assignId(button2,R.id.button_1);
        assignId(button3,R.id.button_2);
        assignId(button4,R.id.button_3);
        assignId(button1,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);

        assignId(button_divide,R.id.button_divide);
        assignId(button_minus,R.id.button_minus);
        assignId(button_multiply,R.id.button_multiply);
        assignId(button_plus,R.id.button_plus);
        assignId(button_equal,R.id.button_equal);

        assignId(button_close_bracket,R.id.button_close_bracket);
        assignId(button_open_bracket,R.id.button_open_bracket);
        assignId(button_dot,R.id.button_dot);
        assignId(button_c,R.id.button_c);
        assignId(button_all_clear,R.id.button_all_clear);

    }

    void assignId(MaterialButton btn,int id){
        btn=findViewById(id);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        result_tv.setTextSize(30);

        MaterialButton btn=(MaterialButton) v;
        String buttonText=btn.getText().toString();

        String dataToCalculate=solution_tv.getText().toString(); // initial : ""

        // base check condition
        if((buttonText.equals("/") || buttonText.equals("*") || buttonText.equals("+") ) && dataToCalculate.length()==0 )
            return;



        // all clear button
        if(buttonText.equals("AC")){
            solution_tv.setText("");
            result_tv.setText("");
            return;
        }

        // equal to button
        if(buttonText.equals("=")){

            if(hasBalancedParentheses(dataToCalculate)){
                solution_tv.setText(result_tv.getText());
                solution_tv.setText("");
                result_tv.animate().scaleX(1).scaleY(1);
                result_tv.setTextSize(80);
            }
            else

                result_tv.setText("Invalid Expression");
            return;


        }

        // Backspace button
        if(buttonText.equals("C")){
            if(dataToCalculate.length() >= 1) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
                solution_tv.setText(dataToCalculate);
                return;
            }
            else{
                android.widget.Toast.makeText(this, "Now everyting is fine ", android.widget.Toast.LENGTH_SHORT).show();
                dataToCalculate="";
                return;
            }
        }


        // base case :
            if (!dataToCalculate.isEmpty()) {

                char lastchar = dataToCalculate.charAt(dataToCalculate.length()-1);

                if (
                        (buttonText.equals("/") && (lastchar == '/' || lastchar == '*' || lastchar == '+' || lastchar == '-')) ||
                                (buttonText.equals("+") && (lastchar == '+' || lastchar == '*' || lastchar == '/' || lastchar == '-')) ||
                                (buttonText.equals("*") && (lastchar == '+' || lastchar == '*' || lastchar == '/' || lastchar == '-')) ||
                                (buttonText.equals("-") && (lastchar == '+' || lastchar == '*' || lastchar == '/' || lastchar == '-'))
                ) return;

            }




            dataToCalculate = dataToCalculate + buttonText;
            solution_tv.setText(dataToCalculate);

            String finalResult;
            try{
                finalResult=getResult(dataToCalculate);
            }
            catch (Exception e){
                android.widget.Toast.makeText(this, " Error occured whie calculating ", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            if(!finalResult.equals("Error"))
                result_tv.setText(finalResult);





    }

    String getResult(String data){

        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initSafeStandardObjects();

             String finalResult=context.evaluateString(scriptable,data,"Javascript",1,null).toString();

             if(finalResult.endsWith(".0") ){
                 finalResult=finalResult.replace(".0","");
             }


             return finalResult;
        }
        catch (Exception e){
            return "Error";
        }

    }

    public static boolean hasBalancedParentheses(String expression) {
        int count = 0;

        for (char c : expression.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }

        return count == 0;
    }

}