package weixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.utils.StringUtil;

public class Boot {

	private static final Logger logger = LoggerFactory.getLogger(Boot.class);
	
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
				res.reply("不明白您在表达神马:)");
				return;
			}
		});

		// 4. 图像消息处理
		Weixin.imageMsg((msg, res) -> {
			logger.debug("image:");
			logger.debug(msg.getPicUrl());
			logger.debug(msg.getMediaId());
			res.reply("Got image " + msg.getMediaId());
		});

		// 5. 语音消息处理
		Weixin.voiceMsg((msg, res) -> {
			logger.debug("voice:");
			logger.debug(msg.getFormat());
			logger.debug(msg.getMediaId());
			if (StringUtil.isNotEmpty(msg.getRecognition())) {
				logger.debug("语音识别：" + msg.getRecognition());
			}
			res.reply("Got voice:" + msg.getMediaId());
		});

		// 6. 视频消息处理
		Weixin.videoMsg((msg, res) -> {
			logger.debug("video:");
			logger.debug(msg.getMediaId());
			logger.debug(msg.getThumbMediaId());
			res.reply("Got video:" + msg.getMediaId());
		});

		// 7. 地理位置消息处理
		Weixin.locationMsg((msg, res) -> {
			logger.debug("location:");
			logger.debug(msg.getLocation_x().toPlainString());
			logger.debug(msg.getLocation_y().toPlainString());
			logger.debug(msg.getScale().toString());
			res.reply("Got location:" + msg.getLocation_x() + " " + msg.getLocation_y() + " " + msg.getScale());
		});

		// 8. 链接消息处理
		Weixin.linkMsg((msg, res) -> {
			logger.debug("link:");
			logger.debug(msg.getTitle());
			logger.debug(msg.getDescription());
			logger.debug(msg.getUrl());
			res.reply("Got url:" + msg.getUrl());
		});

		// 9. 关注/取消关注事件处理
		Weixin.subscribeEvent((event, res) -> {
			logger.debug("event:");
			logger.debug(event.getEvent());
			res.reply("欢迎光临");
		});

		// 10. 扫描二维码事件处理
		Weixin.qrEvent((event, res) -> {
			logger.debug("event:");
			logger.debug(event.getEvent());
			logger.debug(event.getEventKey());
			logger.debug(event.getTicket());
			res.reply("欢迎扫描");
		});

		// 11. 上报地理位置事件处理
		Weixin.locationEvent((event, res) -> {
			logger.debug("event:");
			logger.debug(event.getEvent());
			logger.debug(event.getLatitude().toPlainString());
			logger.debug(event.getLongitude().toPlainString());
			logger.debug(event.getPrecision().toPlainString());
			res.reply("欢迎签到");
		});

		// 12. 自定义菜单事件处理
		Weixin.menuEvent((event, res) -> {
			logger.debug("event:");
			logger.debug(event.getEvent());
			logger.debug(event.getEventKey());
			res.reply("欢迎点击菜单");
		});

		// 13. 菜单跳转链接事件处理
		Weixin.menuLinkEvent((event, res) -> {
			logger.debug("event:");
			logger.debug(event.getEvent());
			logger.debug(event.getEventKey());
			res.reply("欢迎菜单跳转链接");
		});

		Weixin.service();
	}

}
