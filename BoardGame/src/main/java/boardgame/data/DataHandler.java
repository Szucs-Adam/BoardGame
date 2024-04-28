package boardgame.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.tinylog.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    /**
     * The file path used for reading and writing data.
     */
    private String filePath;

    /**
     * The Gson instance used for JSON serialization and deserialization.
     */
    private Gson gson;

    /**
     * Constructs a new DataHandler instance with the specified file path.
     *
     * @param pathOftheFile The path to the data file.
     */
    public DataHandler(final String pathOftheFile) {
        this.filePath = pathOftheFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.info("DataHandler initialized. File path: " + pathOftheFile);
    }
    /**
     * Writes the data to the file.
     *
     * @param dataList The list of data to be written.
     */
    private void writeData(final List<Data> dataList) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(dataList, writer);
            writer.flush();
            Logger.info("Data written to the file: " + filePath);
        } catch (IOException e) {
            Logger.error("Error while writing data to file: " + filePath, e);
        }
    }
    /**
     * Appends new data to the file.
     *
     * @param winnerColor The winner color.
     * @param moveCounterOfTheWinner The move counter of the winner.
     * @param state The end state of the game.
     */
    public void extendData(final String winnerColor,
                  final int moveCounterOfTheWinner, final String state) {
        Data data = new Data(winnerColor, moveCounterOfTheWinner, state);
        List<Data> dataList = readData();
        dataList.add(data);
        writeData(dataList);
        Logger.info("Appended new data: " + data);
    }

    /**
     * Reads the data from the file.
     *
     * @return The list of read data.
     */
    public List<Data> readData() {
        try {
            String fileContent =
                    Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
            Logger.info("Data loaded from file: " + filePath);
            return gson.fromJson(fileContent,
                    new TypeToken<List<Data>>() { }.getType());
        } catch (IOException e) {
            Logger.error("Error while reading data", e);
            return new ArrayList<>();
        }
    }
}
