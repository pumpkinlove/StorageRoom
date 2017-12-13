package com.miaxis.storageroom.comm;

import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.bean.Worker;

import java.net.Socket;
import java.util.Vector;

public class UpdateEscortComm extends BaseComm {
	private static final short FUNC_REQ_UPDATE_ESCORT = 130;
	private static final short FUNC_RET_UPDATE_ESCORT = 8130;

	private Escort escort;
	public Escort getEscort(){
		return escort;
	}

	public UpdateEscortComm(Socket socket, Escort escort) {
		super(socket, FUNC_REQ_UPDATE_ESCORT, FUNC_RET_UPDATE_ESCORT);
		this.escort = escort;
	}

	@Override
	protected Vector<Byte> MakePackBody() {
		Vector<Byte> data = new Vector<>();
		data.clear();
		
		data.addAll(MakeField(escort.getName()));
		data.addAll(MakeField(escort.getOpDate()));
		data.addAll(MakeField(escort.getCode()));
		data.addAll(MakeField(escort.getFinger0()));
		data.addAll(MakeField(escort.getFinger1()));
		data.addAll(MakeField(escort.getOpUserCode()));
		data.addAll(MakeField(escort.getOpUserName()));
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
