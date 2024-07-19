package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @Transactional(readOnly = true)
    public List<MovieCardDTO> findAll(){
        List<Movie> result = movieRepository.findAll();
        return result.stream()
                .map(MovieCardDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieCardDTO findById(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id Not Found."));
        return new MovieCardDTO(movie);
    }
}
