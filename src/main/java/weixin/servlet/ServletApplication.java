package weixin.servlet;

import spark.servlet.SparkApplication;
import weixin.Weixin;

public class ServletApplication implements SparkApplication {

    @Override
    public void init() {
        Weixin.checkSignature();
    }

}