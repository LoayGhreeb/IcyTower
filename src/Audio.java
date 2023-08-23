import javax.sound.sampled.*;
import java.net.URL;

public class Audio {
    Clip clip;
    URL url;
    AudioInputStream audioInputStream;

    public Audio(String path) {
        url = getClass().getResource(path);
    }

    public void start()  {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();

            clip.open(audioInputStream);
            clip.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
