package com.miaxis.storageroom.comm;

import java.net.Socket;
import java.util.Vector;
import com.miaxis.storageroom.bean.TaskExe;

public class ExecTaskComm extends BaseComm
{
	private static final short FUNC_REQ_EXEC_TASK=129;
	private static final short FUNC_RET_EXEC_TASK=8129;
	private TaskExe task;
	
	public ExecTaskComm(Socket socket, TaskExe task)
	{
		super(socket, FUNC_REQ_EXEC_TASK, FUNC_RET_EXEC_TASK);
		this.task = task;
	}

	@Override
	protected Vector<Byte> MakePackBody()
	{
		Vector<Byte> data = new Vector<Byte>();
		data.clear();

		data.addAll(MakeField(task.getTaskcode()));
		data.addAll(MakeField(task.getTasktype()));
		data.addAll(MakeField(task.getDeptno()));

		data.addAll(MakeField(task.getWorkercode()));
		data.addAll(MakeField(task.getWorkername()));
		
		data.addAll(MakeField(task.getEscode1()));
		data.addAll(MakeField(task.getEsname1()));

		data.addAll(MakeField(task.getEscode2()));
		data.addAll(MakeField(task.getEsname2()));

		data.addAll(MakeField(task.getCarcode()));
		data.addAll(MakeField(task.getPlateno()));
		data.addAll(MakeField(task.getTasktime()));
		data.addAll(MakeField(task.getStatus()));
		
		return data;
	}

	@Override
	protected int fetchData(byte[] data)
	{
		int result = bytestous(data);
		switch (result)
		{
			case 0:
				return 0;
			case ERROR_DBACCESS:
				message = "服务器访问数据库错误!";
				return -100;
			case ERROR_DEPT_NOT_EXIST:
				message = "网点不存在!";
				return -101;
			case ERROR_TASK_NOT_EXIST:
				message = "任务未申报";
				return -102;
			case ERROR_TOTAL_TASK_NOT_EXIST:
				message = "未生成该网点的总任务";
				return -103;
			case ERROR_TASK_NOT_CHECK:
				message = "任务未审核";
				return -104;
			case ERROR_TASK_EXECUTED:
				message = "任务已执行";
				return -105;
			case ERROR_TASK_DELETED:
				message = "任务已注销";
				return -106;
			case ERROR_DATE_NOTDUE:
				message = "只能执行当天的任务";
				return -107;
			case ERROR_DATE_OVERDUE:
				message = "不能执行过期的任务";
				return -108;
			case ERROR_TIME_OVERDUE:
				message = "执行时间无效";
				return -109;
			default:
				message = "未知应答码["+result+"]";
				return -4;
		}
	}
}
