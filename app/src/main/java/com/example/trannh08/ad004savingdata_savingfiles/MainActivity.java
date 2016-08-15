package com.example.trannh08.ad004savingdata_savingfiles;


import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    private final String subFolder = "testFiles";
    private final String fileName = "myFile.txt";

    TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myTextView = (TextView) findViewById(R.id.textView_output);
        //checkExternalMedia();
        //writeToSDFile();
        //readRaw();

        myTextView = (TextView) findViewById(R.id.textView_output);
    }

    public void writeFile_internalStorage(View view) {
        try {
            File myFile = new File(getFilesDir() + "/" + subFolder);
            myFile.mkdirs();
            File myOutputFile = new File(myFile, fileName);
            FileOutputStream myFileOutputStream = new FileOutputStream(myOutputFile);

            String outputString = ((EditText) findViewById(R.id.inputString)).getText().toString();
            outputString = "This is a sample of writing file to internal storage\n" + outputString;
            myFileOutputStream.write(outputString.getBytes());
            myFileOutputStream.flush();
            myFileOutputStream.close();

            Log.d("Success", "saveFile_internalStorage");
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Error", ex.getStackTrace().toString());
        }
    }

    public void readFile_internalStorage(View view) {
        try {
            FileInputStream myFileInputStream = new FileInputStream(getFilesDir() + "/" + subFolder + "/" + fileName);
            StringBuilder myStringBuilder = new StringBuilder();
            int totalChars;
            while ((totalChars = myFileInputStream.read()) != -1) {
                myStringBuilder.append((char) totalChars);
            }
            myTextView.setText(myStringBuilder);

            Log.d("Success", "readFile_internalStorage");
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

        if(mExternalStorageAvailable == mExternalStorageWritable == true)
            return true;
        else return false;
    }

    public void writeFile_externalStorage(View view) {
        if(!checkExternalMedia()) return;

        // Find the root of the external storage.
        File root = android.os.Environment.getExternalStorageDirectory();
        myTextView.append("\nExternal file system root: " + root);

        //File dir = new File(root.getAbsolutePath() + "/" + subFolder);
        File dir = new File(root.getAbsolutePath());
        dir.mkdirs();
        File file = new File(dir, fileName);
        String outputString = ((EditText) findViewById(R.id.inputString)).getText().toString();

        try {
            FileOutputStream myFileOutputStream = new FileOutputStream(file);
            PrintWriter myPrintWriter = new PrintWriter(myFileOutputStream);
            myPrintWriter.println("This is a sample of writing file to external storage");
            myPrintWriter.println(outputString);
            myPrintWriter.flush();
            myPrintWriter.close();
            myFileOutputStream.close();

            Log.d("Success", "writeFile_externalStorage");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Error", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", e.getStackTrace().toString());
        }
    }

    /**
     * Method to read in a text file placed in the res/raw directory of the application. The
     * method reads in all lines of the file sequentially.
     */

//    private void readRaw() {
//        myTextView.append("\nData read from res/raw/textfile.txt:");
//        InputStream is = this.getResources().openRawResource(R.raw.textfile);
//        InputStreamReader isr = new InputStreamReader(is);
//        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size
//
//        // More efficient (less readable) implementation of above is the composite expression
//    /*BufferedReader br = new BufferedReader(new InputStreamReader(
//            this.getResources().openRawResource(R.raw.textfile)), 8192);*/
//
//        try {
//            String test;
//            while (true) {
//                test = br.readLine();
//                // readLine() returns null if no more lines in the file
//                if (test == null) break;
//                myTextView.append("\n" + "    " + test);
//            }
//            isr.close();
//            is.close();
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        myTextView.append("\n\nThat is all");
//    }
}
