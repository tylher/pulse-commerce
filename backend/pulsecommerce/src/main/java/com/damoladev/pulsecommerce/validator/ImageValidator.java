package com.damoladev.pulsecommerce.validator;

import com.damoladev.pulsecommerce.exception.ImageValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
public class ImageValidator {
    private static final long MAX_FILE_SIZE = 5*1024*1024;
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );
    private static final Map<String, byte[]> MAGIC_BYTES = Map.of(
            "image/jpeg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
            "image/png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47},
            "image/webp", new byte[]{0x52, 0x49, 0x46, 0x46} // "RIFF"
    );

    public void validate(MultipartFile file) throws ImageValidationException {
        validateSize(file);
        validateMimeType(file);
        validateMagicBytes(file);
    }

    private void validateSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageValidationException(
                    "File too large. Maximum allowed size is 5 MB, got: "
                            + (file.getSize() / (1024 * 1024)) + " MB"
            );
        }
    }


    private void validateMimeType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType)) {
            throw new ImageValidationException(
                    "Invalid file type: " + contentType
                            + ". Allowed: JPEG, PNG, WEBP"
            );
        }
    }

    private void validateMagicBytes(MultipartFile file) {
        try {
            byte[] header = new byte[8];
            file.getInputStream().read(header);

            boolean valid = MAGIC_BYTES.values().stream()
                    .anyMatch(magic -> startsWith(header, magic));

            if (!valid) {
                throw new ImageValidationException(
                        "File content does not match a valid image format. "
                                + "The file may be corrupted or disguised."
                );
            }
        } catch (IOException e) {
            throw new ImageValidationException("Could not read file for validation");
        }
    }

    private boolean startsWith(byte[] data, byte[] prefix) {
        if (data.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) return false;
        }
        return true;
    }

}
