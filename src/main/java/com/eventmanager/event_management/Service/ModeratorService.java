package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Enum.EventCategory;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeratorService {
    @Autowired
    private UserRepository userRepository;

    public void assignCategoryToModerator(Long moderatorId, String category) {
        // Znajdź moderatora
        User moderator = userRepository.findById(moderatorId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Przypisz kategorię z Enum do moderatora
        EventCategory selectedCategory = EventCategory.valueOf(category.toUpperCase());  // Pobieramy wartość z Enum

        // Zapisz kategorię przypisaną do moderatora
        moderator.setAssignedCategory(selectedCategory.name());  // Możesz zapisać jako String, jeśli chcesz

        userRepository.save(moderator);  // Zapisz zmiany w bazie
    }
}
