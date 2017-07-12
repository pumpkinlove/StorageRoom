package com.miaxis.storageroom.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

public abstract class BaseComm {

	protected static final int ERROR_DBACCESS = 5003;
	protected static final int ERROR_WORKERCODE_EXIST = 6015;

	public String message;

	private Vector<Byte> ustobytes(short val)
	{
		Vector<Byte> v = new Vector<Byte>();
		v.clear();

		int n = val;
		if (n < 0)
		{
			n += 0x10000;
		}
		Byte b;
		b = (byte)(n%0x100);
		v.add(b);
		b = (byte)(n/0x100);
		v.add(b);
		return v;
	}

	protected short bytestous(byte[] bytes)
	{
		int m = bytes[0];
		int n = bytes[1];

		if (m < 0)
		{
			m += 0x100;
		}
		if (n < 0)
		{
			n += 0x100;
		}

		return (short)(n*0x100+m);
	}

	protected Vector<Byte> ultobytes(int val)
	{
		Vector<Byte> v = new Vector<Byte>();
		v.clear();

		Byte b = (byte)(val%0x100);
		v.add(b);
		b = (byte)((val%0x10000)/0x100);
		v.add(b);
		b = (byte)((val/0x10000)%0x100);
		v.add(b);
		b = (byte)((val/0x10000)/0x100);
		v.add(b);
		return v;
	}

	protected int bytestoul(byte[] bytes)
	{
		int m = bytes[0];
		int n = bytes[1];
		int x = bytes[2];
		int y = bytes[3];

		if (m < 0)
		{
			m += 0x100;
		}
		if (n < 0)
		{
			n += 0x100;
		}
		if (x < 0)
		{
			x += 0x100;
		}
		if (y < 0)
		{
			y += 0x100;
		}
		return n*0x100+m+x*0x10000+y*0x1000000;
	}

	private  Vector<Byte> makePackHead(short packType,
									   byte packSeq, byte[] keyData, byte[] ivData, int dataLen)
	{
		short check_sum = 0;
		int i = 0;
		Vector<Byte> v = new Vector<Byte>();

		v.clear();
		v.addAll(ustobytes(packType));
		v.add(packSeq);

		v.add(keyData[0]);
		v.add(keyData[1]);
		v.add(keyData[2]);
		v.add(keyData[3]);
		v.add(keyData[4]);
		v.add(keyData[5]);

		v.add(ivData[0]);
		v.add(ivData[1]);
		v.add(ivData[2]);
		v.add(ivData[3]);
		v.add(ivData[4]);
		v.add(ivData[5]);
		v.add(ivData[6]);
		v.add(ivData[7]);

		v.addAll(ultobytes(dataLen));

		for (i=0; i<v.size(); i++)
		{
			int n = v.get(i);
			if (n < 0)
			{
				n += 256;
			}
			check_sum += n;
		}
		v.addAll(ustobytes(check_sum));
		return v;
	}

	private int parsePackHead(byte[] packHead, short[] packType,
							  byte[] packSeq, byte[] keyData, byte[] ivData, int[] dataLen)
	{
		short check_sum1 = 0;
		short check_sum2 = 0;
		byte[] tmp = new byte[4];

		for (int i=0; i<packHead.length-2; i++)
		{
			short n = packHead[i];
			if (n < 0)
			{
				n += 0x100;
			}
			check_sum1 += n;
		}

		tmp[0] = packHead[packHead.length-2];
		tmp[1] = packHead[packHead.length-1];
		check_sum2 = bytestous(tmp);
		if (check_sum2 != check_sum1)
		{
			return -1;
		}

		tmp[0] = packHead[0];
		tmp[1] = packHead[1];
		packType[0] = bytestous(tmp);
		packSeq[0] = packHead[2];

		keyData[0] = packHead[3];
		keyData[1] = packHead[4];
		keyData[2] = packHead[5];
		keyData[3] = packHead[6];
		keyData[4] = packHead[7];
		keyData[5] = packHead[8];

		ivData[0] = packHead[9];
		ivData[1] = packHead[10];
		ivData[2] = packHead[11];
		ivData[3] = packHead[12];
		ivData[4] = packHead[13];
		ivData[5] = packHead[14];
		ivData[6] = packHead[15];
		ivData[7] = packHead[16];

		tmp[0] = packHead[17];
		tmp[1] = packHead[18];
		tmp[2] = packHead[19];
		tmp[3] = packHead[20];
		dataLen[0] = bytestoul(tmp);
		return 0;
	}

	private byte[] makePacket(short packType, byte packSeq, Vector<Byte> data)
	{
		Vector<Byte> v = new Vector<Byte>();
		v.clear();

		byte[] keyData = new byte[6];
		keyData[0] = 0;
		keyData[1] = 0;
		keyData[2] = 0;
		keyData[3] = 0;
		keyData[4] = 0;
		keyData[5] = 0;

		byte[] ivData = new byte[8];
		ivData[0] = 0;
		ivData[1] = 0;
		ivData[2] = 0;
		ivData[3] = 0;
		ivData[4] = 0;
		ivData[5] = 0;
		ivData[6] = 0;
		ivData[7] = 0;

		v.addAll(makePackHead(packType, packSeq, keyData, ivData, data.size()+2));
		v.addAll(data);
		short checkSum = 0;
		for (int i=0; i<data.size(); i++)
		{
			int n = data.get(i);
			if (n < 0)
			{
				n += 256;
			}
			checkSum += n;
		}
		v.addAll(ustobytes(checkSum));

		byte b[] = new byte[v.size()];
		for (int i=0; i<v.size(); i++)
		{
			b[i] = v.get(i);
		}
		return b;
	}

	/** 功能: 对字段长度编码
	 * 参数: field_len-字段长度(输入)
	 * buf-编码后的字段长度
	 * 返回: 编码后的长度
	 * */
	private Vector<Byte> makeFiledLen(int fieldLen)
	{
		Vector<Byte> v = new Vector<Byte>();
		v.clear();

		if (fieldLen < 0x80)
		{
			v.add((byte)fieldLen);
			return v;
		}

		if (fieldLen < 0x100)
		{
			v.add((byte)0x81);
			v.add((byte)fieldLen);
			return v;
		}

		if (fieldLen < 0x10000)
		{
			v.add((byte)0x82);
			v.add((byte)(fieldLen%0x100));
			v.add((byte)(fieldLen/0x100));
			return v;
		}

		if (fieldLen < 0x1000000)
		{
			v.add((byte)0x83);
			v.add((byte)(fieldLen%0x100));
			v.add((byte)((fieldLen%0x10000)/0x100));
			v.add((byte)(fieldLen/0x10000));
			return v;
		}

		v.add((byte)0x84);
		v.add((byte)(fieldLen%0x100));
		v.add((byte)((fieldLen%0x10000)/0x100));
		v.add((byte)((fieldLen/0x10000)%0x100));
		v.add((byte)((fieldLen/0x10000)/0x100));

		return v;
	}

	protected Vector<Byte> MakeField(String value)
	{
		if (value == null)
		{
			value = "";
		}
		Vector<Byte> v = new Vector<Byte>();

		byte[] b = null;
		try
		{
			b = value.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
		}
		v.addAll(makeFiledLen(b.length));
		for (int i=0; i<b.length; i++)
		{
			v.add(b[i]);
		}
		return v;
	}

	protected Vector<Byte> MakeField(byte[] value)
	{
		Vector<Byte> v = new Vector<Byte>();

		v.addAll(makeFiledLen(value.length));
		for (int i=0; i<value.length; i++)
		{
			v.add(value[i]);
		}
		return v;
	}

	/** 功能: 对字段长度进行解码
	 参数: buf-字段长度编码数据
	 field_len-字段长度
	 返回: 编码的字段长度
	 */
	private int parseFieldLen(byte[] buf, int index, int[] fieldLen)
	{
		byte len;
		byte i;
		int flength;

		if (buf[index]>=0 && buf[index]<0x80)
		{
			fieldLen[0] = buf[index];
			return 1;
		}

		flength = 0;
		len = (byte)(buf[index]&0x7f);
		for (i=0; i<len; i++)
		{
			int b = buf[index+i+1];
			if (b < 0)
			{
				b += 0x100;
			}
			flength += (b<<(i*8));
		}
		fieldLen[0] = flength;
		return len+1;
	}

	/** 功能: 字段解码
	 参数: data_len-数据长度, field-编码后的字段,
	 buf_size-缓冲区长度, field_len-字段长度,
	 value-字段值
	 返回: 字段编码后的长度
	 */
	String parseFiled(byte[] buf, int index, int[] length) {
		int[] filedLen = new int[1];

		length[0] = parseFieldLen(buf, index, filedLen);
		if (index+length[0]+filedLen[0] > buf.length) {
			return null;
		}

		byte[] value = Arrays.copyOfRange(buf, index+length[0],
				index+length[0]+filedLen[0]);
		length[0] += filedLen[0];
		try {
			return new String(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	byte[] parseByteFiled(byte[] buf, int index, int[] length) {
		int[] filedLen = new int[1];

		length[0] = parseFieldLen(buf, index, filedLen);
		if (index+length[0]+filedLen[0] > buf.length)
		{
			return null;
		}

		byte[] value = Arrays.copyOfRange(buf, index+length[0],
				index+length[0]+filedLen[0]);
		length[0] += filedLen[0];
		return value;
	}

	protected abstract Vector<Byte> MakePackBody();

	private Socket commSocket;
	private short packType;
	private short retPackType;

	public static Socket connect(String host, int port,
								 int timeout, StringBuilder message)
	{
		InetSocketAddress address = new InetSocketAddress(host, port);
		Socket sock = new Socket();
		try {
			sock.connect(address, timeout);
			return sock;
		} catch (IOException e) {
			message.append("与服务器通信失败，请检查网络和IP端口号");
			return null;
		}
	}

	public static void close(Socket sock) {
		try {
			sock.shutdownInput();
			sock.shutdownOutput();
			sock.close();
		} catch (Exception e) {
		}
	}
	protected BaseComm(Socket socket, short packType, short retPackType) {
		this.commSocket = socket;
		this.packType = packType;
		this.retPackType = retPackType;
	}

	private int recv(byte[] data)
	{
		int bytes;
		InputStream in;
		try
		{
			in = commSocket.getInputStream();
			bytes = 0;
			while (bytes < data.length)
			{
				int len = in.read(data, bytes, data.length-bytes);
				bytes += len;
			}
			return bytes;
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	public int executeComm() {
		try {
			OutputStream out = commSocket.getOutputStream();
			out.write(makePacket(packType, (byte)0, MakePackBody()));

			byte[] packHead = new byte[23];
			if (recv(packHead) != packHead.length) {
				message = "与服务器通讯失败，接收应答包头错误！";
				return -1;
			}

			short[] recvpackType = new short[1];
			byte[] packSeq = new byte[1];
			byte[] keyData = new byte[6];
			byte[] ivData = new byte[8];
			int[] dataLen = new int[1];

			int result = parsePackHead(packHead, recvpackType, packSeq, keyData, ivData, dataLen);
			if (result != 0) {
				message = "应答包头错误！";
				return -2;
			}

			if (retPackType != recvpackType[0]) {
				message = "应答包类型错误！";
				return -3;
			}

			byte[] data = new byte[dataLen[0]];
			int len = recv(data);
			if (len != dataLen[0]) {
				message = "接收应答包体错误！";
				return -4;
			}

			result = fetchData(data);
			return result;
		} catch (IOException err) {
			message = err.getMessage();
			return -100;
		}
	}

	protected int fetchData(byte[] data) {
		return 0;
	}
}
