package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AuthService authService;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, UserService userService, AuthService authService, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.authService = authService;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public ReviewDTO insert(ReviewDTO dto){
       if (dto == null){
           throw new IllegalArgumentException("ReviewDTO cannot be null");
       }
       User user = authService.authenticated();
       if (user == null){
           throw new IllegalStateException("User must be authenticated");
       }
       try {
           Movie movie =movieRepository.getReferenceById(dto.getMovieId());
           Review review = new Review();
           review.setUser(user);
           review.setText(dto.getText());
           review.setMovie(movie);
           reviewRepository.save(review);
           return new ReviewDTO(review);
       }
       catch (EntityNotFoundException e){
           throw new ResourceNotFoundException("Resource not found!");
       }
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> findByMovieId(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Movie with ID " + id + " not found"));
        List<Review> reviews = reviewRepository.findByMovie(movie);
        return reviews.stream().map(ReviewDTO::new).toList();
    }
}
