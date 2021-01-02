package it.multicoredev.scarpetbungeecordintegration.functions;

import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.*;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Collectors;

public class SendPacket {
    public static void apply(Expression expr) {
        expr.addLazyFunction("send_packet", -1, (c, t, lv) -> {
            if (lv.size() < 3)
                throw new InternalExpressionException("'recipe_data' requires at least one argument");

            Value playerValue = lv.get(0).evalValue(c);
            Value identifierValue = lv.get(1).evalValue(c);
            ListValue contentListValue = ListValue.wrap(lv.subList(2, lv.size()).stream().map((vv) -> vv.evalValue(c)).collect(Collectors.toList()));

            if (!(playerValue instanceof EntityValue))
                throw new InternalExpressionException("'send_packet' requires a player as the first argument");
            Entity entity = ((EntityValue) playerValue).getEntity();
            if (!(entity instanceof ServerPlayerEntity))
                throw new InternalExpressionException("'send_packet' requires a player as the first argument");
            if (!(identifierValue instanceof StringValue))
                throw new InternalExpressionException("'send_packet' requires a string as the second argument");

            // Create Packet Buffer
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            for(Value contentValue : contentListValue.getItems()) {
                if (contentValue instanceof NumericValue) {
                    buf.writeDouble(((NumericValue) contentValue).getDouble());
                } else if (contentValue instanceof NBTSerializableValue) {
                    buf.writeCompoundTag(((NBTSerializableValue) contentValue).getCompoundTag());
                } else {
                    buf.writeString(contentValue.getString());
                }
            }

            Identifier identifier = new Identifier(identifierValue.getString().toLowerCase());
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new CustomPayloadS2CPacket(identifier, buf));
            return LazyValue.TRUE;
        });
    }
}
