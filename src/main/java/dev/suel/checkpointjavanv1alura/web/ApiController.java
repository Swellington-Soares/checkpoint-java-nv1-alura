package dev.suel.checkpointjavanv1alura.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiController {

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("API 1.0");
    }
}
