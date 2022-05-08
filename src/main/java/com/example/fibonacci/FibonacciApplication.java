package com.example.fibonacci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("fibonacci")
public class FibonacciApplication {

    public static void main(String[] args) {
        SpringApplication.run(FibonacciApplication.class, args);
    }


    @GetMapping("findNumber")
    public ResponseEntity<String> findFibonacciNumber(@RequestParam int n) {
        int fibNum;
        try {
            fibNum = fibonacci(n);
        } catch (FibonacciOutOfRangeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(String.valueOf(fibNum));
    }

   @PostMapping("createSequence")
   public ResponseEntity<String> generateFibonacciSequence(@RequestParam String n) {
        List<Integer> sequence;
        try {
            sequence = getSequence(n);
        } catch (FibonacciInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        String fileName;
        try {
            fileName = storeSequence(sequence);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(fileName);
   }

   @GetMapping("getSequence")
   public ResponseEntity<String> retrieveFibonacciSequence(@RequestParam String fileName) {
        String sequence;
        try {
            sequence = getSequenceByFilename(fileName);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not available " + e.getMessage());
        }
        return ResponseEntity.ok(sequence);
   }
   private String getSequenceByFilename(String filename) throws FileNotFoundException {
       BufferedReader reader = new BufferedReader(new FileReader(filename));
       return reader.lines().collect(Collectors.joining());
   }

    private List<Integer> getSequence(String str) throws FibonacciInputException {
        int n;
        try {
            n = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new FibonacciInputException("Invalid Input.");
        }
        List<Integer> sequence = new ArrayList<>();
        sequence.add(0);
        int prev = 0;
        int curr = 1;
        int index = 1;
        while (index <= n) {
            sequence.add(curr);
            int next = prev + curr;
            prev = curr;
            curr = next;
            index++;
        }
        return sequence;
    }
    private String storeSequence(List<Integer> sequence) throws IOException {
        String name = "fibonacci.txt";
        File file = new File(name);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(sequence.toString());
        writer.flush();
        writer.close();
        return name;
    }

    private int fibonacci(int position) throws FibonacciOutOfRangeException {
        if (position <= 1) {
            return position;
        }
        if (position >= 8) {
            throw new FibonacciOutOfRangeException(String.format("Request position %s is too large.", ""));
        }
        return fibonacci(position -1) + fibonacci(position -2);
    }
}
