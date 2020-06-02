package com.zhy.vo;

public class RestfulResultVO {

    private String rt_message = "Success";
    private String rt_code;
    private Object data;        // 返回数据
    private int cntPage;        // page数
    private long cntData;        // 返回数据总数

    public static RestfulResultVO success(String rt_code, Object data){
        RestfulResultVO restfulResultVO = new RestfulResultVO();
        restfulResultVO.setRt_message("Success");
        restfulResultVO.setRt_code(rt_code);
        restfulResultVO.setData(data);
        return restfulResultVO;
    }

    public static RestfulResultVO fail(String rt_code, String rt_message, Object data){
        RestfulResultVO restfulResultVO = new RestfulResultVO();
        restfulResultVO.setRt_message(rt_message);
        restfulResultVO.setRt_code(rt_code);
        restfulResultVO.setData(data);
        return restfulResultVO;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCntPage() {
        return cntPage;
    }

    public void setCntPage(int cntPage) {
        this.cntPage = cntPage;
    }

    public long getCntData() {
        return cntData;
    }

    public void setCntData(long cntData) {
        this.cntData = cntData;
    }

    public String getRt_message() {
        return rt_message;
    }

    public void setRt_message(String rt_message) {
        this.rt_message = rt_message;
    }

    public String getRt_code() {
        return rt_code;
    }

    public void setRt_code(String rt_code) {
        this.rt_code = rt_code;
    }

    @Override
    public String toString() {
        return "RestfulResultVO{" +
                ", rt_message='" + rt_message + '\'' +
                ", rt_code='" + rt_code + '\'' +
                ", data=" + data +
                ", cntPage=" + cntPage +
                ", cntData=" + cntData +
                '}';
    }
}
