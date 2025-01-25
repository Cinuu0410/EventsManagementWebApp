package com.eventmanager.event_management.Repository;


import com.eventmanager.event_management.Model.SliderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SliderImageRepository extends JpaRepository<SliderImage, Long> {
    List<SliderImage> findByInSliderTrue();
}
