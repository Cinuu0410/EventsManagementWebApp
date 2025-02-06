package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Model.SliderImage;
import com.eventmanager.event_management.Repository.SliderImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        int maxWidth = 800;
        int maxHeight = 600;

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image.getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(byteArrayInputStream)
                .size(maxWidth, maxHeight)
                .keepAspectRatio(true)
                .toOutputStream(byteArrayOutputStream);

        byte[] scaledImageBytes = byteArrayOutputStream.toByteArray();

        SliderImage sliderImage = new SliderImage();
        sliderImage.setInSlider(false);
        sliderImage.setImage(scaledImageBytes);

        sliderImageRepository.save(sliderImage);
    }

    public void saveImageSecond(SliderImage image) {
        sliderImageRepository.save(image);
    }

    public List<SliderImage> getAllSliderImages() {
        return sliderImageRepository.findAll();
    }

    public Optional<SliderImage> findById(Long id) {
        return sliderImageRepository.findById(id);
    }

    public SliderImage getImageById(Long id) {
        return sliderImageRepository.findById(id).orElse(null);
    }

    public void updateSliderImages(List<Long> selectedIds, List<Long> orderedIds) {
        List<SliderImage> allImages = sliderImageRepository.findAll();

        for (SliderImage image : allImages) {
            image.setInSlider(selectedIds != null && selectedIds.contains(image.getId()));
            image.setOrderIndex(orderedIds.indexOf(image.getId()));
        }

        sliderImageRepository.saveAll(allImages);
    }

    public List<SliderImage> getSliderImages() {
        return sliderImageRepository.findByInSliderTrue();
    }
}
