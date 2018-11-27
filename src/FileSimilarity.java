import java.io.File;

public class FileSimilarity {

    private File studentFile;
    private double similarityScore;

    public FileSimilarity(File studentFile, double similarityScore) {
        this.studentFile = studentFile;
        this.similarityScore = similarityScore;
    }

    public File getStudentFile() {
        return studentFile;
    }

    public void setStudentFile(File studentFile) {
        this.studentFile = studentFile;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    public String toString() {
        return studentFile.getName() + " " + similarityScore + "% similar";
    }
}
