package p5.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.Origin;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import p5.backend.model.SourceCode;
import p5.backend.service.SourceCodeService;

@RestController

@CrossOrigin("http://localhost:5173/")

public class SourceCodeController {

    private static final Logger _logger =
            LoggerFactory.getLogger(SourceCodeController.class);

    private SourceCodeService sourceCodeService;

    @Autowired
    public void setService(SourceCodeService sourceCodeService) {
        this.sourceCodeService = sourceCodeService;
    }

    @PostMapping("/post")

    public String posted(@RequestBody SourceCode sourceCode) {
        _logger.info(sourceCode.getSourceCode());
            return sourceCodeService.runAndCompile(sourceCode);
    }
}
