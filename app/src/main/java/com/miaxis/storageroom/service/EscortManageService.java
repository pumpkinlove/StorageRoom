package com.miaxis.storageroom.service;

import android.content.Intent;
import android.content.Context;

import com.miaxis.storageroom.bean.Escort;
import com.miaxis.storageroom.comm.DeleteEscortComm;
import com.miaxis.storageroom.comm.UpdateEscortComm;
import com.miaxis.storageroom.event.CommExecEvent;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;

public class EscortManageService extends BaseService {
    private static final String ACTION_UPDATE_ESCORT = "com.miaxis.storageroom.service.action.ACTION_UPDATE_ESCORT";
    private static final String ACTION_DELETE_ESCORT = "com.miaxis.storageroom.service.action.ACTION_DELETE_ESCORT";

    private static final String UPDATE_ESCORT = "com.miaxis.storageroom.service.extra.UPDATE_ESCORT";
    private static final String DELETE_ESCORT = "com.miaxis.storageroom.service.extra.DELETE_ESCORT";

    public EscortManageService() {
        super("EscortManageService");
    }

    public static void startActionUpdateEscort(Context context, Escort escort) {
        Intent intent = new Intent(context, AddWorkerService.class);
        intent.setAction(ACTION_UPDATE_ESCORT);
        intent.putExtra(UPDATE_ESCORT, escort);
        context.startService(intent);
    }

    public static void startActionDeleteEscort(Context context, Escort escort) {
        Intent intent = new Intent(context, AddWorkerService.class);
        intent.setAction(ACTION_DELETE_ESCORT);
        intent.putExtra(DELETE_ESCORT, escort);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_ESCORT.equals(action)) {
                final Escort escort = (Escort) intent.getSerializableExtra(UPDATE_ESCORT);
                handleActionUpdateEscort(escort);
            } else if (ACTION_DELETE_ESCORT.equals(action)) {
                final Escort escort = (Escort) intent.getSerializableExtra(DELETE_ESCORT);
                handleActionDeleteEscort(escort);
            }
        }
    }

    private void handleActionUpdateEscort(Escort escort) {
        try {
            Socket socket = socketComm();
            if (socket == null) {
                EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_SOCKET_NULL, CommExecEvent.COMM_UPDATE_ESCORT));
            }
            UpdateEscortComm comm = new UpdateEscortComm(socket, escort);
            int result = comm.executeComm();
            EventBus.getDefault().post(new CommExecEvent(result, CommExecEvent.COMM_UPDATE_ESCORT));
        } catch (Exception e) {
            EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_EXCEPTION, CommExecEvent.COMM_UPDATE_ESCORT));
        }
    }

    private void handleActionDeleteEscort(Escort escort) {
        try {
            Socket socket = socketComm();
            if (socket == null) {
                EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_SOCKET_NULL, CommExecEvent.COMM_DELTE_ESCORT));
            }
            DeleteEscortComm comm = new DeleteEscortComm(socket, escort.getCode());
            int result = comm.executeComm();
            EventBus.getDefault().post(new CommExecEvent(result, CommExecEvent.COMM_DELTE_ESCORT));
        } catch (Exception e) {
            EventBus.getDefault().post(new CommExecEvent(CommExecEvent.RESULT_EXCEPTION, CommExecEvent.COMM_DELTE_ESCORT));
        }
    }

}
