package com.retail.rewards.service;

import com.retail.rewards.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {

    public int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
            amount = 100;
        }
        if (amount > 50) {
            points += (amount - 50);
        }
        return points;
    }

    public Map<String, Integer> getPointsForCustomer(Long customerId, List<Transaction> transactions) {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        Map<String, List<Transaction>> transactionsByMonth = transactions.stream()
                .filter(t -> t.getCustomerId().equals(customerId))
                .collect(Collectors.groupingBy(t -> t.getDate().getMonth().toString()));

        transactionsByMonth.forEach((month, transactionList) -> {
            int totalPoints = transactionList.stream()
                    .mapToInt(t -> calculatePoints(t.getAmount()))
                    .sum();
            monthlyPoints.put(month, totalPoints);
        });

        int totalPoints = monthlyPoints.values().stream().mapToInt(Integer::intValue).sum();
        monthlyPoints.put("Total", totalPoints);

        return monthlyPoints;
    }
}