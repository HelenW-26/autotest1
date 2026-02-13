package newcrm.utils.download;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DownloadFile {

    public static Path tempDirPath;

    // Create temporary download folder
    public static void createTempFolder() {
        try {
            tempDirPath = Files.createTempDirectory("selenium_downloads_");
            System.out.println("Temp download folder path: " + tempDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary folder", e);
        }
    }

    // Delete folder and all files recursively
    public static void deleteTempFolder() {
        try {
            if (tempDirPath == null || !Files.exists(tempDirPath)) {
                return;
            }

            Files.walkFileTree(tempDirPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        System.err.println("Could not delete file: " + file + " (" + e.getMessage() + ")");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    try {
                        Files.delete(dir);
                    } catch (IOException e) {
                        System.err.println("Could not delete folder: " + dir + " (" + e.getMessage() + ")");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("Delete download folder in the path: " + tempDirPath);
            tempDirPath = null;

        } catch (Exception e) {
            System.err.println("Failed to delete temporary folder: " + e.getMessage());
        }
    }

    // Delete specific file in download folder
    public static void deleteDownloadedFile(String fileName) {
        try {
            if (fileName == null || fileName.isEmpty()) {
                return;
            }

            Path filePath = tempDirPath.resolve(fileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("Deleted downloaded file: " + fileName);
            } else {
                System.out.println("File not found: " + fileName);
            }

        } catch (Exception e) {
            System.err.println("Failed to delete file: " + e.getMessage());
        }
    }

    // Wait for downloads to finish (no .crdownload files remain)
    public static Path waitForDownloadsToFinish(int timeoutSeconds) {
        if (tempDirPath == null || !Files.exists(tempDirPath)) {
            System.err.println("Download folder not found or not created yet.");
            return null;
        }

        try {
            int waited = 0;
            Path latestFile = null;

            while (waited < timeoutSeconds) {
                boolean downloading = false;
                long latestModifiedTime = 0;

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDirPath)) {
                    for (Path file : stream) {
                        String name = file.getFileName().toString();

                        // Detect if still downloading
                        if (name.endsWith(".crdownload") || name.endsWith(".tmp")) {
                            downloading = true;
                            break;
                        }

                        // Track latest modified file
                        long modified = Files.getLastModifiedTime(file).toMillis();
                        if (modified > latestModifiedTime) {
                            latestModifiedTime = modified;
                            latestFile = file;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading download folder: " + e.getMessage());
                    break;
                }

                if (downloading) {
                    System.out.println("Downloading in progress... waiting 5 seconds.");
                    Thread.sleep(5000);
                    waited += 5;
                    continue;
                }

                if (latestFile != null && Files.exists(latestFile)) {
                    System.out.println("Download complete: " + latestFile.getFileName());
                    return latestFile;
                }

                Thread.sleep(1000);
                waited++;
            }

            System.out.println("Timeout (" + timeoutSeconds + "s): Download not finished.");
            return null;

        } catch (InterruptedException e) {
            System.err.println("Wait interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error while waiting for downloads: " + e.getMessage());
            return null;
        }
    }

}
