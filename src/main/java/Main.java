import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class Main {
    public static void main(String[] args) {
        ASCIIArt asciiArt = new ASCIIArt("/images/steve_jobs.jpg");
        asciiArt.print();
    }
}