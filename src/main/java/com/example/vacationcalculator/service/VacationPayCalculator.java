package com.example.vacationcalculator.service;

/**
 * Сервис для вычисления суммы отпускных выплат по среднему заработку
 * и количеству рабочих дней отпуска
 */
public interface VacationPayCalculator {
    double calculateVacationPay(double avgMonthlySalary, int workingDays);
}