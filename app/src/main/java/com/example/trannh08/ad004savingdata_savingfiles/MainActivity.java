package com.example.trannh08.ad004savingdata_savingfiles;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    private final String subFolder = "testFiles";
    private final String fileName = "myFile.txt";

    EditText myEditText;
    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = (EditText) findViewById(R.id.inputString);
        myTextView = (TextView) findViewById(R.id.textView_output);
    }

    public void writeFile_internalStorage(View view) {
        try {
            File dir = new File(getFilesDir() + "/" + subFolder);
            dir.mkdirs();
            File file = new File(dir, fileName);
            FileOutputStream myFileOutputStream = new FileOutputStream(file);

            String outputString = myEditText.getText().toString();
            outputString = "This is a sample of writing file to internal storage\n" + outputString;
            myFileOutputStream.write(outputString.getBytes());
            myFileOutputStream.close();

            Toast.makeText(getApplicationContext(), "Message saved to Phone Storage via " + file.getAbsoluteFile(), Toast.LENGTH_LONG).show();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Error", ex.getStackTrace().toString());
        }
    }

    public void readFile_internalStorage(View view) {
        try {
            File dir = new File(getFilesDir() + "/" + subFolder);
            File file = new File(dir, fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            StringBuilder stringBuilder = new StringBuilder();
            int totalChars;
            while ((totalChars = fileInputStream.read()) != -1) {
                stringBuilder.append((char) totalChars);
            }
            myTextView.setText(stringBuilder);
            Toast.makeText(getApplicationContext(), "Loaded message from Phone Storage via " + file.getAbsoluteFile(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Error", ex.getStackTrace().toString());
        }
    }

    private boolean checkExternalMedia() {
        boolean mExternalStorageAvailable;
        boolean mExternalStorageWritable;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWritable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWritable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWritable = false;
        }

        myTextView.setText("External Media: readable=" + mExternalStorageAvailable
                + " writable=" + mExternalStorageWritable);

        if (mExternalStorageAvailable == mExternalStorageWritable == true)
            return true;
        else return false;
    }

    public void writeFile_externalStorage(View view) {
        if (checkExternalMedia()) {
            try {
                File root = Environment.getExternalStorageDirectory();
                File dir = new File(root.getAbsolutePath() + "/" + subFolder);
                if (!dir.exists())
                    dir.mkdirs();
                File file = new File(dir, fileName);
                EditText editText = (EditText) findViewById(R.id.inputString);
                String message = editText.getText().toString();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                /*
                * This block using pure FileOutputStream
                * */
                //fileOutputStream.write(message.getBytes());
                //fileOutputStream.close();

                /*
                * This block using pure PrintWriter
                * */
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                printWriter.println("This is a sample of writing file to external storage");
                printWriter.println(message);
                printWriter.close();
                fileOutputStream.close();

                Toast.makeText(getApplicationContext(), "Message saved to SD Card via " + file.getAbsoluteFile(), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.d("Error", ex.getStackTrace().toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "SD Card not found", Toast.LENGTH_LONG).show();
        }
    }

    public void readFile_externalStorage(View view) {
        try {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/" + subFolder);
            File file = new File(dir, fileName);
            FileInputStream fileInputStream = new FileInputStream(file);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message + "\n");
            }
            bufferedReader.close();
            myTextView.setText(message.toString());

//            StringBuilder myStringBuilder = new StringBuilder();
//            int totalChars;
//            while ((totalChars = fileInputStream.read()) != -1) {
//                myStringBuilder.append((char) totalChars);
//            }
//            myTextView.setText(myStringBuilder);
            Toast.makeText(getApplicationContext(), "Loaded message from SD Card via " + file.getAbsoluteFile(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Error", ex.getStackTrace().toString());
        }
    }


    //Internal Storage
    public void writeMessage_toInternalStorage(View view) {
        EditText editText = (EditText) findViewById(R.id.inputString);
        String message = editText.getText().toString();
        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Message saved", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Internal Storage
    public void readMessage_fromInternalStorage(View view) {
        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String message;
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message + "\n");
            }
            TextView textView = (TextView) findViewById(R.id.textView_output);
            textView.setText(stringBuffer.toString());
            textView.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
