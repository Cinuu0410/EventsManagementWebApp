package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.ColorTheme;
import com.eventmanager.event_management.Model.Comment;
import com.eventmanager.event_management.Repository.ColorThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorThemeService {

    @Autowired
    private ColorThemeRepository colorThemeRepository;

    public void save(String name, String primaryColor, String secondaryColor, String buttonColor) {
        ColorTheme theme = new ColorTheme();
        theme.setName(name);
        theme.setPrimaryColor(primaryColor);
        theme.setSecondaryColor(secondaryColor);
        theme.setButtonColor(buttonColor);

        if (colorThemeRepository.findByName(name).isEmpty()) {
            colorThemeRepository.save(theme);
        } else {
            throw new IllegalArgumentException("Motyw o tej nazwie już istnieje.");
        }
    }

    public List<ColorTheme> getAllThemes() {
        return colorThemeRepository.findAll();
    }

    public ColorTheme getThemeById(Long id) {
        return colorThemeRepository.findById(id).orElse(null);
    }
}
