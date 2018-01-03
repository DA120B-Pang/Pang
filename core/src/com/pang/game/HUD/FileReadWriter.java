package com.pang.game.HUD;


import java.io.*;
import java.util.ArrayList;

public class FileReadWriter {
    //****************Läs in sparade highscore********************
    public ArrayList<HighScoreData> readFile(){
        String fileName = "highscore";
        ArrayList<HighScoreData> read = new ArrayList<>();

        boolean createNewFile = false; // Behöver ny fil skapas?

        try {
            FileInputStream fis = new FileInputStream(fileName);//Vilken fil?
            ObjectInputStream ois = new ObjectInputStream(fis);//Läs in fil
            ArrayList<HighScoreData> kontLista = (ArrayList<HighScoreData>) ois.readObject();//Skriv fil
            ois.close();//Stäng Inputstream
            return kontLista;
        }catch (FileNotFoundException fnf){
            System.out.println(fnf);
            createNewFile = true;//Fil hittas inte testa göra ny
        }catch (IOException ioe){
            System.out.println(ioe);
            createNewFile = true;//Fil hittas inte testa göra ny
        }catch (ClassNotFoundException cnf){
            System.out.println(cnf);
        }
        if(createNewFile) {//Skapa ny fil vid fel
            try {
                PrintWriter writer = new PrintWriter(fileName,"UTF-8");//skapa tom fil
                writer.close();
            } catch (UnsupportedEncodingException ee) {
                System.out.println(ee);
            } catch (FileNotFoundException fnf2) {
                System.out.println(fnf2);
            }
        }
        return read;
    }
    //*****************Spara highscore till fil*******************
    public void writeFile( ArrayList<HighScoreData> write){
        String fileName = "highscore";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(write);
            oos.close();
        }catch (FileNotFoundException fnf){
            System.out.println(fnf);
        }catch (IOException ioe){
            System.out.println(ioe);
        }
    }

}
