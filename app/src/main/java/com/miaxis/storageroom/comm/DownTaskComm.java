package com.miaxis.storageroom.comm;

import com.miaxis.storageroom.bean.Task;
import com.miaxis.storageroom.bean.TaskBox;
import com.miaxis.storageroom.bean.TaskEscort;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DownTaskComm extends BaseComm {
    private static final short FUNC_REQ_DOWN_TASK = 126;
    private static final short FUNC_RET_DOWN_TASK = 8126;

    private String deptno;
    private String taskdate;

    private List<Task> taskList = new ArrayList<>();
    private List<TaskBox> taskBoxList = new ArrayList<>();
    private List<TaskEscort> taskEscortList = new ArrayList<>();

    public List<Task> getTaskList(){
        return taskList;
    }

    public List<TaskBox> getTaskBoxList(){
        return taskBoxList;
    }

    public List<TaskEscort> getTaskEscortList(){
        return taskEscortList;
    }

    public DownTaskComm(Socket socket, String deptno, String taskdate) {
        super(socket, FUNC_REQ_DOWN_TASK, FUNC_RET_DOWN_TASK);
        this.deptno = deptno;
        this.taskdate = taskdate;
    }

    @Override
    protected Vector<Byte> MakePackBody(){
        Vector<Byte> data = new Vector<Byte>();
        data.clear();

        data.addAll(MakeField(deptno));
        data.addAll(MakeField(taskdate));

        return data;
    }

    @Override
    protected int fetchData(byte[] data){
        int result = bytestous(data);
        if (result == ERROR_DBACCESS){
            message = "服务器访问数据库错误!";
            return -100;
        }
        if (result != 0){
            message = "未知应答码["+result+"]";
            return -5;
        }

        int taskCount = data[2];
        if (taskCount < 0){
            taskCount += 256;
        }

        int index = 3;
        for (int i=0; i<taskCount; i++){
            String str;
            Task task = new Task();
            int[] length = new int[1];

            str = parseFiled(data, index, length);
            if (str == null){
                message = "数据包体错误!";
                return -6;
            }
            task.setId(Long.valueOf(str));
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null){
                message = "数据包体错误!";
                return -6;
            }
            task.setTaskcode(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null){
                message = "数据包体错误!";
                return -6;
            }
            task.setTaskseq(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setCarid(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setPlateno(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setStatus(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setTaskdate(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setBegintime(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setEndtime(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setOpuser(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setOpusername(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setOpdate(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setTasktype(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setSeculevel(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setCreatetime(str);
            index += length[0];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            task.setTasklevel(str);
            index += length[0];

            taskList.add(task);

            byte[] buf = parseByteFiled(data, index, length);
            if (buf == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            int boxCount = buf[0];
            index += length[0];

            for (int m=0; m<boxCount; m++)
            {
                TaskBox box = new TaskBox();

                str = parseFiled(data, index, length);
                if (str == null)
                {
                    message = "数据包体错误!";
                    return -6;
                }
                box.setBoxCode(str);
                index += length[0];

                str = parseFiled(data, index, length);
                if (str == null)
                {
                    message = "数据包体错误!";
                    return -6;
                }
                box.setBoxRfid(str);
                index += length[0];

                box.setTaskCode(task.getTaskcode());
                taskBoxList.add(box);
            }

            buf = parseByteFiled(data, index, length);
            if (buf == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            int escortCount = buf[0];
            index += length[0];

            for (int n=0; n<escortCount; n++)
            {
                TaskEscort escort = new TaskEscort();

                str = parseFiled(data, index, length);
                if (str == null)
                {
                    message = "数据包体错误!";
                    return -6;
                }
                escort.setEscortCode(str);
                index += length[0];
                escort.setTaskCode(task.getTaskcode());
                taskEscortList.add(escort);
            }
        }
        return 0;
    }
}
