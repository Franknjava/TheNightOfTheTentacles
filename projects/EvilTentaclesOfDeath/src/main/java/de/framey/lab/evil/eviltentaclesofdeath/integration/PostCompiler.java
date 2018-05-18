package de.framey.lab.evil.eviltentaclesofdeath.integration;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import de.framey.lab.evil.eviltentaclesofdeath.asm.TentacleHandler;
import lombok.extern.java.Log;

/**
 * Simple post compile application.This is the easiest way to summon Cthulhu on your class files. Simply provide a list of directories as
 * command line parameters and all class files in all those directories will get their tentacles attached.
 * <p>
 * The {@link PostCompiler} can be called like this directly or from any build system you prefer:
 * <p>
 * <code>
 * java -cp Tentacle.jar
 * de.framey.lab.evil.eviltentaclesofdeath.integration.PostCompiler &lt;CLASS DIR 1&gt; ... &lt;CLASS DIR n&gt;
 * </code>
 * </p>
 * </p>
 *
 * @author Frank Meyfarth
 */
@Log
public class PostCompiler {

    /**
     * Main method of the {@link PostCompiler}. Each parameter represents a directory containing class files to be mingled with. For each
     * directory {@link #walkFileTree(String)} will be called in a parallel stream.
     *
     * @param args
     *            a list of class folders to cthulhufy
     * @throws Exception
     *             in the rare case Cthulhu gets angry
     */
    public static void main(String[] args) throws Exception {
        Arrays.asList(args).stream().parallel().forEach(PostCompiler::walkFileTree);
    }

    /**
     * Simply walks the tree of the given directory and calls {@link #transform(Path)} on any file found.
     *
     * @param root
     *            of the directory to scan
     */
    private static void walkFileTree(String root) {
        try {
            Files.walkFileTree(FileSystems.getDefault().getPath(root), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    return transform(file);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This does the job. Reads the file into memory, calls {@link TentacleHandler#transform(byte[])} on its contents and write it back to
     * disk.
     *
     * @param file
     *            the file to transform
     * @return the transformed file with tentacles attached
     * @throws IOException
     *             if the file cannot be read or written
     */
    private static final FileVisitResult transform(Path file) throws IOException {
        String name = file.toFile().getName();
        if (name.endsWith(".class")) {
            byte[] code = Files.readAllBytes(file);
            code = TentacleHandler.transform(code);
            Files.write(file, code, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("File transformed: " + file.getFileName() + " ["+code.length+" Bytes]");
        }
        return FileVisitResult.CONTINUE;
    }
}
