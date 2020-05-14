import static java.nio.file.StandardWatchEventKinds.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import battle.BattleMainControll;
import util.AodemiLogger;
import util.BattleControllUtil;
import util.BattleFieldUtil;
import util.CardUtil;
import util.StringUtil;

public class Aodemi {

	ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
        WatchService watcher;
        FileInputStream bfStream = null;
        String inputFilePath = args[0];
    	String inputFile = "";
    	String outputFile = "";
    	boolean initFlg = true;
    	boolean player1Flg = true;

    	if (args.length == 2) {
    		initFlg = false;
    	}

        try {
        	//ファイルパスに末尾が無い場合は追加
        	if (!inputFilePath.endsWith("\\")) {
        		inputFilePath = inputFilePath + "\\";
        	}

        	//ファイル、文字列操作ユーティリティを呼び出す
        	StringUtil stringUtil = new StringUtil();

            //対戦管理シートの読み取り、初期化
        	BattleControllUtil battleControllUtil = new BattleControllUtil(inputFilePath);

        	//テスト用処理。１を指定すればプレイヤー１、２ならプレイヤー２
        	if (initFlg) {
        		player1Flg = battleControllUtil.startBattle();
        	} else {
        		if ("1".equals(args[1])) {
        			player1Flg = true;
        		} else {
        			player1Flg = false;
        		}
        	}

        	//盤面情報シートの読み取り
        	BattleFieldUtil battleFieldUtil  = new BattleFieldUtil();

        	//どちらのプレイヤーかを判断し、読み込みパスを設定 TODO 本来は毎回読み込む必要あり
        	String playerPath = "";
            HashMap<String, Object> map = new HashMap<String, Object>();

        	if (player1Flg) {
        		playerPath = inputFilePath + "プレイヤー１\\";
        	} else {
        		playerPath = inputFilePath + "プレイヤー２\\";
        	}

        	inputFile = playerPath + "input.json";
        	outputFile = playerPath + "output.json";

        	//ファイルが存在した場合は削除
        	File file = new File(inputFile);

            if (file.isFile()) {
            	file.delete();
            }

            //各ユーザーのデッキ情報の読み取り用ユーティリティ起動
            CardUtil cardUtil = new CardUtil(inputFilePath);
            BattleMainControll battleMainControll = new BattleMainControll(inputFilePath, player1Flg);

            System.out.println("準備が出来ました。input.jsonを更新してください");

            watcher = FileSystems.getDefault().newWatchService();
            Watchable path = Paths.get(playerPath);
            path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

	        while (true) {

	            WatchKey watchKey;

	            try {
	                watchKey = watcher.take();
	            } catch (InterruptedException e) {
	                System.err.println(e.getMessage());
	                return;
	            }

            for (WatchEvent<?> event : watchKey.pollEvents()) {

                Object context = event.context();

                //更新以外は処理しない
                if (event.kind() != ENTRY_CREATE) {
                	break;
                }

                //更新ファイルがinput.json以外の場合は反応しない
                String fileName = new File(context.toString()).getName();
                if (!"input.json".equals(fileName)) {
                	break;
                }

                if (map.size() != 0) {
                	break;
                }

                //入力ファイルの読み込み
                map = stringUtil.readJsonFile(inputFile);

                String data = stringUtil.getJsonStr(map).replace("\r\n","");
                AodemiLogger.writeLogOnly("input:" + data);

                //初期化処理の場合
                if ("init".equals(map.get("phase"))) {

                	//init処理を呼び出す
                	battleMainControll.init(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                //init以外のタイミングで相手が敗北している場合は即座に勝利する
                if (battleControllUtil.cheackEnemyWinLose(player1Flg)) {
                	AodemiLogger.write("あなたの勝利です");

                	HashMap<String, Object> outMap = new HashMap<String, Object>();
                	outMap.put("severCheack", "win");

                	stringUtil.serializeJson(outMap, outputFile);

                	//inputファイルが存在する場合は削除
                    if (file.isFile()) {
                    	file.delete();
                    }

                	continue;
                }

                if ("set".equals(map.get("phase"))) {

                	//set処理を呼び出す
                	battleMainControll.set(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }


                if ("open".equals(map.get("phase"))) {

                	//open処理を呼び出す
                	battleMainControll.open(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if ("remove".equals(map.get("phase"))) {

                	//remove処理を呼び出す
                	battleMainControll.remove(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if ("start".equals(map.get("phase"))) {

                	//startフェーズ処理を呼び出す
                	battleMainControll.start(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if ("openSelect".equals(map.get("phase")) ||
                		"startSelect".equals(map.get("phase")) ||
                		"autoSelect".equals(map.get("phase")) ||
                		"skill1Select".equals(map.get("phase")) ||
                		"skill2Select".equals(map.get("phase")) ||
                		"shieldSelect".equals(map.get("phase")) ||
                		"specialSelect".equals(map.get("phase")) ||
                		"closeSelect".equals(map.get("phase"))) {

                	//select機能を呼び出す
                	battleMainControll.select(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if ("battle".equals(map.get("phase"))) {

                	//battle開始フェーズ処理を呼び出す
                	battleMainControll.battle(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                //行動選択
                if ("battleAction".equals(map.get("phase"))) {

                	//battleAction処理を呼び出す
                	battleMainControll.battleAction(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if ("close".equals(map.get("phase"))) {

                	//set処理を呼び出す
                	battleMainControll.close(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                //リザルト
                if ("surrender".equals(map.get("phase"))) {

                	//surrender処理を呼び出す
                	battleMainControll.surrender(map, battleFieldUtil, battleControllUtil, cardUtil, stringUtil);

                }

                if (file.isFile()) {
                	file.delete();
                }
            }

            //インプットデータを初期化する
            map = new HashMap<String, Object>();

            //監視リセット
            watchKey.reset();

        }

        } catch (Exception e) {

            try {

            	StringWriter sw = new StringWriter();
            	PrintWriter pw = new PrintWriter(sw);
            	e.printStackTrace(pw);
            	pw.flush();

				AodemiLogger.write(sw.toString());

			} catch (IOException e1) {
				e1.printStackTrace();
			}

            File file = new File(inputFile);

            if (file.isFile()) {
            	file.delete();
            }

            return;
        } finally {

        	try {
	        	if (bfStream != null) {
	        		bfStream.close();
	        	}
			} catch (IOException e) {

				e.printStackTrace();

			}
        }
    }

}
