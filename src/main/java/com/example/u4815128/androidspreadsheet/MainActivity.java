package com.example.u4815128.androidspreadsheet;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ArrayList<EditText> cells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cells = new ArrayList<>();
        cells.add((EditText) findViewById(R.id.editText1));
        cells.add((EditText) findViewById(R.id.editText2));
        cells.add((EditText) findViewById(R.id.editText3));
        cells.add((EditText) findViewById(R.id.editText4));
        cells.add((EditText) findViewById(R.id.editText5));
        cells.add((EditText) findViewById(R.id.editText6));
        cells.add((EditText) findViewById(R.id.editText7));
        cells.add((EditText) findViewById(R.id.editText8));
        cells.add((EditText) findViewById(R.id.editText9));
        cells.add((EditText) findViewById(R.id.editText10));
        cells.add((EditText) findViewById(R.id.editText11));
        cells.add((EditText) findViewById(R.id.editText12));
    }


    public void saveData(View view) {

        String filename = "spreadsheetData";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            for (EditText cell : cells) {
                String cellText = cell.getText().toString() + "\n";
                outputStream.write(cellText.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadData(View view) {

        String filename = "spreadsheetData";
        FileInputStream inputStream;

        try {
            inputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                EditText cell = cells.get(i);
                cell.setText(line, TextView.BufferType.EDITABLE);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculate(View view) {

        // Build map
        Map<String, Double> mappy = new HashMap<>();

        /*for (EditText cell : cells) {
            String cellText = cell.getText().toString() + "\n";
            if (!cellText.equals(" ")) {1
        }*/


        for (EditText cell : cells) {
            String cellText = cell.getText().toString();
            System.out.println("Cell text: " + cellText + "\n");
            System.out.println("Cell type: " + cellText.getClass() + "\n");
            System.out.println("Cell text len: " + cellText.length() + "\n");
            if (cellText.length() > 0) {
                SpreadsheetTokenizer toke = new SpreadsheetTokenizer(cellText);
                Expression exp = Expression.parseExp(toke);
                Double doubleVal = exp.evaluate(mappy);
                int intVal = doubleVal.intValue();
                String stringVal;
                if (doubleVal - Math.floor(doubleVal) == 0)
                    stringVal = "" + intVal;
                else
                    stringVal = Double.toString(doubleVal);
                System.out.println("Evaluates to: " + doubleVal);
                // System.out.println("Simplify: " + exp.simplify().evaluate(mappy) + "\n");

                cell.setText(stringVal, TextView.BufferType.EDITABLE);
            }
        }
    }
}
