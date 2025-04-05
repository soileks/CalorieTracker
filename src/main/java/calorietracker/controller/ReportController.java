package calorietracker.controller;

import calorietracker.service.CalorieReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final CalorieReportService reportService;

    @GetMapping("/daily/{userId}")
    public ResponseEntity<CalorieReportService.DailyReport> getDailyReport(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null) {
            date = LocalDate.now();
        }

        return ResponseEntity.ok(reportService.getDailyReport(userId, date));
    }
}
