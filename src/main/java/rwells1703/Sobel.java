package rwells1703;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class Sobel implements SinglebandImageProcessor<Float, FImage> {
    private final float[][] sobelKernelHorizontal;
    private final float[][] sobelKernelVertical;

    public Sobel() {
        // 3x3 sobel kernel
        /*this.sobelKernelHorizontal = new float[][]{
                {1,0,-1},
                {2,0,-2},
                {1,0,-1}
        };*/

        // 5x5 sobel kernel
        this.sobelKernelHorizontal = new float[][]{
                {1,2,0,-2,-1},
                {4,8,0,-8,-4},
                {6,12,0,-12,-6},
                {4,8,0,-8,-4},
                {1,2,0,-2,-1}
        };

        // Transpose the vertical kernel to get the horizontal one
        this.sobelKernelVertical = transposeArray(this.sobelKernelHorizontal);
    }

    // Take the kernel and divide each value by the kernel size to normalise it
    private static void averageKernel(float[][] kernel) {
        int kernelSize = kernel.length * kernel[0].length;

        for (int y = 0; y < kernel.length; y++) {
            for (int x = 0; x < kernel[0].length; x++) {
                kernel[y][x] /= kernelSize;
            }
        }
    }

    @Override
    public void processImage(FImage image) {
        averageKernel(sobelKernelHorizontal);
        averageKernel(sobelKernelVertical);

        // Perform convolution along the horizontal and vertical axes
        Convolution convHorizontal = new Convolution(sobelKernelHorizontal);
        Convolution convVertical = new Convolution(sobelKernelVertical);

        // Get 2 images for horizontal and vertical edges
        FImage edgesHorizontal = image.process(convHorizontal);
        FImage edgesVertical = image.process(convVertical);

        // Sum the 2 images to get all edges
        image.internalAssign(edgesVertical.addInplace(edgesHorizontal));
    }

    // 'Transpose' an array of floats (vertical becomes horizontal and vice versa)
    private float[][] transposeArray(float[][] array) {
        int p = array.length;
        int q = array[0].length;

        float[][] transposedArray = new float[q][p];

        for(int x = 0; x < q; x++) {
            for(int y = 0; y < p; y++) {
                transposedArray[x][y] = array[y][x];
            }
        }

        return transposedArray;
    }
}
