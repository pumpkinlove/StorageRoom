package com.miaxis.storageroom.comm;

import java.net.Socket;
import java.util.Vector;

/**
 * Created by xu.nan on 2017/7/3.
 */

public class CountEscortComm extends BaseComm {
    private static final short FUNC_REQ_COUNT_ESCORT = 128;
    private static final short FUNC_RET_COUNT_ESCORT = 8128;

    private int escortCount;
    private String orgCode;
    private String timeStamp;

    public int getEscortCount() {
        return escortCount;
    }

    public CountEscortComm(Socket socket, String orgCode, String timeStamp) {
        super(socket, FUNC_REQ_COUNT_ESCORT, FUNC_RET_COUNT_ESCORT);
        this.orgCode = orgCode;
        this.timeStamp = timeStamp;
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

        escortCount = data[2];
        if (escortCount < 0) {
            escortCount += 256;
        }

        return 0;
    }
}
