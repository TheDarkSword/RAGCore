package it.revarmygaming.commonapi.socket.packet;

import it.revarmygaming.commonapi.socket.Buffer;
import it.revarmygaming.commonapi.socket.handler.NetworkHandler;
import it.revarmygaming.commonapi.socket.handler.ProcessPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class Packets {

    private static HashMap<Integer, Class> packets = new HashMap<>();
    private static NetworkHandler networkHandler;
    private static HashMap<Class, Method> process = new HashMap<>();

    static {
        packets.put(0x0, RawPacket.class);
    }

    public static void registerPacket(int id, Class packet){
        packets.put(id, packet);
    }

    public static void setNetworkHandler(NetworkHandler networkHandler){
        Packets.networkHandler = networkHandler;

        process.clear();
        for(Method method : networkHandler.getClass().getMethods()){
            if(method.isAnnotationPresent(ProcessPacket.class)){
                if(method.getParameterCount() == 2 && method.getParameterTypes()[0].getInterfaces().length > 0 &&
                        Arrays.asList(method.getParameterTypes()[0].getInterfaces()).contains(Packet.class) && method.getParameterTypes()[1] == Buffer.class){
                    process.put(method.getParameterTypes()[0], method);
                }
            }
        }
    }

    public static Class getPacket(int id){
        return packets.get(id);
    }

    public static void callProcess(Packet packet, Buffer buffer) {
        try{
            process.get(packet.getClass()).invoke(networkHandler, packet, buffer);
        } catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
    }
}
