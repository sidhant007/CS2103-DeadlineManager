package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTaskCollection;

/**
 * A class to access TaskCollection data stored as an xml file on the hard disk.
 */
public class XmlTaskCollectionStorage implements TaskCollectionStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskCollectionStorage.class);

    private Path filePath;

    public XmlTaskCollectionStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTaskCollection> readAddressBook()
        throws DataConversionException, IOException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}
     *
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskCollection> readAddressBook(Path filePath)
        throws DataConversionException,
        FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("TaskCollection file " + filePath + " not found");
            return Optional.empty();
        }

        XmlSerializableTaskCollection xmlAddressBook = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlAddressBook.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveAddressBook(ReadOnlyTaskCollection addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyTaskCollection)}
     *
     * @param filePath location of the data. Cannot be null
     */
    public void saveAddressBook(ReadOnlyTaskCollection addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableTaskCollection(addressBook));
    }

}