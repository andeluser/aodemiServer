package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AodemiLogger {

	private static String fileName = "";

	FileWriter filewriter = null;
	private static String battleId = "";
	private static String playerId = "";
	private static String programName = "";
	private static AodemiLogger instance = null;

	private static boolean player1Flg = true;

	private static final long serialVersionUID = 1L;

	private AodemiLogger(String battleId, String playerId, boolean playerFlg) throws SecurityException, IOException {

		this.battleId = battleId;
		this.playerId = playerId;
		this.player1Flg = playerFlg;

//		this.programName = programName;
		//filePath = new File(".").getAbsoluteFile().getParent();
		Calendar cal = Calendar.getInstance();

//		if (playerFlg) {
//			fileName = "log_player1.log";
//		} else {
//			fileName = "log_player2.log";
//		}

//		fileName = "log.log";

		String filePath = "C:\\log\\";

		//フォルダが存在しない場合は作成
		File folder = new File(filePath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		switch(cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				fileName = filePath + "sun_log.log";
				break;
			case Calendar.MONDAY:
				fileName = filePath + "mon_log.log";
				break;
			case Calendar.TUESDAY:
				fileName = filePath + "tue_log.log";
				break;
			case Calendar.WEDNESDAY:
				fileName = filePath + "wed_log.log";
				break;
			case Calendar.THURSDAY:
				fileName = filePath + "thu_log.log";
				break;
			case Calendar.FRIDAY:
				fileName = filePath + "fri_log.log";
				break;
			case Calendar.SATURDAY:
				fileName = filePath + "sat_log.log";
				break;
		}

		File file = new File(fileName);

		//ファイルが存在しない場合は作成
		if (!file.exists()) {
			file.createNewFile();
		}

		//６日以上経過した場合は削除
		Long lastmodified = file.lastModified();
		Date kousin = new Date(lastmodified);

		Calendar beforeCal = Calendar.getInstance();
		beforeCal.add(Calendar.DATE, -2);
		Date start = beforeCal.getTime();

		if (start.compareTo(kousin) > 0) {
			file.delete();
		}
//
//		FileHandler fileHandler = new FileHandler(filePath, true);
//		fileHandler.setFormatter(new SimpleFormatter());
//		logger.addHandler(fileHandler);
//		logger.setLevel(Level.FINE);
//
//		ConsoleHandler consoleHandler = new ConsoleHandler();
//		consoleHandler.setLevel(Level.CONFIG);
//		logger.addHandler(consoleHandler);
//		logger.setUseParentHandlers(false);

	}

	public static AodemiLogger getInstance(String battleId, String playerId, boolean playerFlg) throws SecurityException, IOException {

		if (instance == null) {

			instance = new AodemiLogger(battleId, playerId, playerFlg);

		}

	    return instance;

	}

	public static synchronized void write(String str) throws IOException {

		BufferedWriter bw = null;
		OutputStreamWriter osw = null;

		try {

			File f = new File(fileName);
		    osw  = new OutputStreamWriter(new FileOutputStream(f, true), "Shift-JIS");
		    bw = new BufferedWriter(osw);

		    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    String time = sdf.format(timestamp);

		    String player = "";
		    if (player1Flg) {
		    	player = "プレイヤー１";
		    } else {
		    	player = "プレイヤー２";
		    }

		    bw.write("[" + time + "][BatteID:" + battleId + "][" + player + "]" + str);
		    bw.newLine();

		    System.out.println("[" + time + "][BatteID:" + battleId + "][" + player + "]" + str);

		} finally {

			if (bw != null) {
				bw.close();
			}

			if (osw != null) {
				osw.close();
			}

		}
	}

	public static synchronized void writeLogOnly(String str) throws IOException {

		BufferedWriter bw = null;
		OutputStreamWriter osw = null;

		try {

			File f = new File(fileName);
		    osw  = new OutputStreamWriter(new FileOutputStream(f, true), "Shift-JIS");
		    bw = new BufferedWriter(osw);

		    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    String time = sdf.format(timestamp);

		    String player = "";
		    if (player1Flg) {
		    	player = "プレイヤー１";
		    } else {
		    	player = "プレイヤー２";
		    }

		    bw.write("[" + time + "][BatteID:" + battleId + "][" + player + "]" + str);
		    bw.newLine();

		} finally {

			if (bw != null) {
				bw.close();
			}

			if (osw != null) {
				osw.close();
			}

		}
	}

	public void setBattleId(String battleId) {
		this.battleId = battleId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
}
