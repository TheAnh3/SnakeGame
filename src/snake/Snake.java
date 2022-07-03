/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import cz.gyarab.util.Utils;
import cz.gyarab.util.event.EventListener;
import cz.gyarab.util.event.KeyEvent;
import cz.gyarab.util.light.LightColor;
import cz.gyarab.util.light.Matrix;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Snake {

    private final Matrix matrix = Matrix.createMatrix(15, 15);
    private int sloupec;
    private int radek;
    private int sloupecAnimace;
    private int radekAnimace;
    private long prodleva = 250;
    private int x;
    private int y;
    private int opacne = 0;
    private boolean smrt = true;
    private int o;
    private int z;
    private int skore;

    ArrayList<Point> ocas = new ArrayList<Point>();

    public static void main(String[] args) throws InterruptedException {
        Snake ma = new Snake();
        ma.hra();
    }

    public Snake() {
        matrix.getInteractiveLightPanel().addKeyListener(new EventListener<KeyEvent>() {
            @Override
            public void event(KeyEvent event) {

                if (event.isLeft() && opacne != 1 ) {
                    smer(-1, 0);
                    opacne = 2;

                }
                if (event.isRight() && opacne != 2) {
                    smer(1, 0);
                    opacne = 1;

                }
                if (event.isDown() && opacne != 3) {
                    smer(0, -1);
                    opacne = 4;

                }
                if (event.isUp() && opacne != 4) {
                    smer(0, 1);
                    opacne = 3;
                }

            }
        });
        for (int sloupec = 0; sloupec < matrix.getWidth(); sloupec++) {
            for (int radek = 0; radek < matrix.getHeight(); radek++) {
                if ((sloupec + radek) % 2 == 0) {
                    matrix.setBackground(sloupec, radek, LightColor.DARK_GRAY);
                } else {
                    matrix.setBackground(sloupec, radek, LightColor.GRAY);
                }
            }
        }

        matrix.showWindow();

        rozsvit_hlavu(0, 0);
        jablko();
        jed();
        sloupecAnimace = 1;
        radekAnimace = 0;
    }

    public void jablko() {
        Random rnd = new Random();
        x = rnd.nextInt(matrix.getWidth() - 1);
        y = rnd.nextInt(matrix.getHeight() - 1);

    }
     public static void prehraj(String filepath){
       InputStream zvuk;
    try{
        zvuk = new FileInputStream(new File(filepath));
        AudioStream audio = new AudioStream(zvuk);
        AudioPlayer.player.start(audio);
    }   
    catch(Exception e){
        JOptionPane.showMessageDialog(null, "Error");
    }
   }

    public void jed() {
        Random rnd = new Random();
        o = rnd.nextInt(matrix.getWidth() - 1);
        z = rnd.nextInt(matrix.getHeight() - 1);
    }

    public void zjevseJablko() {
        matrix.setColor(LightColor.RED);
        matrix.setOn(x, y);
    }

    public void zjevseJed() {
        matrix.setColor(LightColor.MAGENTA);
        matrix.setOn(o, z);
    }

    public void rozsvit_hlavu(int sloupec, int radek) {

        matrix.setColor(LightColor.RGB(0.5804, 0.8588, 0.5686));
        matrix.setOn(sloupec, radek);
    }

    public void rozsvit(int sloupec, int radek) {
        matrix.setColor(LightColor.GREEN);
        matrix.setOn(sloupec, radek);
    }

    public void smer(int sloupec, int radek) {
        sloupecAnimace = sloupec;
        radekAnimace = radek;
    }
    public void hra() {

        while (smrt) {

            for (int i = 0; i < matrix.getWidth(); i++) {
                for (int j = 0; j < matrix.getHeight(); j++) {
                    matrix.setOff(i, j);

                }
            }

            zjevseJablko();
            zjevseJed();
            sloupec += sloupecAnimace;
            radek += radekAnimace;

            if (sloupec < 0 || sloupec >= matrix.getWidth() || radek < 0 || radek > matrix.getHeight() - 1) {
                System.out.println("GAME OVER (" + prodleva + ")");
                 prehraj("zvuk\\\\mario_smrt.wav");
                break;
            }
            for (int i = 0; i < ocas.size() - 1; i++) {
                ocas.get(i).x = ocas.get(i + 1).x;
                ocas.get(i).y = ocas.get(i + 1).y;
            }
            if (ocas.size() >= 1) {
                ocas.get(ocas.size() - 1).x = sloupec - sloupecAnimace;
                ocas.get(ocas.size() - 1).y = radek - radekAnimace;
            }
            if (matrix.getColor(sloupec, radek) == LightColor.RED) {
                skore++;
                jablko();
                ocas.add(new Point(sloupec, radek));
                System.out.println("Score:" + skore);
                jed();
                  prehraj("zvuk\\\\kousnuti.wav");
            }

            if (matrix.getColor(sloupec, radek) == LightColor.MAGENTA) {
                jed();
                prodleva -= 20;
                prehraj("zvuk\\\\uh.wav");
            }

            for (int i = 0; i < ocas.size(); i++) {
                rozsvit(ocas.get(i).x, ocas.get(i).y);
            }
            for (int i = 0; i < ocas.size() - 1; i++) {
                if ((ocas.get(i).x - sloupec) == 0 && (ocas.get(i).y - radek) == 0) {

                    System.out.println("GAME OVER (" + prodleva + ")");
                    smrt = false;
                     prehraj("zvuk\\\\mario_smrt.wav");
                }
            }
            for (int i = 0; i < ocas.size(); i++) {
                if (matrix.getColor(ocas.get(i).x, ocas.get(i).y) == LightColor.RED) {
                    jablko();
                    zjevseJablko();

                }
            }
            for (int i = 0; i < ocas.size(); i++) {
                if (ocas.get(i).x == x && ocas.get(i).y == y) {
                    jablko();

                }
            }
            for (int i = 0; i < ocas.size(); i++) {
                if (ocas.get(i).x == o && ocas.get(i).y == z) {
                    jed();

                }
            }
            for (int i = 0; i < ocas.size(); i++) {
                if (x == o && y == z) {
                    jablko();

                }
            }

            if (x == o && y == z) {
                jablko();

            }

            if (o == x && z == y) {
                jed();

            }
            if(prodleva == 10){
                prodleva = 0;
            }

            rozsvit_hlavu(sloupec, radek);
            Utils.sleep(prodleva);

        }
    }
}
