微信机器人小站
=============

基于Java Micro Web框架[Spark](http://www.sparkjava.com/)开发的微信公众平台机器人小站，特点是简单。

**代码就是一切：**

```java
public class Boot {

    public static void main(String[] args) {
        // 1. 配置服务器URL，Token和端口（调试用途）
        Weixin.configure("/", "myweixin", 80);

        // 2. 消息初始验证
        Weixin.checkSignature();

        // 3. 文本消息处理
        Weixin.textMsg((msg, res) -> {
            switch (msg.getContent()) {
            case "hello": {
                res.reply("world");
                break;
            }
            case "hehe": {
                res.reply("ee");
                break;
            }
            default:
                return;
            }
        });

        // 4. 图像消息处理
        Weixin.imageMsg((msg, res) -> {
            System.out.println("image:");
            System.out.println(msg.getPicUrl());
            System.out.println(msg.getMediaId());
            res.reply("Got image " + msg.getMediaId());
        });

        // 5. 语音消息处理
        Weixin.voiceMsg((msg, res) -> {
            System.out.println("voice:");
            System.out.println(msg.getFormat());
            System.out.println(msg.getMediaId());
            res.reply("Got voice:" + msg.getMediaId());
        });

        // 6. 视频消息处理
        Weixin.videoMsg((msg, res) -> {
            System.out.println("video:");
            System.out.println(msg.getMediaId());
            System.out.println(msg.getThumbMediaId());
            res.reply("Got video:" + msg.getMediaId());
        });

        // 7. 地理位置消息处理
        Weixin.locationMsg((msg, res) -> {
            System.out.println("location:");
            System.out.println(msg.getLocation_x());
            System.out.println(msg.getLocation_y());
            System.out.println(msg.getScale());
            res.reply("Got location:" + msg.getLocation_x() + " " + msg.getLocation_y() + " " + msg.getScale());
        });

        // 8. 链接消息处理
        Weixin.linkMsg((msg, res) -> {
            System.out.println("link:");
            System.out.println(msg.getTitle());
            System.out.println(msg.getDescription());
            System.out.println(msg.getUrl());
            res.reply("Got url:" + msg.getUrl());
        });

        Weixin.service();
    }

}
```
