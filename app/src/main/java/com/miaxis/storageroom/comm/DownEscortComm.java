package com.miaxis.storageroom.comm;


import com.miaxis.storageroom.bean.Escort;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by xu.nan on 2017/6/30.
 */

public class DownEscortComm extends BaseComm {

    private static final short FUNC_REQ_DOWN_ESCORT = 125;
    private static final short FUNC_RET_DOWN_ESCORT = 8125;

    private List<Escort> escortList;
    private String orgCode;
    private String timeStamp;
    private String pageNum;
    private String pageSize;

    public List<Escort> getEscortList() {
        return escortList;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public DownEscortComm(Socket socket, String orgCode, String timeStamp, String pageNum, String pageSize) {
        super(socket, FUNC_REQ_DOWN_ESCORT, FUNC_RET_DOWN_ESCORT);
        this.orgCode = orgCode;
        this.timeStamp = timeStamp;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        escortList = new ArrayList<>();
    }

    @Override
    protected Vector<Byte> MakePackBody() {
        Vector<Byte> data = new Vector<Byte>();
        data.clear();
        data.addAll(MakeField(orgCode));
        data.addAll(MakeField(timeStamp));
        data.addAll(MakeField(pageNum));
        data.addAll(MakeField(pageSize));
        return data;
    }

    @Override
    protected int fetchData(byte[] data) {
        int result = bytestous(data);
        if (result == ERROR_DBACCESS)
        {
            message = "服务器访问数据库错误!";
            return -100;
        }
        if (result != 0)
        {
            message = "未知应答码["+result+"]";
            return -5;
        }

        int nCount = data[2];
        if (nCount < 0)
        {
            nCount += 256;
        }

        int index = 3;

        if (nCount > 0) {
            int[] len = new int[1];
            timeStamp = parseFiled(data, index, len);
            if (timeStamp == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            index += len[0];
        }

        for (int i=0; i<nCount; i++)
        {
            String str;
            Escort escort = new Escort();
            int[] length = new int[1];

            str = parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setId(Long.valueOf(str));
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setCode(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setName(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setPassword(str);
            index += length[0];

            byte[] photo = this.parseByteFiled(data, index, length);
            if (photo == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setPhoto(photo);

            index += length[0];
            for (int m=0; m<10; m++)
            {
                str = this.parseFiled(data, index, length);
                if (str == null)
                {
                    message = "数据包体错误!";
                    return -6;
                }
                escort.setFinger(str, m);
                index += length[0];
            }
            escortList.add(escort);
        }
        return 0;
    }
}
