package com.example.remote_assitant.main_application;

import android.annotation.SuppressLint;

import com.example.remote_assitant.utilities.DeviceInfo;
import com.example.remote_assitant.utilities.Resolution;
public class GlobalRepository {
    public static char _Delimiter = ':';
    public static char _EndSymbol = '!';

    public static class Actions {

        public final static char _MouseMoveAction          = 'm';
        public final static char _LayoutScreenInfo         = 'l';
        public final static char _MouseLeftClickAction     = 'c';
        public final static char _MouseDoubleClickAction   = 'd';
        public final static char _MouseRightClickAction    = 'r';
        public final static char _Login                    = 'o';
        public final static char _LoginWithUUID            = 'u';
        public final static char _LoginSuccessful          = 'e';
        public final static char _LoginFailed              = 'f';


        private static final String _EndCharSequence = "" + _Delimiter + _EndSymbol ;

       @SuppressLint("DefaultLocale")
       public static String getMouseMoveActionCommandText(final long isUp, final long isRight) {
           return String
                   .format("%c%c%d%c%d%s"
                           , _MouseMoveAction
                           , _Delimiter
                           , isUp
                           , _Delimiter
                           , isRight
                           , _EndCharSequence);
       }

        @SuppressLint("DefaultLocale")
        public static String getLayoutScreenInfoCommandText(Resolution resolution) {
            return String
                    .format("%c%c%d%c%d%s" // l:w:h:!
                            , _LayoutScreenInfo
                            , _Delimiter
                            , resolution.getWidth()
                            , _Delimiter
                            , resolution.getHeigth()
                            , _EndCharSequence);
        }

        @SuppressLint("DefaultLocale")
        public static String getDeviceInfoCommandText(String deviceName, String ModelName) {
           return "";
        }

        @SuppressLint("DefaultLocale")
        public static String getMouseClickActionCommandText() {
           return String
                   .format("%c%s" // c:!
                            , _MouseLeftClickAction
                            , _EndCharSequence);
        }

        @SuppressLint("DefaultLocale")
        public static String getMouseRightClickActionCommandText() {
            return String
                    .format("%c%s" // c:!
                            , _MouseRightClickAction
                            , _EndCharSequence);
        }

        @SuppressLint("DefaultLocale")
        public static String getMouseDoubleClickActionCommandText() {
            return String
                    .format("%c%s" // c:!
                            , _MouseDoubleClickAction
                            , _EndCharSequence);
        }

        public static String getLoginCredentialsCommandText(String Password, DeviceInfo deviceInfo) {
           return String
                   .format("%c%c%s%c%s%c%s%s" // 0:pass:deviceName:deviceInfo:!
                           , _Login
                           , _Delimiter
                           , Password
                           , _Delimiter
                           , deviceInfo.getDeviceName()
                           , _Delimiter
                           , deviceInfo.getDeviceModel()
                           , _EndCharSequence);

        }

        public static String getLoginWithUUIDCommandText(String base64UUID) {
           return String
                   .format("%c%c%s%s"
                   , _LoginWithUUID
                   , _Delimiter
                   , base64UUID
                   , _EndCharSequence);
        }

    }

}
