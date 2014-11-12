

// -----( IS Java Code Template v1.2
// -----( CREATED: 2008-03-27 11:38:37
// -----( ON-HOST: mundov3

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.wm.lang.ns.NSService;
// --- <<IS-END-IMPORTS>> ---

public final class vLog

{
	// ---( internal utility methods )---

	final static vLog _instance = new vLog();

	static vLog _newInstance() { return new vLog(); }

	static vLog _cast(Object o) { return (vLog)o; }

	// ---( server methods )---




	public static final void log (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(log)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required message
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	message = IDataUtil.getString( pipelineCursor, "message" );
		pipelineCursor.destroy();
		
		if (os == null) {
		synchronized(fileLock) {
			open();
		}
		}
		
		if (os != null) {
		try {
			String timestamp = df.format(new Date());
			os.write(timestamp.getBytes());
			NSService nsService = Service.getCallingService();
			if (nsService != null) {
				os.write(OPENSQUARE);
				os.write(nsService.toString().getBytes());
				os.write(CLOSESQUARE);
			}
			os.write(message.getBytes());
			os.write(NEWLINE);
			os.flush();
		} catch (IOException ioex) {}
		}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static Object fileLock = new Object();
	static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS - ");
	static final byte[] OPENSQUARE = "[".getBytes();
	static final byte[] CLOSESQUARE = "] ".getBytes();
	static final byte[] NEWLINE = "\n".getBytes();
	static OutputStream os;
	static {
		open();
	}
	private static void open() {
	try {
		os = new BufferedOutputStream(new FileOutputStream("logs/vlog.log", true));
	} catch (IOException ioex) {
		os = null;
	}
	}
	// --- <<IS-END-SHARED>> ---
}

