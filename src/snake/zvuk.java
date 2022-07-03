/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author thean
 */
public class zvuk {
    public static void main(String[] args) {
       prehraj("zvuk\\\\kousnuti.wav"); 
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
}
