package com.example.demo.service;

import com.example.demo.model.RideRequest;
import com.example.demo.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.*;

@Service
public class RideMatchService {
    @Autowired private RideRequestRepository repo;

    public List<RideRequest> search(String source, String dest, String date, String time,
                                    Integer maxPrice, String vehicleType,
                                    Boolean acOnly, Boolean ladiesOnly) {
        final String src = norm(source), dst = norm(dest);

        // DB-level pre-filter: only active rides with seats available.
        // Previously used repo.findAll() which loaded every row regardless.
        return repo.findAllActiveRides().stream()
            .filter(r -> maxPrice == null || r.getPricePerSeat() == null || r.getPricePerSeat() <= maxPrice)
            .filter(r -> vehicleType == null || vehicleType.isEmpty()
                      || vehicleType.equalsIgnoreCase(r.getVehicleType()))
            .filter(r -> !Boolean.TRUE.equals(acOnly)     || Boolean.TRUE.equals(r.getAcAvailable()))
            .filter(r -> !Boolean.TRUE.equals(ladiesOnly)  || Boolean.TRUE.equals(r.getLadiesOnly()))
            .map(r  -> { computeScore(r, src, dst, date, time); return r; })
            .filter(r -> r.getAiMatchScore() > 0)
            .sorted(Comparator.comparingInt(RideRequest::getAiMatchScore).reversed())
            .collect(Collectors.toList());
    }

    private void computeScore(RideRequest r, String src, String dst, String date, String time) {
        if (r.getSourceLocation() == null || r.getDestinationLocation() == null) {
            r.setAiMatchScore(0); return;
        }
        String rs = norm(r.getSourceLocation()), rd = norm(r.getDestinationLocation());
        int score = 0;

        // Location matching: source 0-40, dest 0-40
        score += locScore(rs, src);
        score += locScore(rd, dst);

        // Date proximity bonus: 0-12
        if (date != null && !date.isEmpty() && r.getRideDate() != null) {
            try {
                long days = Math.abs(ChronoUnit.DAYS.between(
                    LocalDate.parse(r.getRideDate()), LocalDate.parse(date)));
                score += days == 0 ? 12 : days == 1 ? 7 : days <= 3 ? 3 : 0;
            } catch (Exception ignored) {}
        }

        // Time proximity bonus: 0-8
        if (time != null && !time.isEmpty() && r.getRideTime() != null) {
            try {
                long mins = Math.abs(ChronoUnit.MINUTES.between(
                    LocalTime.parse(r.getRideTime()), LocalTime.parse(time)));
                score += mins <= 15 ? 8 : mins <= 30 ? 5 : mins <= 60 ? 2 : 0;
            } catch (Exception ignored) {}
        }

        // Seat availability bonus: 0-5
        if (r.getAvailableSeats() != null) score += Math.min(r.getAvailableSeats(), 5);

        // Freshness bonus (recently posted rides rank higher): 0-5
        if (r.getCreatedAt() != null) {
            long hours = ChronoUnit.HOURS.between(r.getCreatedAt(), LocalDateTime.now());
            score += hours < 6 ? 5 : hours < 24 ? 3 : hours < 72 ? 1 : 0;
        }

        // Price competitiveness bonus: 0-5 (cheaper rides score higher)
        if (r.getPricePerSeat() != null) {
            score += r.getPricePerSeat() < 50 ? 5 : r.getPricePerSeat() < 100 ? 3
                   : r.getPricePerSeat() < 200 ? 1 : 0;
        }

        score = Math.min(score, 100);
        r.setAiMatchScore(score);
        r.setAiMatchLabel(score >= 85 ? "Excellent Match" : score >= 65 ? "Great Match"
                        : score >= 45 ? "Good Match" : score >= 25 ? "Possible Match" : "Low Match");
    }

    private int locScore(String a, String b) {
        if (b == null || b.isEmpty()) return 15; // no filter = neutral
        if (a.equals(b))              return 40;
        if (a.contains(b) || b.contains(a)) return 32;
        int ws = wordOverlap(a, b);
        if (ws > 0) return ws;
        int lev = lev(a, b);
        return lev <= 2 ? 20 : lev <= 4 ? 12 : lev <= 6 ? 6 : 0;
    }

    private int wordOverlap(String a, String b) {
        String[] wa = a.split("\\s+"), wb = b.split("\\s+");
        int m = 0;
        for (String x : wa)
            for (String y : wb)
                if (x.length() > 2 && y.length() > 2
                        && (x.equals(y) || x.contains(y) || y.contains(x))) m++;
        return m > 0 ? Math.min(m * 12, 28) : 0;
    }

    private String norm(String s) {
        return s == null ? "" : s.toLowerCase().trim()
               .replaceAll("[,.]", " ").replaceAll("\\s+", " ");
    }

    private int lev(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++)
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else dp[i][j] = Math.min(
                    dp[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 1),
                    Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1));
            }
        return dp[a.length()][b.length()];
    }
}
