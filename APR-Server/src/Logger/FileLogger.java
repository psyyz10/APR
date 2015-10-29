package Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import Server.G52APRRequestHandler;

/** It is FileLogger class, the design pattern explanation is in the ServerController class */
public class FileLogger implements ILogger{
	private File file;
	private G52APRRequestHandler requestHandler;
	private char loggerState;
	private PrintWriter writer;
	
	/** Construct the FileLogger with file name */
	public FileLogger(String fileName){
		this(fileName, false);
	}

	/** Construct the FileLogger with file name and whether is reset */
	public FileLogger(String fileName, boolean reset){
		file = new File(fileName);

		try {
			// If reset is true, reset the file content to empty
			if (reset){
				writer = new PrintWriter(fileName);
				writer.print("");
			}

			writer = new PrintWriter(new FileWriter(file, !reset));
			writer.print(formatDate(new Date()) + ", $, " + "starting logging");

		} catch (IOException e) {
			System.out.println("The file " + fileName + "can not be read");
		}
	}

	/** WHen requestHandler notifies, the logger will update */
	@Override
	public void update(Observable arg0, Object arg1) {
		requestHandler = (G52APRRequestHandler)arg0;
		loggerState = requestHandler.getLoggerState();

		/** Depends on different logger state, it will write different kinds of details */
		switch (loggerState){
		case '>' :
			writer.print("\n" + formatDate(new Date()) + ", " + loggerState + ", " + requestHandler.getFileName()
					+ ", " + requestHandler.getRequestLine()); break;
		case '<' :
			writer.print("\n" + formatDate(new Date()) + ", " + loggerState + ", " + requestHandler.getFileName()
					+ ", " + requestHandler.getStatusLine()); break;
		}
		writer.flush();
	}
	
	/** Transform the date to the HTTP date format */
	public String formatDate (Date date){
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		return df.format(date);
	}
}
