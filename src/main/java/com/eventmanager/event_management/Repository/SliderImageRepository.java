package com.eventmanager.event_management.Repository;


import com.eventmanager.event_management.Model.SliderImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SliderImageRepository extends JpaRepository<SliderImage, Long> {

    @Query("SELECT s FROM SliderImage s WHERE s.inSlider = true ORDER BY s.orderIndex ASC")
    List<SliderImage> findByInSliderTrue();

}
