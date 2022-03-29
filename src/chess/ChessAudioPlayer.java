package chess;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChessAudioPlayer {

    private final String moveSoundPath = "./res/audio/move.wav";
    private final String captureSoundPath = "./res/audio/capture.wav";
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private boolean enabled = true;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void playMoveSound() {
        if (this.enabled) {
            this.executorService.execute(() -> this.playSound(this.moveSoundPath));
        }
    }

    public void playCaptureSound() {
        if (this.enabled) {
            this.executorService.execute(() -> this.playSound(this.captureSoundPath));
        }
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
