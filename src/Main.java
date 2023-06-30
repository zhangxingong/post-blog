import client.*;
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
        if (StringUtils.isBlank(filePath)) {
            log.error("param blog's path is required!");
            return;
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            log.error("unfound this file!");
            return;
        }
        String authorization = PropertiesUtil.getProperty("authorization");
        if (StringUtils.isBlank(authorization)) {
            log.error("authorization is blank, cannot continue.");
            return;
        }
        String commandType = args[1];
        if (StringUtils.isBlank(commandType)) {
            commandType = "put";
        }
        PostBlog postBlog = new PostBlog(filePath, "Bearer " + authorization);
        postBlog.setStatus(0);
        postBlog.setMsg("init");
        if ("put".equalsIgnoreCase(commandType)) {
            postBlog.postBlog();
        } else {
            postBlog.removeBlog();
        }

    }
}