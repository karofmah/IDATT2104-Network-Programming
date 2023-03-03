package p5.backend.service;

import org.springframework.stereotype.Service;
import p5.backend.model.SourceCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

@Service
public class SourceCodeService {

    public String runAndCompile(SourceCode sourceCode){
        String result="";
        try {

            String[] commands = {"docker", "run", "--rm", "python:3.9-alpine", "python","-c",sourceCode.getSourceCode()};


            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);


            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            result = output.toString();
            System.out.println(result);
            int exitCode = process.waitFor();

            // print the process exit code
            System.out.println("Process exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}


