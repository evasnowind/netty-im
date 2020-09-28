package com.prayerlaputa.im.study.protocol;

import com.prayerlaputa.im.study.protocol.command.Command;
import com.prayerlaputa.im.study.protocol.request.CreateGroupRequestPacket;
import com.prayerlaputa.im.study.protocol.request.GroupMessageRequestPacket;
import com.prayerlaputa.im.study.protocol.request.JoinGroupRequestPacket;
import com.prayerlaputa.im.study.protocol.request.ListGroupMembersRequestPacket;
import com.prayerlaputa.im.study.protocol.request.LoginRequestPacket;
import com.prayerlaputa.im.study.protocol.request.LogoutRequestPacket;
import com.prayerlaputa.im.study.protocol.request.MessageRequestPacket;
import com.prayerlaputa.im.study.protocol.request.QuitGroupRequestPacket;
import com.prayerlaputa.im.study.protocol.response.CreateGroupResponsePacket;
import com.prayerlaputa.im.study.protocol.response.GroupMessageResponsePacket;
import com.prayerlaputa.im.study.protocol.response.JoinGroupResponsePacket;
import com.prayerlaputa.im.study.protocol.response.ListGroupMembersResponsePacket;
import com.prayerlaputa.im.study.protocol.response.LoginResponsePacket;
import com.prayerlaputa.im.study.protocol.response.LogoutResponsePacket;
import com.prayerlaputa.im.study.protocol.response.MessageResponsePacket;
import com.prayerlaputa.im.study.protocol.response.QuitGroupResponsePacket;
import com.prayerlaputa.im.study.serialize.Serializer;
import com.prayerlaputa.im.study.serialize.impl.JSONSerializer;
import com.prayerlaputa.im.study.serialize.impl.KryoSerializer;
import com.prayerlaputa.im.study.serialize.impl.ProtoStuffSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenglong.yu
 * created on 2020/9/17
 */
public class PacketCodeC {
    public static final int MAGIC_NUMBER = 0x12345678;
    private static final PacketCodeC instance = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;

    public static PacketCodeC getInstance() {
        return instance;
    }

    private PacketCodeC() {
        packetTypeMap = new HashMap<>();
        //登录请求
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        //文本消息
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        //登出消息包
        packetTypeMap.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        //创建群组消息
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        //列出群组成员消息
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        //加入群组消息
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        //退出群组消息
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        //群组消息
        packetTypeMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer jsonSerializer = new JSONSerializer();
        serializerMap.put(jsonSerializer.getSerializerAlgorithm(), jsonSerializer);
        Serializer protoStuffSerializer = new ProtoStuffSerializer();
        serializerMap.put(protoStuffSerializer.getSerializerAlgorithm(), protoStuffSerializer);
        Serializer kryoSerializer = new KryoSerializer();
        serializerMap.put(kryoSerializer.getSerializerAlgorithm(), kryoSerializer);
    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // 2. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
