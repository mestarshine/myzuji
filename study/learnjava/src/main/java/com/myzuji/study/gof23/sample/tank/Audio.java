package com.myzuji.study.gof23.sample.tank;

import javax.sound.sampled.*;
import java.io.IOException;

public class Audio {
    byte[] b = new byte[1024 * 1024 * 15];
    private AudioFormat audioFormat = null;
    private SourceDataLine sourceDataLine = null;
    private DataLine.Info dataLine_info = null;
    private AudioInputStream audioInputStream = null;

    public Audio(String fileName) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(Audio.class.getClassLoader().getResource(fileName));
            audioFormat = audioInputStream.getFormat();
            dataLine_info = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine_info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        try {

            while (true) {
                int len = 0;
                sourceDataLine.open(audioFormat, 1024 * 1024 * 15);
                sourceDataLine.start();
                audioInputStream.mark(12358946);
                while ((len = audioInputStream.read(b)) > 0) {
                    sourceDataLine.write(b, 0, len);
                }
                audioInputStream.reset();

                sourceDataLine.drain();
                sourceDataLine.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            byte[] b = new byte[1024 * 5];
            int len = 0;
            sourceDataLine.open(audioFormat, 1024 * 5);
            sourceDataLine.start();
            audioInputStream.mark(12358946);
            while ((len = audioInputStream.read(b)) > 0) {
                sourceDataLine.write(b, 0, len);
            }
            sourceDataLine.drain();
            sourceDataLine.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
