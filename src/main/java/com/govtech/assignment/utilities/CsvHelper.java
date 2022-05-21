package com.govtech.assignment.utilities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class CsvHelper {
    public static final String CSV_TYPE = "text/csv";
    public static final String ERROR_HEADERS_ARE_MISSING = "Headers are missing";

    public static boolean isCSVFormat(MultipartFile file) {
        return CSV_TYPE.equals(file.getContentType());
    }

    public static CSVParser getParserFromFile(MultipartFile file) throws IOException {
        BOMInputStream bomInputStream = new BOMInputStream(file.getInputStream());
        return CSVParser.parse(bomInputStream, Charset.defaultCharset(), CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }

    public static void validateCsvHeaders(CSVParser parser, List<String> headers) throws IOException {
        if (!parser.getHeaderNames().containsAll(headers)) {
            throw new IOException(ERROR_HEADERS_ARE_MISSING);
        }
    }
}
