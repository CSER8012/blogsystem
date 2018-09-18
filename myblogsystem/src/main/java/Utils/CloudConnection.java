package Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudConnection {
    private static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dahfv80os", "api_key", "892623573547898",
            "api_secret", "p-yHFMGb_oevIfCNGGVdCG1A0MU"));

    public static String uploadToCloud(String filePath) throws IOException {
        if(filePath == null || filePath.contains("http") || filePath.contains("www"))
            return filePath;
        File file = new File(filePath);
        if(file == null || file.length() == 0) {
            return null;
        }

        Map result = cloudinary.uploader().upload(file,ObjectUtils.emptyMap());
        return (String) result.get("url");
    }
}
