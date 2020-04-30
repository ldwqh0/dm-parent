package com.dm.sample.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sessions")
public class SessionController {

    @Autowired
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @GetMapping
    public Map<String, ? extends Session> getAll(@RequestParam("username") String username) {
        return sessionRepository.findByPrincipalName(username);
    }

    @GetMapping("delete/{id}")
    public void deleteByName(@PathVariable("id") String id) {
        Map<String, ? extends Session> sessions = sessionRepository.findByPrincipalName(id);
        sessions.keySet().forEach(sessionRepository::deleteById);
    }
}
