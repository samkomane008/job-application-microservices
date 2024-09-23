package com.review.service.service;

import com.review.service.model.Review;
import com.review.service.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    public ReviewServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReview_ShouldReturnTrue_WhenReviewIsAdded() {
        // Arrange
        Long companyId = 1L;
        Review review = createReview(companyId, "Great company!", "Positive Review", 5.0);

        // Act
        boolean result = reviewService.addReview(companyId, review);

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).save(review);
        assertEquals(companyId, review.getCompanyId());
    }

    @Test
    void addReview_ShouldReturnFalse_WhenCompanyIdIsNull() {
        // Arrange
        Review review = createReview(null, "Great company!", "Positive Review", 5.0);

        // Act
        boolean result = reviewService.addReview(null, review);

        // Assert
        assertFalse(result);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void addReview_ShouldReturnFalse_WhenReviewIsNull() {
        // Act
        boolean result = reviewService.addReview(1L, null);

        // Assert
        assertFalse(result);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void getAllReviews_ShouldReturnListOfReviews_WhenReviewsExist() {
        // Arrange
        Long companyId = 1L;
        Review review1 = createReview(companyId, "Great!", "Positive", 5.0);
        Review review2 = createReview(companyId, "Good!", "Satisfactory", 4.0);
        when(reviewRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(review1, review2));

        // Act
        List<Review> reviews = reviewService.getAllReviews(companyId);

        // Assert
        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        verify(reviewRepository, times(1)).findByCompanyId(companyId);
    }

    @Test
    void getAllReviews_ShouldReturnEmptyList_WhenNoReviewsExist() {
        // Arrange
        Long companyId = 1L;
        when(reviewRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList());

        // Act
        List<Review> reviews = reviewService.getAllReviews(companyId);

        // Assert
        assertNotNull(reviews);
        assertTrue(reviews.isEmpty());
        verify(reviewRepository, times(1)).findByCompanyId(companyId);
    }

    @Test
    void getReview_ShouldReturnReview_WhenReviewExists() {
        // Arrange
        Long reviewId = 1L;
        Review review = createReview(1L, "Great!", "Positive", 5.0);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        Review result = reviewService.getReview(reviewId);

        // Assert
        assertNotNull(result);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void getReview_ShouldReturnNull_WhenReviewDoesNotExist() {
        // Arrange
        Long reviewId = 1L;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Act
        Review result = reviewService.getReview(reviewId);

        // Assert
        assertNull(result);
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void updateReview_ShouldReturnTrue_WhenReviewIsUpdated() {
        // Arrange
        Long reviewId = 1L;
        Review existingReview = createReview(1L, "Old description", "Old title", 3.0);
        Review updatedReview = createReview(1L, "Updated description", "Updated title", 5.0);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));

        // Act
        boolean result = reviewService.updateReview(reviewId, updatedReview);

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).save(existingReview);
        assertEquals("Updated description", existingReview.getDescription());
        assertEquals("Updated title", existingReview.getTitle());
        assertEquals(5.0, existingReview.getRating());
    }

    @Test
    void updateReview_ShouldReturnFalse_WhenReviewDoesNotExist() {
        // Arrange
        Long reviewId = 1L;
        Review updatedReview = createReview(1L, "Updated description", "Updated title", 5.0);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // Act
        boolean result = reviewService.updateReview(reviewId, updatedReview);

        // Assert
        assertFalse(result);
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void deleteReview_ShouldReturnTrue_WhenReviewIsDeleted() {
        // Arrange
        Long reviewId = 1L;

        // Act
        boolean result = reviewService.deleteReview(reviewId);

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void deleteReview_ShouldReturnFalse_WhenReviewIdIsNull() {
        // Act
        boolean result = reviewService.deleteReview(null);

        // Assert
        assertFalse(result);
        verify(reviewRepository, never()).deleteById(any());
    }

    private Review createReview(Long companyId, String description, String title, double rating) {
        return Review.builder()
                .companyId(companyId)
                .description(description)
                .title(title)
                .rating(rating)
                .build();
    }
}
