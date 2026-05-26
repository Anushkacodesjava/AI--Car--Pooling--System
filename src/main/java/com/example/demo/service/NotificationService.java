package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class NotificationService {
    @Autowired private NotificationRepository repo;
    @Autowired private PoolUserRepository userRepo;

    public Notification send(Long userId, String title, String message, String type) {
        PoolUser u = userRepo.findById(userId).orElseThrow();
        Notification n = new Notification();
        n.setUser(u); n.setTitle(title); n.setMessage(message); n.setType(type);
        return repo.save(n);
    }

    public List<Notification> getByUser(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public long countUnread(Long userId) {
        return repo.countUnread(userId);
    }

    public Notification markRead(Long id) {
        return repo.findById(id).map(n -> {
            n.setIsRead(true);
            return repo.save(n);
        }).orElse(null);
    }

    // Fixed: was doing N individual saves in a loop — now uses a single bulk update query
    @Transactional
    public void markAllRead(Long userId) {
        repo.markAllReadByUserId(userId);
    }

    // Broadcast: still per-user saves but wrapped in one transaction
    @Transactional
    public void broadcast(String title, String message, String type) {
        userRepo.findAll().forEach(u -> {
            Notification n = new Notification();
            n.setUser(u); n.setTitle(title); n.setMessage(message); n.setType(type);
            repo.save(n);
        });
    }
}
