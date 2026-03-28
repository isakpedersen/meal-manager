package mealmanager;

import java.io.InputStream;

import javafx.scene.image.Image;

public class ImageService {
    private static final String PRODUCT_IMAGES = "/mealmanager/images/products/";
    private static final String PLACEHOLDER_URL = "/mealmanager/images/placeholder.jpg";
        
    public Image getImage(String ean) {
        String url = PRODUCT_IMAGES + ean + ".jpg";
        InputStream is = getClass().getResourceAsStream(url);
        if (is == null) {
            is = getClass().getResourceAsStream(PLACEHOLDER_URL);
        }
        return new Image(is);
    }
}
