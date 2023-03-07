package p5.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import p5.backend.model.SourceCode;
import p5.backend.service.SourceCodeService;

@RestController

@CrossOrigin("http://localhost:5173/")

public class SourceCodeController {



    private SourceCodeService sourceCodeService;

    @Autowired
    public void setService(SourceCodeService sourceCodeService) {
        this.sourceCodeService = sourceCodeService;
    }

    @PostMapping("/post")
    public String posted(@RequestBody SourceCode sourceCode) {
            return sourceCodeService.runAndCompile(sourceCode);
    }
}
