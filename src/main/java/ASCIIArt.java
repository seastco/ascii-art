import org.bytedeco.opencv.opencv_core.IplImage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class ASCIIArt {

    private static final String ASCII_CHARS = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
    private static final int MAX_PIXEL_VALUE = 255;

    private final List<List<Character>> asciiArt;

    public ASCIIArt(String filePath) {
        String path = getClass().getResource(filePath).getPath();
        IplImage image = cvLoadImage(path);
        this.asciiArt = generateAsciiArt(image);
    }

    private List<List<Character>> generateAsciiArt(IplImage image) {
        List<List<Character>> asciiArt = new ArrayList<>();
        ByteBuffer buffer = image.createBuffer();

        for (int i = 0; i < image.height(); i++) {
            List<Character> row = new ArrayList<>();

            for (int j = 0; j < image.width(); j++) {
                /*
                The position being calculated is the starting position of a pixel in the buffer.
                (i * step) finds the row position, and (j * channels) finds the column position.
                Starting at the position we find the red value, next is green, and lastly blue.
                - i is the image height row
                - widthStep is the number of bytes between rows
                - j is the image width column
                - nChannels is the number of color channels (3 for RGB)
                 */
                int position = (i * image.widthStep()) + (j * image.nChannels());
                int red = buffer.get(position) & 0xFF; // 0xFF to cast unsigned byte to int
                int green = buffer.get(position + 1) & 0xFF;
                int blue = buffer.get(position + 2) & 0xFF;

                /*
                Derive a "brightness" value from each pixel by taking a weighted average of the RGB values.
                These weights account for human perception of luminance. Then map this brightness (0-255)
                to 0-64 to assign an ASCII character.
                 */
                double brightness = (0.21 * red) + (0.72 * green) + (0.07 * blue);
                int charIndex = (int) ((brightness / MAX_PIXEL_VALUE) * (ASCII_CHARS.length() - 1));
                char asciiChar = ASCII_CHARS.charAt(charIndex);
                row.add(asciiChar);
            }

            asciiArt.add(row);
        }

        return asciiArt;
    }

    public void print() {
        for (List<Character> row : this.asciiArt) {
            for (Character ascii : row) {
                System.out.print(ascii+""+ascii+""+ascii);
            }
            System.out.println();
        }
    }
}
