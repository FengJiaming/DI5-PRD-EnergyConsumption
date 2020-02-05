package controller.simulator.utils;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;


public class LogErrStream extends OutputStream {
	
	private Log log;
 	
	public LogErrStream(Log log) {
 		this.log = log;
 	}

    public void close() {
    }

    public void flush() {
        
    }

    public void write(final byte[] b) throws IOException {
        String s = new String(b);
    	log.error(s);
    }

    public void write(final byte[] b, final int off, final int len)
        throws IOException {
    	if(len == 1)
    		return;
    	
    	String s = new String(b, off, len);
        log.error(s);
    }

    public void write(final int b) throws IOException {
        log.error(b);
    }
}
