package com.eventmanager.event_management.Repository;

import com.eventmanager.event_management.Model.ColorTheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColorThemeRepository extends JpaRepository<ColorTheme, Long> {
    List<ColorTheme> findAll();

    Optional<ColorTheme> findByName(String name);
}
