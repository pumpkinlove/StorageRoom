package com.miaxis.storageroom.comm;

import java.net.Socket;
import java.util.Vector;

public class DeleteEscortComm extends BaseComm {
	private static final short FUNC_REQ_DELETE_ESCORT = 132;
	private static final short FUNC_RET_DELETE_ESCORT = 8132;

	private String escortCode;

	public DeleteEscortComm(Socket socket, String escortCode) {
		super(socket, FUNC_REQ_DELETE_ESCORT, FUNC_RET_DELETE_ESCORT);
		this.escortCode = escortCode;
	}

	@Override
	protected Vector<Byte> MakePackBody() {
		Vector<Byte> data = new Vector<>();
		data.clear();
		
		data.addAll(MakeField(escortCode));
		return data;
	}
	
	@Override
	protected int fetchData(byte[] data) {
		int result = bytestous(data);
		switch (result) {
			case 0:
				return 0;
			case ERROR_DBACCESS:
				message = "服务器访问数据库异常!";
				return -100;
			case ERROR_WORKERCODE_EXIST:
				message = "该编号已存在";
				return -101;
			default:
				message = "未知应答码["+result+"]";
				return -4;
		}
	}
}
