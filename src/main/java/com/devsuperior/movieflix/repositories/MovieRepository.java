package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM(
            SELECT DISTINCT tb_movie.id, tb_movie.title
            FROM tb_movie
            WHERE (:genreIds IS NULL OR tb_movie.genre_id = :genreIds)) AS tb_result
            """,
    countQuery = """
            SELECT COUNT(*) FROM(
            SELECT DISTINCT tb_movie.id, tb_movie.title
            FROM tb_movie
            WHERE (:genreIds IS NULL OR tb_movie.genre_id = :genreIds)) AS tb_result
            """)
    Page<MovieCardProjection> searchMovies(List<Long> genreIds,Pageable pageable);

    @Query("SELECT obj FROM Movie obj WHERE obj.genre IN :genreIds")
    List<Movie> searchProductsWithCategories(List<Long> genreIds);
}
