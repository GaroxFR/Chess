package chess;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ChessAudioPlayer {

    private final String moveSoundPath = "./res/audio/move.wav";
    private final String captureSoundPath = "./res/audio/capture.wav";


    public void playMoveSound() {
        new Thread(() -> this.playSound(this.moveSoundPath)).start();
    }

    public void playCaptureSound() {
        new Thread(() -> this.playSound(this.captureSoundPath)).start();
    }

    private void playSound(String path) {
        try {
            AudioInputStream moveStream = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(moveStream);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.CLOSE) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
