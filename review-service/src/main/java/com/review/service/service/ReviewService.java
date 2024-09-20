package com.review.service.service;

import com.review.service.model.Review;
import com.review.service.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // Add review
    public boolean addReview(Long companyId, Review review) {
        if (companyId != null & review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    // Get all reviews
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    // Get review by Id
    public Review getReview(Long reviewId){
        return reviewRepository.findById(reviewId).orElse(null);
    }

    // Update review
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setCompanyId(updatedReview.getCompanyId());
            review.setDescription(updatedReview.getDescription());
            review.setTitle(updatedReview.getTitle());
            review.setRating(updatedReview.getRating());
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    // Delete review
    public boolean deleteReview(Long reviewId) {
        if (reviewId != null) {
            reviewRepository.deleteById(reviewId);
            return true;
        } else {
            return false;
        }
    }
}
