package com.miaxis.storageroom.comm;

import com.miaxis.storageroom.bean.Worker;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by xu.nan on 2017/7/12.
 */

public class DownWorkerComm extends BaseComm {

    private static final short FUNC_REQ_DOWN_WORKER = 127;
    private static final short FUNC_RET_DOWN_WORKER = 8127;

    private List<Worker> workerList;
    private String orgCode;
    private String timeStamp;

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public DownWorkerComm(Socket socket, String orgCode, String timeStamp) {
        super(socket, FUNC_REQ_DOWN_WORKER, FUNC_RET_DOWN_WORKER);
        this.orgCode = orgCode;
        this.timeStamp = timeStamp;
        workerList = new ArrayList<>();
    }

    @Override
    protected Vector<Byte> MakePackBody() {
        Vector<Byte> data = new Vector<>();
        data.clear();
        data.addAll(MakeField(orgCode));
        data.addAll(MakeField(timeStamp));
        return data;
    }

    @Override
    protected int fetchData(byte[] data) {
        int result = bytestous(data);
        if (result == ERROR_DBACCESS) {
            message = "服务器访问数据库错误!";
            return -100;
        }
        if (result != 0) {
            message = "未知应答码["+result+"]";
            return -5;
        }

        int nCount = data[2];
        if (nCount < 0) {
            nCount += 256;
        }

        int index = 3;

        int[] len = new int[1];

        if (nCount > 0) {
            timeStamp = parseFiled(data, index, len);
            if (timeStamp == null) {
                message = "数据包体错误!";
                return -6;
            }
            index += len[0];
        }

        for (int i=0; i<nCount; i++) {
            String str;
            Worker worker = new Worker();
            int[] length = new int[1];

            str = parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setId(Long.valueOf(str));
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setCode(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setName(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setOrgCode(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setPhone(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setIdCard(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setOpUser(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setOpUserName(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            worker.setOpDate(str);
            index += length[0];

            for (int m=0; m<10; m++) {
                str = this.parseFiled(data, index, length);
                if (str == null) {
                    message = "数据包体错误!";
                    return -6;
                }
                worker.setFinger(str, m);
                index += length[0];
            }
            workerList.add(worker);
        }
        return 0;
    }
}
