package com.review.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.service.messaging.ReviewMessageProducer;
import com.review.service.model.Review;
import com.review.service.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMessageProducer reviewMessageProducer;

    @InjectMocks
    private ReviewController reviewController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper
    }

    @Test
    void getAllReviews_ShouldReturnReviews_WhenCompanyIdIsValid() throws Exception {
        Long companyId = 1L;
        Review review1 = new Review(1L, "Great!", "Positive", 5.0, companyId);
        Review review2 = new Review(2L, "Good!", "Satisfactory", 4.0, companyId);
        when(reviewService.getAllReviews(companyId)).thenReturn(Arrays.asList(review1, review2));

        mockMvc.perform(get("/reviews?companyId=" + companyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Great!"))
                .andExpect(jsonPath("$[1].title").value("Good!"));

        verify(reviewService, times(1)).getAllReviews(companyId);
    }

    @Test
    void getAllReviews_ShouldReturnEmptyList_WhenNoReviewsExist() throws Exception {
        Long companyId = 1L;
        when(reviewService.getAllReviews(companyId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reviews?companyId=" + companyId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(reviewService, times(1)).getAllReviews(companyId);
    }

    @Test
    void addReview_ShouldReturnCreated_WhenReviewIsSaved() throws Exception {
        Long companyId = 1L;
        Review review = new Review(null, "Great!", "Positive", 5.0, companyId);
        when(reviewService.addReview(eq(companyId), any())).thenReturn(true);

        mockMvc.perform(post("/reviews/" + companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))) // Use ObjectMapper to convert to JSON
                .andExpect(status().isCreated())
                .andExpect(content().string("Review Added Successfully!"));

        verify(reviewService, times(1)).addReview(eq(companyId), any());
        verify(reviewMessageProducer, times(1)).sendMessage(any());
    }

    @Test
    void addReview_ShouldReturnNotFound_WhenReviewIsNotSaved() throws Exception {
        Long companyId = 1L;
        Review review = new Review(null, "Great!", "Positive", 5.0, companyId);
        when(reviewService.addReview(eq(companyId), any())).thenReturn(false);

        mockMvc.perform(post("/reviews/" + companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))) // Use ObjectMapper to convert to JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Review Not Found"));

        verify(reviewService, times(1)).addReview(eq(companyId), any());
        verify(reviewMessageProducer, never()).sendMessage(any());
    }

    @Test
    void getReview_ShouldReturnReview_WhenReviewExists() throws Exception {
        Long reviewId = 1L;
        Review review = new Review(reviewId, "Great!", "Positive", 5.0, 1L);
        when(reviewService.getReview(reviewId)).thenReturn(review);

        mockMvc.perform(get("/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Great!"));

        verify(reviewService, times(1)).getReview(reviewId);
    }

    @Test
    void getReview_ShouldReturnNotFound_WhenReviewDoesNotExist() throws Exception {
        Long reviewId = 100L;
        when(reviewService.getReview(reviewId)).thenReturn(null);

        mockMvc.perform(get("/reviews/" + reviewId))
                .andExpect(status().isNotFound());

        verify(reviewService, times(1)).getReview(reviewId);
    }

    @Test
    void updateReview_ShouldReturnOk_WhenReviewIsUpdated() throws Exception {
        Long reviewId = 1L;
        Review review = new Review(reviewId, "Updated", "Update", 4.0, 1L);
        when(reviewService.updateReview(eq(reviewId), any())).thenReturn(true);

        mockMvc.perform(put("/reviews/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))) // Use ObjectMapper to convert to JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Review Updated Successfully"));

        verify(reviewService, times(1)).updateReview(eq(reviewId), any());
    }

    @Test
    void updateReview_ShouldReturnNotFound_WhenReviewDoesNotExist() throws Exception {
        Long reviewId = 1L;
        Review review = new Review(reviewId, "Updated", "Update", 4.0, 1L);
        when(reviewService.updateReview(eq(reviewId), any())).thenReturn(false);

        mockMvc.perform(put("/reviews/" + reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review))) // Use ObjectMapper to convert to JSON
                .andExpect(status().isNotFound())
                .andExpect(content().string("Review Not Found"));

        verify(reviewService, times(1)).updateReview(eq(reviewId), any());
    }

    @Test
    void deleteReview_ShouldReturnOk_WhenReviewIsDeleted() throws Exception {
        Long reviewId = 1L;
        when(reviewService.deleteReview(reviewId)).thenReturn(true);

        mockMvc.perform(delete("/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(content().string("Review Deleted Successfully"));

        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    @Test
    void deleteReview_ShouldReturnNotFound_WhenReviewDoesNotExist() throws Exception {
        Long reviewId = 1L;
        when(reviewService.deleteReview(reviewId)).thenReturn(false);

        mockMvc.perform(delete("/reviews/" + reviewId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Review Not Found"));

        verify(reviewService, times(1)).deleteReview(reviewId);
    }

    @Test
    void getAverageReview_ShouldReturnAverage_WhenReviewsExist() throws Exception {
        Long companyId = 1L;
        Review review1 = new Review(1L, "Good", "Good", 4.0, companyId);
        Review review2 = new Review(2L, "Great", "Great", 5.0, companyId);
        when(reviewService.getAllReviews(companyId)).thenReturn(Arrays.asList(review1, review2));

        mockMvc.perform(get("/reviews/averageRating?companyId=" + companyId))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));

        verify(reviewService, times(1)).getAllReviews(companyId);
    }

    @Test
    void getAverageReview_ShouldReturnZero_WhenNoReviewsExist() throws Exception {
        Long companyId = 1L;
        when(reviewService.getAllReviews(companyId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reviews/averageRating?companyId=" + companyId))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));

        verify(reviewService, times(1)).getAllReviews(companyId);
    }
}
