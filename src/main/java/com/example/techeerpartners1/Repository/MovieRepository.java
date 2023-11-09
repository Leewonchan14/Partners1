package com.example.techeerpartners1.Repository;

import com.example.techeerpartners1.Model.DAO.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // find isDelete is not true
    Page<Movie> findByIsDeletedFalse(Pageable pageable);
}
