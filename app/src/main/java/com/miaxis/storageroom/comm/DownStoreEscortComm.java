package com.miaxis.storageroom.comm;


import com.miaxis.storageroom.bean.Escort;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by xu.nan on 2017/6/30.
 */

public class DownStoreEscortComm extends BaseComm {

    private static final short FUNC_REQ_DOWN_ESCORT = 131;
    private static final short FUNC_RET_DOWN_ESCORT = 8131;

    private List<Escort> escortList;
    private String orgCode;
    private String pageNum;
    private String pageSize;

    public List<Escort> getEscortList() {
        return escortList;
    }

    public DownStoreEscortComm(Socket socket, String orgCode, String pageNum, String pageSize) {
        super(socket, FUNC_REQ_DOWN_ESCORT, FUNC_RET_DOWN_ESCORT);
        this.orgCode = orgCode;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        escortList = new ArrayList<>();
    }

    @Override
    protected Vector<Byte> MakePackBody() {
        Vector<Byte> data = new Vector<>();
        data.clear();
        data.addAll(MakeField(orgCode));
        data.addAll(MakeField(pageNum));
        data.addAll(MakeField(pageSize));
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

        for (int i=0; i<nCount; i++) {
            String str;
            Escort escort = new Escort();
            int[] length = new int[1];

            str = parseFiled(data, index, length);
            if (str == null) {
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
            escort.setPhone(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setIdCard(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setOpUserCode(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setOpUserName(str);
            index += length[0];

            str = this.parseFiled(data, index, length);
            if (str == null)
            {
                message = "数据包体错误!";
                return -6;
            }
            escort.setOpDate(str);
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

            str = this.parseFiled(data, index, length);
            if (str == null) {
                message = "数据包体错误!";
                return -6;
            }
            try {
                int c = Integer.valueOf(str);
                if (c == 0) {
                    escort.setCollected(true);
                } else if (c == 1) {
                    escort.setCollected(false);
                } else {
                    message = "数据包体错误";
                    return -6;
                }
            } catch (Exception e) {
                message = "数据包体错误";
                return -6;
            }
            index += length[0];
            escortList.add(escort);
        }
        return 0;
    }
}
