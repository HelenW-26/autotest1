package newcrm.utils.api;

import cn.hutool.json.JSONObject;

import java.io.Serializable;

public class HttpClientResult implements Serializable {

    //响应状态码
    private int code;
    //响应数据
    private String content;

    private JSONObject jsonObject;


    public HttpClientResult(int code , String content){
        this.code = code;
        this.content = content;
        this.jsonObject = new JSONObject(content);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
