package com.ynz.demo.printcsvfile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class PrintCsvFile {

  record Person(String name, Integer age) {
      public List<String> toCsvRecord() {
        return List.of(name, age.toString());
      }
  }

  static List<Person> persons;

  static {
    persons = List.of(new Person("Mike", 20), new Person("Mia", 23));
  }

  public static void main(String[] args) throws IOException {

    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter =
            new CSVPrinter(
                new PrintWriter(byteArrayOutputStream),
                CSVFormat.DEFAULT
                    .builder()
                    .setHeader(getHeaders().toArray(new String[0]))
                    .build());
        FileWriter fileWriter = new FileWriter("csvOutputFile.csv", StandardCharsets.UTF_8)) {

      for (var person : persons) {
        csvPrinter.printRecord(person.toCsvRecord());
      }

      // flush formatted csv content into the underlying stream(buffer).
      csvPrinter.flush();

      // write buffer into a physical file
      fileWriter.write(byteArrayOutputStream.toString(StandardCharsets.UTF_8));

      // flush buffer content into underlying physical file.
      fileWriter.flush();
    } catch (IOException e) {
      log.error("error message: {}", e.getMessage());
    }
  }

  static List<String> getHeaders() {
    return List.of("Name", "Age");
  }
}
