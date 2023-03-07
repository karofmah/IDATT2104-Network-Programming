package p5.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import p5.backend.controller.SourceCodeController;
import p5.backend.model.SourceCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class SourceCodeService {
    private static final Logger _logger =
            LoggerFactory.getLogger(SourceCodeController.class);

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


            System.out.println("Process exited with code " + exitCode);


        } catch (IOException | InterruptedException e) {
            _logger.warn(e.getMessage());
        }
        _logger.info(result);
        return result;
    }
}


