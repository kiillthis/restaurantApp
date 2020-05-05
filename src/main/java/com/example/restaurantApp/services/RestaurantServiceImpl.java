package com.example.restaurantApp.services;


import com.example.restaurantApp.bucket.BucketName;
import com.example.restaurantApp.domain.Restaurant;
import com.example.restaurantApp.filestore.FileStore;
import com.example.restaurantApp.profile.UserProfile;
import com.example.restaurantApp.repository.RestaurantRepository;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import java.io.IOException;
import java.util.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private FileStore fileStore;

    @Override
    public List<Restaurant> getAllRestaurants() {
        return (List<Restaurant>) restaurantRepository.findAll();
    }

    @Override
    public int addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant).getId();
    }

    @Override
    public int deleteRestaurant(int id) {
        restaurantRepository.deleteById(id);
        return id;
    }

    @Override
    public void uploadRestaurantImage(int restaurantId, MultipartFile file) {
        isFileEmpty(file);

        isImage(file);

        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        Map<String, String> metadata = extractMetadata(file);

        String path = String.format("%s/%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), "Restaurants",restaurant.getId());
        String filename = String.format("%s", file.getOriginalFilename());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            String link = "https://kray-bucket.s3.eu-central-1.amazonaws.com/Restaurants/" + restaurantId + "/" + filename;
            restaurantRepository.updateRestaurantLinkById(restaurantId, link); //setUserProfileImageLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void updateLink(int restaurantId, String link) {
        restaurantRepository.updateRestaurantLinkById(restaurantId, link);
    }


    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private Restaurant getRestaurantOrThrow(int restaurantId) {
        return getAllRestaurants()
                .stream()
                .filter(restaurant -> restaurant.getId() == restaurantId )
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Restaurant %s not found", restaurantId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

}
