package com.fyp.producerservice.controller;


import com.fyp.producerservice.service.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/producer")
public class ProducerController {

    @Autowired
    KafkaSender kafkaSender;

    @PostMapping("/notify-followers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void notifyFollowers(@RequestParam(value = "uuid") String uuid) {
        kafkaSender.send(uuid);
    }
}
