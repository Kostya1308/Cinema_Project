package by.academy.final_project.services.readers.service;

import by.academy.final_project.repository.readers.FilesReader;

public class FilesReaderServiceImpl implements FilesReaderService {
    FilesReader fileReader;

    public FilesReaderServiceImpl(FilesReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public void readProgrammeDescription() {
        fileReader.readProgrammeDescription();
    }
}
