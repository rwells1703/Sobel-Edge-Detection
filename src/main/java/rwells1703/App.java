package rwells1703;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.Transforms;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        MBFImage image = ImageUtilities.readMBF(new File("images\\plane.bmp"));

        float[][] sobelKernelHorizontal = new float[][]{
                {-1,0,1},
                {-2,0,2},
                {-1,0,1}
        };
        averageKernel(sobelKernelHorizontal);

        float[][] sobelKernelVertical = new float[][]{
                {-1,-2,-1},
                {0,0,0},
                {1,2,1}
        };
        averageKernel(sobelKernelVertical);

        Convolution convHorizontal = new Convolution(sobelKernelHorizontal);
        Convolution convVertical = new Convolution(sobelKernelVertical);

        MBFImage edgesHorizontal = image.clone().process(convHorizontal);
        MBFImage edgesVertical = image.clone().process(convVertical);
        
        //Display the image
        DisplayUtilities.display(image);
        DisplayUtilities.display(Transforms.calculateIntensity(edgesVertical.addInplace(edgesHorizontal)).multiplyInplace(5f));
    }

    private static void averageKernel(float[][] kernel) {
        int kernelSize = kernel.length * kernel[0].length;

        for (int y = 0; y < kernel.length; y++) {
            for (int x = 0; x < kernel[0].length; x++) {
                kernel[y][x] /= kernelSize;
            }
        }
    }
}
