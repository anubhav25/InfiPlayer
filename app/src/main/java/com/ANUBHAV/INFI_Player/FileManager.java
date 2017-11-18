package com.ANUBHAV.INFI_Player;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ANUBHAV on 7/18/2016.
 */
public class FileManager  {
    private Context context;

    FileManager(Context con)
    {
        this.context=con;
    }
    FileManager(){}



            public void createFile(String filename,String data) {


                FileOutputStream fos;
                try {
                    fos = context.openFileOutput(filename, context.MODE_PRIVATE);
                    fos.write(data.getBytes());
                    fos.close();


                } catch (FileNotFoundException e) {e.printStackTrace();}
                catch (IOException e) {e.printStackTrace();}

            }




            public String readFile(String filename) {
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                            context.openFileInput(filename)));
                    String inputString;

                    while ((inputString = inputReader.readLine()) != null) {
                        stringBuffer.append(inputString);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

               return (stringBuffer.toString());
            }

        }



