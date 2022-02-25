package helpers;


public class LoginHelper {

    public static boolean validLogin(String login) {
        if (login.isEmpty() || login.length() > 8 ) return false;
        return true;
    }

    public static boolean validPort(String port) {
        if (port == null) {
            return false;
        }
        try {
            Integer.parseInt(port);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean validIP (String ip) {
        try {
            if (ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
