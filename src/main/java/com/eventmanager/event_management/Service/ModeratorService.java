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
        User moderator = userRepository.findById(moderatorId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        EventCategory selectedCategory = EventCategory.valueOf(category.toUpperCase());
        moderator.setAssignedCategory(selectedCategory.name());

        userRepository.save(moderator);
    }
}
