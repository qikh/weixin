package weixin.inteface;

import weixin.message.MessageResponse;
import weixin.message.TextMessage;

public interface TextMessageHandler {
	public void handle(TextMessage msg, MessageResponse res);
}
