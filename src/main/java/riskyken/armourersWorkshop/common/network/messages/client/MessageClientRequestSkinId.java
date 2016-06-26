package riskyken.armourersWorkshop.common.network.messages.client;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import riskyken.armourersWorkshop.common.skin.SkinDataCache;

/**
 * Sent from the client to the server to request an
 * ID for a skin file name.
 * @author RiskyKen
 *
 */
public class MessageClientRequestSkinId implements IMessage, IMessageHandler<MessageClientRequestSkinId, IMessage> {

    private String fileName;
    
    public MessageClientRequestSkinId() {
    }
    
    public MessageClientRequestSkinId(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, fileName);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        fileName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public IMessage onMessage(MessageClientRequestSkinId message, MessageContext ctx) {
        SkinDataCache.INSTANCE.clientRequestSkinId(message.fileName, ctx.getServerHandler().playerEntity);
        return null;
    }
}
