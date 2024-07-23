package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;
import com.devsuperior.movieflix.projections.MovieDetailsProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public Page<MovieDetailsDTO> findAllPaged(String genreId,Pageable pageable){
        List<Long> genreIds = new ArrayList<>();
        if (!"0".equals(genreId)){
            genreIds = Arrays.stream(genreId.split(",")).map(Long::parseLong).toList();
        }
        Page<MovieDetailsProjection> page = movieRepository.searchMovies(genreIds,pageable);
        List<Long> movieIds = page.map(MovieDetailsProjection::getId).toList();
        List<Movie> entities = movieRepository.searchProductsWithCategories(movieIds);
        entities = (List<Movie>) Utils.replace(page.getContent(),entities);
        List<MovieDetailsDTO> dtos = entities.stream().map(MovieDetailsDTO::new).toList();
        return new PageImpl<>(dtos,page.getPageable(),page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id Not Found."));
        return new MovieDetailsDTO(movie);
    }
}
