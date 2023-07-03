package client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * Created by xgzhang on 2023/6/14.
 */
public class PostBlog {
    private static final Log log = LogFactory.getLog(PostBlog.class);

    private String filePath;
    private int status;
    private String msg;
    private String token;
    private File file;

    public PostBlog(String filePath, String token) {
        this.filePath = filePath;
        this.token = token;
    }

    public void postBlog() {
        if (StringUtils.isBlank(filePath)) {
            setMsg("don't exist a blog file. Please check.");
            log.error(getMsg());
            setStatus(-1);
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            setMsg("don't exist a blog file. Please check.");
            log.error(getMsg());
            setStatus(-1);
            return;
        }

        setFile(file);

        JSONObject resObj = getPostContent();

        putPostContent(resObj);
    }

    public void removeBlog() {
        if (StringUtils.isBlank(filePath)) {
            setMsg("don't exist a blog file. Please check.");
            log.error(getMsg());
            setStatus(-1);
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            setMsg("don't exist a blog file. Please check.");
            log.error(getMsg());
            setStatus(-1);
            return;
        }

        setFile(file);

        JSONObject resObj = getPostContent();

        deleteContent(resObj);
    }

    private void putPostContent(JSONObject resObj) {
        String sha = "";
        String message = "add post";

        if (resObj != null && StringUtils.isBlank(resObj.getString("message"))) {
            sha = resObj.getString("sha");
            message = "update post";
        }

        String name = getFile().getName();
        String url = getPostPath(name);
        String content = Base64Utils.encryptToBase64(getFilePath());
        message += (" " + name);

        JSONObject param = new JSONObject();
        if (StringUtils.isValid(sha)) {
            param.put("sha", sha);
        }
        param.put("message", message);
        log.info("jsonParam = " + param);
        param.put("content", content);

        String jsonParam = JSON.toJSONString(param);

        JSONObject res = JSON.parseObject(RestApi.doPut(url, getToken(), jsonParam));
        log.info("res = " + res);
        setStatus(1);
        setMsg("success");
    }

    private void deleteContent(JSONObject resObj) {
        String sha = "";
        JSONObject param = new JSONObject();
        String message = "delete post";

        if (resObj != null && StringUtils.isBlank(resObj.getString("message"))) {
            sha = resObj.getString("sha");
        }

        String name = getFile().getName();
        String url = getPostPath(name);

        message += (" " + name);
        if (StringUtils.isValid(sha)) {
            param.put("sha", sha);
        }
        param.put("message", message);

        String jsonParam = JSON.toJSONString(param);
        //log.info("jsonParam = " + jsonParam);

        JSONObject res = JSON.parseObject(RestApi.doDelete(url, getToken(), jsonParam));
        log.info("res = " + res);
        setStatus(1);
        setMsg("success");

    }

    private String getPostPath(String name) {
        String path = "";
        String gh_path;
        if (FileUtils.isImageFile(name)) {
            gh_path = PropertiesUtil.getProperty("gh_image_path");
            path = "https://api.github.com/repos/zhangxingong/blog/contents/" + gh_path + name;
        } else {
            gh_path = PropertiesUtil.getProperty("gh_content_path");
            path = "https://api.github.com/repos/zhangxingong/blog/contents/" + gh_path + name;
        }

        log.info("postPath = " + path);
        return path;
    }

    private JSONObject getPostContent() {
        String name = getFile().getName();
        String url = getPostPath(name);
        String res = RestApi.doGet(url, getToken());
        //log.info("get Content=" + res);
        JSONObject jsonRes = JSON.parseObject(res);
        return jsonRes;
    }

    public String getRemoteContent() {
        return null;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
