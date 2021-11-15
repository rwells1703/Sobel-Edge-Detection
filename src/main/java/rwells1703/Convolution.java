package rwells1703;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class Convolution implements SinglebandImageProcessor<Float, FImage> {
    private final float[][] kernel;
    private final int templateWidth;
    private final int templateHeight;

    public Convolution(float[][] kernel) {
        //note that like the image pixels kernel is indexed by [row][column]
        this.kernel = kernel;
        this.templateWidth = kernel[0].length;
        this.templateHeight = kernel.length;
    }

    @Override
    public void processImage(FImage image) {
        // convolve image with kernel and store result back in image

        // Calculate zero border sizes
        int horizontalBorderSize = (int) Math.ceil((double)(templateWidth - 1) / 2);
        int verticalBorderSize = (int) Math.ceil((double)(templateHeight - 1) / 2);

        // Add border to the image
        FImage borderedImage = new FImage(image.getWidth()+2*horizontalBorderSize + 1, image.getHeight()+2*verticalBorderSize + 1);
        borderedImage.overlayInplace(image, horizontalBorderSize, verticalBorderSize);

        FImage convolutedImage = new FImage(borderedImage.getWidth() - templateWidth, borderedImage.getHeight() - templateHeight);

        // Loop through all pixels in the bordered image
        for (int x = 0; x <= borderedImage.getWidth() - templateWidth; x++) {
            for (int y = 0; y <= borderedImage.getHeight() - templateHeight; y++) {
                float sum = 0;

                // Loop through all pixels in the kernel/template
                for (int kernelX = 0; kernelX < templateWidth; kernelX++) {
                    for (int kernelY = 0; kernelY < templateHeight; kernelY++) {
                        // Sum the products of the kernel and corresponding image pixels
                        sum += borderedImage.getPixel(x + kernelX, y + kernelY) * kernel[kernelX][kernelY];
                    }
                }

                // Add the new pixel to the convoluted image
                convolutedImage.setPixel(x, y, sum);
            }
        }

        // Output the convoluted image
        image.internalAssign(convolutedImage);
    }
}