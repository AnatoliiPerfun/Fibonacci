package com.example.fibonacci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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

   @PostMapping("createSequence")
   public ResponseEntity<String> generateFibonacciSequence(@RequestParam int n) throws IOException {
        List<Integer> sequence = getSequence(n);
        return ResponseEntity.ok(storeSequence(sequence));
   }

   @GetMapping("getSequence")
   public ResponseEntity<String> retrieveFibonacciSequence(@RequestParam String fileName) throws IOException {
        return ResponseEntity.ok(getSequence(fileName));
   }
   private String getSequence(String filename) throws FileNotFoundException {
       BufferedReader reader = new BufferedReader(new FileReader(filename));
       return reader.lines().collect(Collectors.joining());
   }

    private List<Integer> getSequence(int n) {
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
}
