package com.TWC.Automation.FrameWork.TWC.Automation.FrameWork;

import java.io.DataOutputStream;
import java.io.IOException;

public class usb {

	public static void main(String[] args) throws Exception {
		
		
		//Settings.Secure.putInt(getActivity().getContentResolver(),Settings.Secure.ADB_ENABLED, 1);

String[] SET_DM_PORT_STATUS_LIST = new String[9];{
    SET_DM_PORT_STATUS_LIST[0] = "setMTP";
    SET_DM_PORT_STATUS_LIST[1] = "setMTPADB";
    SET_DM_PORT_STATUS_LIST[2] = "setPTP";
    SET_DM_PORT_STATUS_LIST[3] = "setPTPADB";
    SET_DM_PORT_STATUS_LIST[4] = "setRNDISDMMODEM";
    SET_DM_PORT_STATUS_LIST[5] = "setRMNETDMMODEM";
    SET_DM_PORT_STATUS_LIST[6] = "setDMMODEMADB";
    SET_DM_PORT_STATUS_LIST[7] = "setMASSSTORAGE";
    SET_DM_PORT_STATUS_LIST[8] = "setMASSSTORAGEADB";}

String[] SET_DM_PORT_CONFIG_LIST = new String[9];{
    SET_DM_PORT_CONFIG_LIST[0] = "mtp";
    SET_DM_PORT_CONFIG_LIST[1] = "mtp,adb";
    SET_DM_PORT_CONFIG_LIST[2] = "ptp";
    SET_DM_PORT_CONFIG_LIST[3] = "ptp,adb";
    SET_DM_PORT_CONFIG_LIST[4] = "rndis,acm,diag";
    SET_DM_PORT_CONFIG_LIST[5] = "rmnet,acm,diag";
    SET_DM_PORT_CONFIG_LIST[6] = "diag,acm,adb";
    SET_DM_PORT_CONFIG_LIST[7] = "mass_storage";
    SET_DM_PORT_CONFIG_LIST[8] = "mass_storage,adb";}

Process su = Runtime.getRuntime().exec("su");
             DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
int paramInt = 0;
outputStream.writeBytes("am broadcast -a android.provider.Telephony.SECRET_CODE -d android_secret_code://" + SET_DM_PORT_STATUS_LIST[paramInt]+"\n");
outputStream.writeBytes("setprop sys.usb.config " + SET_DM_PORT_CONFIG_LIST[paramInt]+"\n");
            if(SET_DM_PORT_STATUS_LIST[paramInt].contains("adb")){
                outputStream.writeBytes("settings put global adb_enabled 1\n");
            }
		

	}

}
