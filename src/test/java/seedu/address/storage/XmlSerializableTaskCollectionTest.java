package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.TaskCollection;
import seedu.address.testutil.TypicalTasks;

public class XmlSerializableTaskCollectionTest {

    private static final Path TEST_DATA_FOLDER = Paths
        .get("src", "test", "data", "XmlSerializableTaskCollectionTest");
    private static final Path TYPICAL_TASKS_FILE = TEST_DATA_FOLDER
        .resolve("typicalTasksInTaskCollection.xml");
    private static final Path INVALID_TASK_FILE = TEST_DATA_FOLDER
        .resolve("invalidTaskInTaskCollection.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        XmlSerializableTaskCollection dataFromFile = XmlUtil.getDataFromFile(TYPICAL_TASKS_FILE,
            XmlSerializableTaskCollection.class);
        TaskCollection taskCollectionFromFile = dataFromFile.toModelType();
        TaskCollection typicalPersonsTaskCollection = TypicalTasks.getTypicalTaskCollections();
        assertEquals(taskCollectionFromFile, typicalPersonsTaskCollection);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        XmlSerializableTaskCollection dataFromFile = XmlUtil.getDataFromFile(INVALID_TASK_FILE,
            XmlSerializableTaskCollection.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

}
