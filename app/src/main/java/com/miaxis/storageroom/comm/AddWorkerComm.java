package com.miaxis.storageroom.comm;

import java.net.Socket;
import java.util.Vector;

import com.miaxis.storageroom.bean.Worker;

public class AddWorkerComm extends BaseComm {
	private static final short FUNC_REQ_ADD_WORKER = 115;
	private static final short FUNC_RET_ADD_WORKER = 8115;
	
	private Worker worker;
	public Worker getWorkercode(){
		return worker;
	}
	
	public AddWorkerComm(Socket socket, Worker worker){
		super(socket, FUNC_REQ_ADD_WORKER, FUNC_RET_ADD_WORKER);
		this.worker = worker;
	}

	@Override
	protected Vector<Byte> MakePackBody() {
		Vector<Byte> data = new Vector<>();
		data.clear();
		
		data.addAll(MakeField(worker.getOrgCode()));
		data.addAll(MakeField(worker.getName()));
		data.addAll(MakeField(worker.getOpDate()));
		data.addAll(MakeField(worker.getCode()));
		data.addAll(MakeField(worker.getFinger(0)));
		data.addAll(MakeField(worker.getFinger(1)));
		data.addAll(MakeField(worker.getOpUser()));
		data.addAll(MakeField(worker.getOpUserName()));
		return data;
	}
	
	@Override
	protected int fetchData(byte[] data) {
		int result = bytestous(data);
		switch(result){
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
