package it.revarmygaming.commonapi.socket;

import it.revarmygaming.commonapi.socket.packet.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Buffer {

    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;

    public Buffer(Socket socket, DataInputStream reader, DataOutputStream writer){
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public Integer readInt(){
        try {
            return reader.readInt();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long readLong(){
        try {
            return reader.readLong();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Float readFloat(){
        try {
            return reader.readFloat();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double readDouble(){
        try {
            return reader.readDouble();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Short readShort(){
        try {
            return reader.readShort();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readUnsignedShort(){
        try {
            return reader.readUnsignedShort();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Byte readByte(){
        try {
            return reader.readByte();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int readUnsignedByte(){
        try {
            return reader.readUnsignedByte();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public byte[] readBytes(int length){
        try {
            byte[] buf = new byte[length];
            reader.read(buf);
            return buf;
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String readString(int length){
        return new String(readBytes(length));
    }

    public Boolean readBoolean(){
        try {
            return reader.readBoolean();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeInt(int data){
        try {
            writer.writeInt(data);
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLong(long data){
        try {
            writer.writeLong(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFloat(float data){
        try {
            writer.writeFloat(data);
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDouble(double data){
        try {
            writer.writeDouble(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeShort(short data){
        try {
            writer.writeShort(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeByte(byte data){
        try {
            writer.writeByte(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBytes(byte[] data){
        try {
            writer.write(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeBoolean(boolean data){
        try {
            writer.writeBoolean(data);
            flush();
        } catch (SocketException ignored){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePacket(Packet packet){
        packet.encode(this);
        flush();
    }

    public void close() throws IOException {
        reader.close();
        writer.close();;
    }

    private void flush(){
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getReader() {
        return reader;
    }

    public DataOutputStream getWriter() {
        return writer;
    }
}
