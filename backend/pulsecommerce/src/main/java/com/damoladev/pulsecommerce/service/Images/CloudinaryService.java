package com.damoladev.pulsecommerce.service.Images;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService implements FileStorageService {
    private final Cloudinary cloudinary;

    @Override
    public Map uploadFile(MultipartFile file,String filePath,String filename) throws IOException {
        String publicId = filename.substring(0,filename.lastIndexOf('.'));
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder",filePath,
                "public_id",publicId,
                "overwrite",true,
                "resource_type","auto"
        ));
    }

    @Override
    public Map deleteFile(String key, String resourceType) throws IOException {
        return cloudinary.uploader().destroy(key, ObjectUtils.asMap(
                "resource_type", "image"
        ));
    }


    public String getUrl(String publicId) {
        return cloudinary.url().secure(true).generate(publicId);
    }

    public String getThumbnailUrl(String publicId, int width, int height) {
        return cloudinary.url()
                .transformation(new Transformation().width(width).height(height).crop("fill"))
                .generate(publicId);
    }
}
