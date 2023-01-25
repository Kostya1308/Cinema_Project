package by.academy.final_project.repository.readers;

import java.io.*;

public class FilesReaderImp implements FilesReader {

    @Override
    public void readProgrammeDescription() {

        try (BufferedReader br = new BufferedReader(new java.io.FileReader("src" + File.separatorChar +
                "files" + File.separatorChar + "ProgrammeDescription.txt"))) {
            BufferedWriter bw = new BufferedWriter(new PrintWriter(System.out));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
