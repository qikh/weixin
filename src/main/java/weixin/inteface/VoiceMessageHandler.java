package weixin.inteface;

import weixin.message.MessageResponse;
import weixin.message.VoiceMessage;

public interface VoiceMessageHandler {
	public void handle(VoiceMessage msg, MessageResponse res);
}
