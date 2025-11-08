package ca.gbc.comp3095.event.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/api/events/ping")
    public String ping() {
        return "event-service: OK";
    }
}
