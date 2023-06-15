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

        //System.out.println("Hello world!");
//        String url = "https://api.github.com/user/repos";
        //String url = "https://api.github.com/repos/zhangxingong/blog/contents/content/about/index.md";
      //  String res = RestApi.doGet(url, "Bearer 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");
//        String res = "d:/home/blog/content/post/testtt.md";
//        File file = new File(res);
//        if (file.exists()) {
//           String name=  file.getName();
//            System.out.println("name = " + name);
//        }
       // System.out.println("index = " + index);
        System.out.println("res = " + postBlog.getMsg());
    }
}