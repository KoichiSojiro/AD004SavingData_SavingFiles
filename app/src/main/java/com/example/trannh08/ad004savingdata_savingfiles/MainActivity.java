package com.example.trannh08.ad004savingdata_savingfiles;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    private final String subFolder = "testFiles";
    private final String fileName = "myFile.txt";

    private final int REQUEST_EXTERNAL_STORAGE = 113;

    EditText myEditText;
    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myEditText = (EditText) findViewById(R.id.inputString);
        myTextView = (TextView) findViewById(R.id.textView_output);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_EXTERNAL_STORAGE) {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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
        } catch (FileNotFoundException e) {
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

            /**
             * This block using FileInputStream + StringBuilder
             */
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

    private boolean checkExternalStorage_Available() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWritable = false;
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

        Toast.makeText(getApplicationContext(), "External Media: readable=" + mExternalStorageAvailable
                + " writable=" + mExternalStorageWritable, Toast.LENGTH_SHORT).show();

        if (mExternalStorageAvailable == mExternalStorageWritable == true)
            return true;
        else return false;
    }
    private boolean checkExternalStorage_Permission_API23() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            //handling permission if API < 23
            return checkExternalStorage_Available();
        }
    }
    private void askExternalStorage_Permission_API23() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "External Storage permission is needed", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
        }
    }
    public void writeFile_externalStorage(View view) {
        if(checkExternalStorage_Permission_API23()) {
            //ok to use SD Card
            writeFile();
        } else {
            askExternalStorage_Permission_API23();
            if(checkExternalStorage_Permission_API23()) {
                //ok to use SD Card
                writeFile();
            } else {
                Toast.makeText(getApplicationContext(), "SD Card not found", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void writeFile() {
        try {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/" + subFolder);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, fileName);
            EditText editText = (EditText) findViewById(R.id.inputString);
            String message = editText.getText().toString();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            /**
             * This block using FileReader + BufferedReader
             */
            //fileOutputStream.write(message.getBytes());
            //fileOutputStream.close();

            /**
             * This block using FileReader + PrintWriter
             */
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println("This is a sample of writing file to external storage");
            printWriter.println(message);
            printWriter.close();
            fileOutputStream.close();

            Toast.makeText(getApplicationContext(), "Message saved to SD Card via "
                    + file.getAbsoluteFile(), Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Error", ex.getStackTrace().toString());
        }
    }

    public void readFile_externalStorage(View view) {
        try {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/" + subFolder);
            File file = new File(dir, fileName);

            /**
             * This block using FileReader + BufferedReader
             */
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String message = "";
            String lineValue;
            while ((lineValue = bufferedReader.readLine()) != null) {
                message += lineValue + "\n";
            }
            bufferedReader.close();

            myTextView.setText(message.toString());
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
}
