import java.io.File;
import java.util.Scanner;

public class RealTimeDirectoryAnalysis {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("   Real-Time Directory Analysis System    ");
        System.out.println("==========================================");

        try {
            // 1. Prompt user for input
            System.out.print("Enter directory path: ");
            String path = scanner.nextLine();
            File directory = new File(path);

            // Validation
            if (!directory.exists() || !directory.isDirectory()) {
                System.err.println("Error: Invalid directory path.");
                return;
            }

            File[] files = directory.listFiles();
            if (files == null) {
                System.err.println("Error: Unable to read files.");
                return;
            }

            // 2. Create Threads (Real-Time Concept: Parallel Execution)
            // Thread 1: Counts Java Files
            FileCounterTask javaCounter = new FileCounterTask(files, "JAVA");
            // Thread 2: Counts Issue Files
            FileCounterTask issueCounter = new FileCounterTask(files, "ISSUE");

            // 3. Start Threads
            javaCounter.start();
            issueCounter.start();

            // 4. Wait for Threads to finish (Synchronization)
            // The main system waits here until the workers are done
            javaCounter.join();
            issueCounter.join();

            // 5. Display Results
            System.out.println("Number of Java Files = " + javaCounter.getCount());
            System.out.println("Number of Issues     = " + issueCounter.getCount());
            System.out.println("----------------------------------------");

        } catch (InterruptedException e) {
            System.err.println("System interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}


class FileCounterTask extends Thread {
    private File[] files;
    private String taskType;
    private int count = 0;

    public FileCounterTask(File[] files, String taskType) {
        this.files = files;
        this.taskType = taskType;
    }

    @Override
    public void run() {
        // Loop through the file list
        for (File file : files) {
            if (file.isFile()) {
                String name = file.getName();

                // Logic for counting Java files
                if (taskType.equals("JAVA")) {
                    if (name.endsWith(".java")) {
                        count++;
                    }
                }

                // Logic for counting Issues
                // Assumption: File contains the word "Issue"
                else if (taskType.equals("ISSUE")) {
                    if (name.contains("Issue")) {
                        count++;
                    }
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
}