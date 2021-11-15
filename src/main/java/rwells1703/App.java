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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App {
    public static void main( String[] args ) throws IOException {
        imageSobel();
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
        String inputFileFolder = "images\\";
        String inputFile = "happy2.jpg";
        MBFImage image = ImageUtilities.readMBF(new File(inputFileFolder + inputFile));

        FImage greyscaleImage = Transforms.calculateIntensity(image);

        FImage sobelImage = greyscaleImage.process(new Sobel()).multiplyInplace(4f);

        // Save the image to a file
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);

        ImageUtilities.write(sobelImage, new File(inputFileFolder + "output\\" + dateString + "_" + inputFile));

        //DisplayUtilities.display(sobelImage);
    }
}
