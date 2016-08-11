package com.example.trannh08.ad004savingdata_savingfiles;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MEDIA";
    private TextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = (TextView) findViewById(R.id.textView_output);
        checkExternalMedia();
        writeToSDFile();
        //readRaw();
    }

    public void saveFile_internalStorage(View view) {
        String filename = "myFile.txt";
        String outputString = ((EditText) findViewById(R.id.inputString)).getText().toString();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(outputString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveFile_internalStorage2(view);
    }

    public void saveFile_internalStorage2(View view)
    {
        String filename = "mySecondFile.txt";
        String outputString = ((EditText) findViewById(R.id.inputString)).getText().toString();
        File myDir = getFilesDir();

        try {
            File secondFile = new File(myDir + "/text/", filename);
            if (secondFile.getParentFile().mkdirs()) {
                secondFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(secondFile);

                fos.write(outputString.getBytes());
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkExternalMedia() {
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
        myTextView.append("\n\nExternal Media: readable="
                + mExternalStorageAvailable + " writable=" + mExternalStorageWritable);
    }

    private void writeToSDFile() {

        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

        File root = android.os.Environment.getExternalStorageDirectory();
        myTextView.append("\nExternal file system root: " + root);

        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

        File dir = new File(root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Hi , How are you");
            pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the  manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        myTextView.append("\n\nFile written to " + file);
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
