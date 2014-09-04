package weixin;

import static spark.Spark.get;
import static spark.Spark.post;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;

import weixin.event.LocationEvent;
import weixin.event.MenuEvent;
import weixin.event.MenuLinkEvent;
import weixin.event.QrEvent;
import weixin.event.SubscribeEvent;
import weixin.inteface.ImageMessageHandler;
import weixin.inteface.LinkMessageHandler;
import weixin.inteface.LocationEventHandler;
import weixin.inteface.LocationMessageHandler;
import weixin.inteface.MenuEventHandler;
import weixin.inteface.MenuLinkEventHandler;
import weixin.inteface.QrEventHandler;
import weixin.inteface.SubscribeEventHandler;
import weixin.inteface.TextMessageHandler;
import weixin.inteface.VideoMessageHandler;
import weixin.inteface.VoiceMessageHandler;
import weixin.message.ImageMessage;
import weixin.message.LinkMessage;
import weixin.message.LocationMessage;
import weixin.message.MessageResponse;
import weixin.message.TextMessage;
import weixin.message.VideoMessage;
import weixin.message.VoiceMessage;
import weixin.utils.MessageUtil;
import weixin.utils.StringUtil;

public class Weixin {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Weixin.class);

	public static String token = "";
	public static String url = "/";

	private static TextMessageHandler textMessageHandler = null;
	private static ImageMessageHandler imageMessageHandler = null;
	private static VoiceMessageHandler voiceMessageHandler = null;
	private static VideoMessageHandler videoMessageHandler = null;
	private static LocationMessageHandler locationMessageHandler = null;
	private static LinkMessageHandler linkMessageHandler = null;
	private static SubscribeEventHandler subscribeEventHandler = null;
	private static QrEventHandler qrEventHandler = null;
	private static LocationEventHandler locationEventHandler = null;
	private static MenuEventHandler menuEventHandler = null;
	private static MenuLinkEventHandler menuLinkEventHandler = null;

	public static void configure(String url, String token, Integer port) {
		if (url != null) {
			Weixin.url = url;
		}
		if (token != null) {
			Weixin.token = token;
		}
		if (port != null) {
			spark.Spark.setPort(port);
		} else {
			spark.Spark.setPort(80);
		}
	}

	public static void checkSignature() {
		get("/", (request, response) -> {
			logger.debug("Incoming GET request.");
			String signature = request.queryParams("signature");
			String timestamp = request.queryParams("timestamp");
			String nonce = request.queryParams("nonce");
			String echostr = request.queryParams("echostr");
			logger.debug("signature:" + signature);
			logger.debug("timestamp:" + timestamp);
			logger.debug("nonce:" + nonce);
			logger.debug("echostr:" + echostr);

			if (checkSignature(signature, timestamp, nonce)) {
				logger.debug("Signature check success.");
				return echostr;
			} else {
				return null;
			}
		});
	}

	/**
	 * 验证URL有效性
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		if (StringUtil.isEmpty(signature) || StringUtil.isEmpty(timestamp) || StringUtil.isEmpty(nonce)) {
			return false;
		}
		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		String[] array = new String[] { token, timestamp, nonce };
		Arrays.sort(array);

		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		MessageDigest md;
		String digestStr;
		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest((array[0] + array[1] + array[2]).getBytes());
			digestStr = bytesToHexStr(digest);
		} catch (NoSuchAlgorithmException e) {
			return false;
		}

		// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		if (digestStr.equalsIgnoreCase(signature)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Register text message handler.
	 * 
	 * @param handler
	 */
	public static void textMsg(TextMessageHandler handler) {
		textMessageHandler = handler;
	}

	/**
	 * Register image message handler.
	 * 
	 * @param handler
	 */
	public static void imageMsg(ImageMessageHandler handler) {
		imageMessageHandler = handler;
	}

	/**
	 * Register voice message handler.
	 * 
	 * @param handler
	 */
	public static void voiceMsg(VoiceMessageHandler handler) {
		voiceMessageHandler = handler;
	}

	/**
	 * Register video message handler.
	 * 
	 * @param handler
	 */
	public static void videoMsg(VideoMessageHandler handler) {
		videoMessageHandler = handler;
	}

	/**
	 * Register location message handler.
	 * 
	 * @param handler
	 */
	public static void locationMsg(LocationMessageHandler handler) {
		locationMessageHandler = handler;
	}

	/**
	 * Register link message handler.
	 * 
	 * @param handler
	 */
	public static void linkMsg(LinkMessageHandler handler) {
		linkMessageHandler = handler;
	}

	/**
	 * Register subscribe event handler.
	 * 
	 * @param handler
	 */
	public static void subscribeEvent(SubscribeEventHandler handler) {
		subscribeEventHandler = handler;
	}

	/**
	 * Register QR event handler.
	 * 
	 * @param handler
	 */
	public static void qrEvent(QrEventHandler handler) {
		qrEventHandler = handler;
	}

	/**
	 * Register location event handler.
	 * 
	 * @param handler
	 */
	public static void locationEvent(LocationEventHandler handler) {
		locationEventHandler = handler;
	}

	/**
	 * Register menu event handler.
	 * 
	 * @param handler
	 */
	public static void menuEvent(MenuEventHandler handler) {
		menuEventHandler = handler;
	}

	/**
	 * Register menu link event handler.
	 * 
	 * @param handler
	 */
	public static void menuLinkEvent(MenuLinkEventHandler handler) {
		menuLinkEventHandler = handler;
	}

	/**
	 * Start service.
	 */
	public static void service() {
		logger.debug("Start servicing messages.");
		post(url, (req, res) -> {
			logger.debug("Incoming POST request.");
			String xmlStr = req.body();
			logger.debug("text msg:\n" + xmlStr);
			Map<String, String> msg = MessageUtil.xmlToMap(xmlStr);
			switch (msg.get("MsgType")) {
			case "text": {
				if (textMessageHandler != null) {
					TextMessage message = getTextMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						textMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "image": {
				if (imageMessageHandler != null) {
					ImageMessage message = getImageMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						imageMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "voice": {
				if (voiceMessageHandler != null) {
					VoiceMessage message = getVoiceMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						voiceMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "video": {
				if (videoMessageHandler != null) {
					VideoMessage message = getVideoMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						videoMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "location": {
				if (locationMessageHandler != null) {
					LocationMessage message = getLocationMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						locationMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "link": {
				if (linkMessageHandler != null) {
					LinkMessage message = getLinkMessage(msg);
					if (message != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(message.getFromUserName());
						response.setFromUserName(message.getToUserName());
						response.setCreateTime(message.getCreateTime());
						response.setMsgType(message.getMsgType());

						linkMessageHandler.handle(message, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
				}
				break;
			}
			case "event": {
				switch (msg.get("Event")) {
				case "subscribe":
				case "unsubscribe": {
					if (StringUtil.isEmpty(msg.get("EventKey"))) { /* Subscribe/Unsubscribe */
						SubscribeEvent event = getSubscribeEvent(msg);
						if (event != null) {
							MessageResponse response = new MessageResponse();
							response.setToUserName(event.getFromUserName());
							response.setFromUserName(event.getToUserName());
							response.setCreateTime(event.getCreateTime());
							response.setMsgType(event.getMsgType());

							logger.debug("handle");
							subscribeEventHandler.handle(event, response);
							String xmlResponse = response.toXml();
							logger.debug("resp msg:\n" + xmlResponse);
							return xmlResponse;
						}
					} else if (msg.get("Ticket") != null) { /* QR */
						QrEvent event = getQrEvent(msg);
						if (event != null) {
							MessageResponse response = new MessageResponse();
							response.setToUserName(event.getFromUserName());
							response.setFromUserName(event.getToUserName());
							response.setCreateTime(event.getCreateTime());
							response.setMsgType(event.getMsgType());

							qrEventHandler.handle(event, response);
							String xmlResponse = response.toXml();
							logger.debug("resp msg:\n" + xmlResponse);
							return xmlResponse;
						}
					}
					break;
				}
				case "SCAN": {
					QrEvent event = getQrEvent(msg);
					if (event != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(event.getFromUserName());
						response.setFromUserName(event.getToUserName());
						response.setCreateTime(event.getCreateTime());
						response.setMsgType(event.getMsgType());

						qrEventHandler.handle(event, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
					break;
				}
				case "LOCATION": {
					LocationEvent event = getLocationEvent(msg);
					if (event != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(event.getFromUserName());
						response.setFromUserName(event.getToUserName());
						response.setCreateTime(event.getCreateTime());
						response.setMsgType(event.getMsgType());

						locationEventHandler.handle(event, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
					break;
				}
				case "CLICK": {
					MenuEvent event = getMenuEvent(msg);
					if (event != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(event.getFromUserName());
						response.setFromUserName(event.getToUserName());
						response.setCreateTime(event.getCreateTime());
						response.setMsgType(event.getMsgType());

						menuEventHandler.handle(event, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
					break;
				}
				case "VIEW": {
					MenuLinkEvent event = getMenuLinkEvent(msg);
					if (event != null) {
						MessageResponse response = new MessageResponse();
						response.setToUserName(event.getFromUserName());
						response.setFromUserName(event.getToUserName());
						response.setCreateTime(event.getCreateTime());
						response.setMsgType(event.getMsgType());

						menuLinkEventHandler.handle(event, response);
						String xmlResponse = response.toXml();
						logger.debug("resp msg:\n" + xmlResponse);
						return xmlResponse;
					}
					break;
				}
				}
			}
			}

			return "";
		});
	}

	private static TextMessage getTextMessage(Map<String, String> map) {
		TextMessage msg = new TextMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("text");
		msg.setContent(map.get("Content"));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static ImageMessage getImageMessage(Map<String, String> map) {
		ImageMessage msg = new ImageMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("image");
		msg.setPicUrl(map.get("PicUrl"));
		msg.setMediaId(map.get("MediaId"));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static VoiceMessage getVoiceMessage(Map<String, String> map) {
		VoiceMessage msg = new VoiceMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("image");
		msg.setFormat(map.get("Format"));
		msg.setMediaId(map.get("MediaId"));
		msg.setRecognition(map.get("Recognition"));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static VideoMessage getVideoMessage(Map<String, String> map) {
		VideoMessage msg = new VideoMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("image");
		msg.setMediaId(map.get("MediaId"));
		msg.setThumbMediaId(map.get("ThumbMediaId"));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static LocationMessage getLocationMessage(Map<String, String> map) {
		LocationMessage msg = new LocationMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("image");
		msg.setLocation_x(new BigDecimal(map.get("Location_X")));
		msg.setLocation_y(new BigDecimal(map.get("Location_Y")));
		msg.setScale(new Integer(map.get("Scale")));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static LinkMessage getLinkMessage(Map<String, String> map) {
		LinkMessage msg = new LinkMessage();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("image");
		msg.setTitle(map.get("Title"));
		msg.setDescription(map.get("Description"));
		msg.setUrl(map.get("Url"));
		msg.setMsgId(Long.valueOf(map.get("MsgId")));
		return msg;
	}

	private static SubscribeEvent getSubscribeEvent(Map<String, String> map) {
		SubscribeEvent msg = new SubscribeEvent();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("event");
		msg.setEvent(map.get("Event"));
		return msg;
	}

	private static QrEvent getQrEvent(Map<String, String> map) {
		QrEvent msg = new QrEvent();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("event");
		msg.setEvent(map.get("Event"));
		msg.setEventKey(map.get("EventKey"));
		msg.setTicket(map.get("Ticket"));
		return msg;
	}

	private static LocationEvent getLocationEvent(Map<String, String> map) {
		LocationEvent msg = new LocationEvent();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("event");
		msg.setEvent(map.get("Event"));
		msg.setLatitude(new BigDecimal(map.get("Latitude")));
		msg.setLongitude(new BigDecimal(map.get("Longitude")));
		msg.setPrecision(new BigDecimal(map.get("Precision")));
		return msg;
	}

	private static MenuEvent getMenuEvent(Map<String, String> map) {
		MenuEvent msg = new MenuEvent();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("event");
		msg.setEvent(map.get("Event"));
		msg.setEventKey(map.get("EventKey"));
		return msg;
	}

	private static MenuLinkEvent getMenuLinkEvent(Map<String, String> map) {
		MenuLinkEvent msg = new MenuLinkEvent();
		msg.setToUserName(map.get("ToUserName"));
		msg.setFromUserName(map.get("FromUserName"));
		msg.setCreateTime(Integer.valueOf(map.get("CreateTime")));
		msg.setMsgType("event");
		msg.setEvent(map.get("Event"));
		msg.setEventKey(map.get("EventKey"));
		return msg;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToHexStr(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

}
