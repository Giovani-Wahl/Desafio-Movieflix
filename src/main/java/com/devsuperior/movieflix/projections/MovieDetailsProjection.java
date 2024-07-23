package com.devsuperior.movieflix.projections;

import com.devsuperior.movieflix.dto.GenreDTO;

public interface MovieDetailsProjection extends IdProjection<Long>{
    String getTitle();
    String getSubTitle();
    Integer getYear();
    String getImgUrl();
    String getSynopsis();
    GenreDTO getGenre();
}
