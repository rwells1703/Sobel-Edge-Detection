package rwells1703;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        videoSobel();
    }

    private static void videoSobel() throws VideoCaptureException {
        // Access First Webcam
        VideoCapture cap = new VideoCapture(1280, 720);

        VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(cap);

        // Process Video
        display.addVideoListener(
                        new VideoDisplayListener<>() {
                            @Override
                            public void afterUpdate(VideoDisplay videoDisplay) {
                            }

                            @Override
                            public void beforeUpdate(MBFImage frame) {
                                FImage greyscaleFrame = Transforms.calculateIntensity(frame);
                                // Good settings for 5x5 sobel
                                FImage greyscaleSobelFrame = greyscaleFrame.process(new Sobel()).clipMin(0.03f).multiplyInplace(4f).flipX();//.threshold(0.05f);

                                /// Good settings for 3x3 sobel
                                //FImage greyscaleSobelFrame = greyscaleFrame.process(new Sobel()).clipMin(0.01f).multiplyInplace(20f).flipX();//.threshold(0.05f);

                                frame.internalAssign(MBFImage.createRGB(greyscaleSobelFrame));
                            }
                        }
                );
    }

    private static void imageSobel() throws IOException {
        MBFImage image = ImageUtilities.readMBF(new File("images\\fish.bmp"));
        FImage greyscaleImage = Transforms.calculateIntensity(image);

        DisplayUtilities.display(greyscaleImage.process(new Sobel()).multiplyInplace(4f));
    }
}
