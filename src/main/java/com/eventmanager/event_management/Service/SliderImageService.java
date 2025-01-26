package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Model.SliderImage;
import com.eventmanager.event_management.Repository.SliderImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SliderImageService {
    
    private final SliderImageRepository sliderImageRepository;

    public SliderImageService(SliderImageRepository sliderImageRepository) {
        this.sliderImageRepository = sliderImageRepository;
    }

    public void saveImage(MultipartFile image) throws IOException {
        byte[] imageBytes = image.getBytes();
        SliderImage sliderImage = new SliderImage();
        sliderImage.setInSlider(false);
        sliderImage.setImage(imageBytes);

        sliderImageRepository.save(sliderImage);
    }

    public List<SliderImage> getAllSliderImages() {
        return sliderImageRepository.findAll();
    }

    public Optional<SliderImage> findById(Long id) {
        return sliderImageRepository.findById(id);
    }

    public void updateSliderImages(List<Long> selectedIds) {
        List<SliderImage> allImages = sliderImageRepository.findAll();
        for (SliderImage image : allImages) {
            boolean isSelected = selectedIds.contains(image.getId());
            image.setInSlider(isSelected);
        }
        sliderImageRepository.saveAll(allImages);
    }

    public List<SliderImage> getSliderImages() {
        return sliderImageRepository.findByInSliderTrue();
    }
}
