package com.govtech.assignment.services;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.dtos.UserUploadRequest;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.repositories.UserRepository;
import com.govtech.assignment.utilities.CsvHelper;
import com.govtech.assignment.utilities.UserProcessorThread;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    private static final String[] VALID_CSV_HEADERS = { "NAME", "SALARY" };
    private static final String ERROR_FILE_DATA_IS_CORRUPTED = "File data is corrupted";
    private static final String ERROR_INVALID_FILE_TYPE = "Invalid File Type";
    private static final String ERROR_INTERNAL = "Something went wrong";
    private static final List<User> POISON_PILL = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> findUsers(UserGetRequest request) {
        return this.userRepository.findUsersFromRequest(request);
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveAllUsersFromRequest(UserUploadRequest request) throws IOException, RuntimeException {
        validateRequest(request);
        CSVParser parser = CsvHelper.getParserFromFile(request.getFile());
        CsvHelper.validateCsvHeaders(parser, Arrays.asList(VALID_CSV_HEADERS));
        saveUsersFromCsvParser(parser);
    }

    private void saveUsersFromCsvParser(CSVParser parser) throws IOException, RuntimeException {
        BlockingQueue<List<User>> queue = new LinkedBlockingQueue<>();
        AtomicReference<Exception> exception = new AtomicReference<>();
        Thread thread = getCsvProcessorThread(parser, queue, exception);
        try {
            while (thread.isAlive() || queue.size() > 0) {
                List<User> item = queue.take();
                if (item == POISON_PILL) break;
                this.userRepository.saveAll(item);
            }
            if (exception.get() != null) {
                throw exception.get();
            }
        } catch (IOException e) {
            throw new IOException(ERROR_FILE_DATA_IS_CORRUPTED);
        } catch (Exception e) {
            throw new RuntimeException(ERROR_INTERNAL);
        }
    }

    private Thread getCsvProcessorThread(
            CSVParser parser,
            BlockingQueue<List<User>> queue,
            AtomicReference<Exception> exception) {
        Thread userProcessorThread = new UserProcessorThread(parser, queue, exception, this, POISON_PILL);
        userProcessorThread.start();
        return userProcessorThread;
    }

    public User convertCsvRecordToUser(CSVRecord record) throws IOException {
        String name = record.get(VALID_CSV_HEADERS[0]);
        String salaryString = record.get(VALID_CSV_HEADERS[1]);
        if (Strings.isEmpty(name) || !NumberUtils.isNumber(salaryString)) {
            throw new IOException(ERROR_FILE_DATA_IS_CORRUPTED);
        }
        double salary = Double.parseDouble(salaryString);
        return salary < 0 ? null : new User(name, salary);
    }

    private void validateRequest(UserUploadRequest request) throws IOException {
        if (request == null || request.getFile() == null || !CsvHelper.isCSVFormat(request.getFile())) {
            logger.error("Invalid User Upload Request");
            throw new IOException(ERROR_INVALID_FILE_TYPE);
        }
    }
}
