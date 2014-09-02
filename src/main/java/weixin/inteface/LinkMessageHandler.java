package weixin.inteface;

import weixin.message.LinkMessage;
import weixin.message.MessageResponse;

public interface LinkMessageHandler {
	public void handle(LinkMessage msg, MessageResponse res);
}
