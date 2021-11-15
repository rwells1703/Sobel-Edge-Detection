package rwells1703;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        MBFImage image = ImageUtilities.readMBF(new File("images\\plane.bmp"));

        float[][] sobelKernelHorizontal = new float[][]{
                {1,0,1},
                {2,0,2},
                {1,0,1}
        };

        float[][] sobelKernelVertical = new float[][]{
                {1,2,1},
                {0,0,0},
                {1,2,1}
        };

        Convolution convHoriztonal = new Convolution(sobelKernelHorizontal);
        Convolution convVerical = new Convolution(sobelKernelVertical);

        MBFImage edgesHorizontal = image.clone().process(convHoriztonal);
        MBFImage edgesVertical = image.clone().process(convVerical);
        
        //Display the image
        DisplayUtilities.display(edgesHorizontal);
    }
}
