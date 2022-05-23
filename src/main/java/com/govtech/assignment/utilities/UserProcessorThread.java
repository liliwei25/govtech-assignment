package com.govtech.assignment.utilities;

import com.govtech.assignment.entities.User;
import com.govtech.assignment.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public class UserProcessorThread extends Thread {
    public static final int BATCH_SIZE = 500;

    private CSVParser parser;
    private BlockingQueue<List<User>> queue;
    private AtomicReference<Exception> exception;
    private UserService userService;
    private List<User> poisonPill;

    @Override
    public void run() {
        List<User> users = new ArrayList<>();
        try {
            for (CSVRecord record : parser) {
                User user = userService.convertCsvRecordToUser(record);
                if (user != null) {
                    users.add(user);
                }
                if (users.size() >= BATCH_SIZE) {
                    queue.put(users);
                    users = new ArrayList<>();
                }
            }
            queue.put(poisonPill);
        } catch (Exception e) {
            exception.set(e);
            queue.add(poisonPill);
        }
    }
}
