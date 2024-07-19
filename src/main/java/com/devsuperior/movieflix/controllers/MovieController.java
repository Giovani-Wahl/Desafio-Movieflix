package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITOR','ROLE_MEMBER')")
    @GetMapping
    public ResponseEntity<List<MovieCardDTO>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(movieService.findAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITOR','ROLE_MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<MovieCardDTO> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(movieService.findById(id));
    }
}
