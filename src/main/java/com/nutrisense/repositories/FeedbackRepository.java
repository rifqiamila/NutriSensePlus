package com.nutrisense.repositories;

import com.nutrisense.models.laporan.Feedback;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackRepository extends JsonRepository<Feedback> {
    
    private static final String FEEDBACK_FILE_PATH = "src/main/resources/data/feedback.json";

    public FeedbackRepository() {
        super(FEEDBACK_FILE_PATH, Feedback.class);
    }

    // Feedback-specific methods
    public List<Feedback> findByDapur(String dapurId) {
        return findAll().stream()
                .filter(feedback -> dapurId.equals(feedback.getDapurId()))
                .collect(Collectors.toList());
    }

    public List<Feedback> findByMenu(String menuId) {
        return findAll().stream()
                .filter(feedback -> menuId.equals(feedback.getMenuId()))
                .collect(Collectors.toList());
    }

    public List<Feedback> findBySiswa(String siswaId) {
        return findAll().stream()
                .filter(feedback -> siswaId.equals(feedback.getSiswaId()))
                .collect(Collectors.toList());
    }

    public List<Feedback> findActiveFeedback() {
        return findAll().stream()
                .filter(Feedback::isActive)
                .collect(Collectors.toList());
    }

    public double getAverageRatingByDapur(String dapurId) {
        List<Feedback> dapurFeedback = findByDapur(dapurId).stream()
                .filter(Feedback::isActive)
                .collect(Collectors.toList());

        if (dapurFeedback.isEmpty()) return 0;

        return dapurFeedback.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0);
    }

    public double getAverageRatingByMenu(String menuId) {
        List<Feedback> menuFeedback = findByMenu(menuId).stream()
                .filter(Feedback::isActive)
                .collect(Collectors.toList());

        if (menuFeedback.isEmpty()) return 0;

        return menuFeedback.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0);
    }
}