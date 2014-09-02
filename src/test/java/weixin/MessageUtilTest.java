package weixin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import weixin.message.MessageResponse;
import weixin.message.TextMessage;
import weixin.utils.MessageUtil;

public class MessageUtilTest {

	@Test
	public void testSignature() {
		String signature = "DB70414D5F035883146F53021065C6895D2A0AE2";
		String timestamp = "1409562117";
		String nonce = "103815410";
		assertEquals(Weixin.checkSignature(signature, timestamp, nonce),
				Boolean.TRUE);
	}

	@Test
	public void testxmlToTextMessage() {
		String xmlStr = "<xml><ToUserName><![CDATA[gh_46db7d8280e3]]></ToUserName>\n"
				+ "<FromUserName><![CDATA[oXpsnuCkBalHTJU_VtKaH_U0vHLo]]></FromUserName>\n"
				+ "<CreateTime>1409563190</CreateTime>\n"
				+ "<MsgType><![CDATA[text]]></MsgType>\n"
				+ "<Content><![CDATA[hello]]></Content>\n"
				+ "<MsgId>6054027802895798666</MsgId>\n" + "</xml>\n";
		TextMessage req = MessageUtil.xmlToTextMessage(xmlStr);
		assertEquals(req.getToUserName(), "gh_46db7d8280e3");
		assertEquals(req.getFromUserName(), "oXpsnuCkBalHTJU_VtKaH_U0vHLo");
		assertEquals(req.getCreateTime(), Integer.valueOf(1409563190));
		assertEquals(req.getMsgType(), "text");
		assertEquals(req.getContent(), "hello");
		assertEquals(req.getMsgId(), Long.valueOf(6054027802895798666l));
	}

	@Test
	public void testTextResponseToXml() {
		String expectedXml = "<xml>"
				+ "<ToUserName><![CDATA[toUser]]></ToUserName>"
				+ "<FromUserName><![CDATA[fromUser]]></FromUserName>"
				+ "<CreateTime>12345678</CreateTime>"
				+ "<MsgType><![CDATA[text]]></MsgType>"
				+ "<Content><![CDATA[content]]></Content>" + "</xml>";
		MessageResponse resp = new MessageResponse();
		resp.setToUserName("toUser");
		resp.setFromUserName("fromUser");
		resp.setCreateTime(12345678);
		resp.setMsgType("text");
		resp.setContent("content");
		String xmlStr = resp.toXml();
		assertEquals(xmlStr, expectedXml);
	}

}
