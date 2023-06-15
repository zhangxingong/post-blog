import client.FileUtils;
import client.PostBlog;
import client.PropertiesUtil;
import client.RestApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ResourceBundle;

/**
 * Created by ${USER} on ${DATE}.
 */
public class Main {
    private static final Log log = LogFactory.getLog(Main.class);
    public static void main(String[] args) {
        String filePath = args[0];
        String authorization = PropertiesUtil.getProperty("authorization");
        PostBlog postBlog = new PostBlog(filePath,
                "Bearer " + authorization);
        postBlog.setStatus(0);
        postBlog.setMsg("init");
        postBlog.postBlog();

    }
}