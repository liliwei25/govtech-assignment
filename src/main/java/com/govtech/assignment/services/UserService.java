package com.govtech.assignment.services;

import com.govtech.assignment.dtos.UserGetRequest;
import com.govtech.assignment.dtos.UserUploadRequest;
import com.govtech.assignment.entities.User;
import com.govtech.assignment.repositories.UserRepository;
import com.govtech.assignment.utilities.CsvHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserService {
    private static final String[] VALID_CSV_HEADERS = { "NAME", "SALARY" };
    private static final String ERROR_FILE_DATA_IS_CORRUPTED = "File data is corrupted";
    private static final String ERROR_INVALID_FILE_TYPE = "Invalid File Type";

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Lazy
    private final UserService self;

    public List<User> findUsers(UserGetRequest request) {
        return this.userRepository.findUsersFromRequest(request);
    }

    public void saveAllUsersFromRequest(UserUploadRequest request) throws IOException {
        validateRequest(request);
        CSVParser parser = CsvHelper.getParserFromFile(request.getFile());
        CsvHelper.validateCsvHeaders(parser, Arrays.asList(VALID_CSV_HEADERS));
        saveUsersFromCsvParser(parser);
    }

    private void saveUsersFromCsvParser(CSVParser parser) throws IOException {
        List<User> users = new ArrayList<>();
        for (CSVRecord record : parser) {
            User user = convertCsvRecordToUser(record);
            if (user != null) {
                users.add(user);
            }
        }
        self.saveUsers(users);
    }

    private User convertCsvRecordToUser(CSVRecord record) throws IOException {
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

    @Transactional
    public void saveUsers(Iterable<User> users) {
        this.userRepository.saveAll(users);
    }
}
