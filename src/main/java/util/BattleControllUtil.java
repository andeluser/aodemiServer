package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.apache.poi.ss.usermodel.Workbook;

import card.CardAbility;
import cardDataObject.Card;
import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.AbilityFactory;
import factory.DaoFactory;

public class BattleControllUtil {

	private Workbook controllBook = null;
	private String battleControllFilePath = "";
	private String battleID = "T0001";
	BattleControllDAO dao = null;
	DaoFactory factory = null;

	public BattleControllUtil(String inputFilePath) throws Exception {
		this.battleControllFilePath = inputFilePath + "battleControll.xlsx";
		factory = new DaoFactory();
		dao = factory.createControllDAO();
	}

	public boolean startBattle() throws Exception {
		boolean player1Flg = true;

		BattleControllDTO dto = new BattleControllDTO();

		dto.setBattle_id(battleID);
		dto.setSub_no(1);
		dto.setNew_flg(1);
		dto.setTurncount(0);
		dto.setOut("");
		dto.setNext_action("");
		dto.setPlayer_id_1("");
		dto.setSpecial_color_1("");
		dto.setSpecial_id_1("");
		dto.setLife_1(0);
		dto.setDeck_no_1(0);
		dto.setPlayer_id_2("");
		dto.setSpecial_color_2("");
		dto.setSpecial_id_2("");
		dto.setLife_2(0);
		dto.setDeck_no_2(0);
		dto.setPhase("init");
		dto.setStatus_1("ready");
		dto.setStatus_2("ready");
		dto.setPlayer_input_1("");
		dto.setPlayer_input_2("");
		dto.setPlayer_output_1("");
		dto.setPlayer_output_2("");
		dto.setPlayer_revival_1("");
		dto.setPlayer_revival_2("");
		dto.setPlayer_surrend_1(0);
		dto.setPlayer_surrend_2(0);
		dto.setNext_special_player_1("");
		dto.setNext_special_status_1("");
		dto.setNext_special_player_2("");
		dto.setNext_special_status_2("");
		dto.setNext_open_player_1("");
		dto.setNext_open_status_1("");
		dto.setNext_open_player_2("");
		dto.setNext_open_status_2("");

		//最初にデータを登録する。登録が失敗した場合は既に登録済なので更新する
		try {
			BattleControllDTO checkDto = dao.getAllValue(battleID);

			//既に両方のＩＤが登録済みだった場合は、過去の結果が残っているので一度削除する
			if (!"".equals(checkDto.getPlayer_id_1()) && !"".equals(checkDto.getPlayer_id_2())) {
				dao.deleteBattle();
				dao.insert(dto);
			} else if ("".equals(checkDto.getPlayer_id_1()) && "".equals(checkDto.getPlayer_id_2())) {
				//未登録のデータがある場合はプレイヤー２扱いで登録はしない
				player1Flg = false;
			} else {
				dao.insert(dto);
			}

		} catch (Exception e) {
			//例外が発生した場合は登録しない
			player1Flg = false;
		}

		return player1Flg;

//		 Workbook battleControllBook = null;
//		 FileInputStream bfStream = null;
//		 FileOutputStream outStream = null;
//
//		 try {
//				bfStream = new FileInputStream(this.battleControllFilePath);
//				 while (true) {
//					 try {
//						 Thread.sleep(500);
//
//						 //読み取り専用でファイルを開く
//						 battleControllBook = new XSSFWorkbook(bfStream);
//						 break;
//					 } catch (Exception e) {
//						 AodemiLogger.write("Controll ファイルが操作中のため待機...");
//					 }
//				 }
//		        bfStream.close();
//
//		        //1シート目
//		        Sheet battleControllSheet = battleControllBook.getSheetAt(0);
//		        //2行目
//		        Row battleControllRow = battleControllSheet.getRow(1);
//
//				//フェーズがinit以外の場合、シートを初期化する
//				if (!"init".equals(battleControllRow.getCell(9).getStringCellValue())) {
//					//プレイヤー１と判定
//					player1Flg = true;
//
//					//まずシートの内容を削除する。
//					battleControllSheet.removeRow(battleControllRow);
//					Row newRow = battleControllSheet.createRow(1);
//
//					//対戦番号
//					newRow.createCell(0).setCellValue("T0001");
//					//プレイヤーID
//					newRow.createCell(1).setCellValue("");
//					//特殊色1
//					newRow.createCell(2).setCellValue("");
//					//ライフ1
//					newRow.createCell(3).setCellValue("");
//					//利用デッキ番号1
//					newRow.createCell(4).setCellValue("");
//					//プレイヤーID2
//					newRow.createCell(5).setCellValue("");
//					//特殊色2
//					newRow.createCell(6).setCellValue("");
//					//ライフ2
//					newRow.createCell(7).setCellValue("");
//					//相手利用デッキ番号2
//					newRow.createCell(8).setCellValue("");
//					//フェーズ
//					newRow.createCell(9).setCellValue("init");
//					//状態１
//					newRow.createCell(10).setCellValue("ready");
//					//状態２
//					newRow.createCell(11).setCellValue("ready");
//					//OUT
//					newRow.createCell(12).setCellValue("");
//					//nextAction1
//					newRow.createCell(13).setCellValue("");
//					//nextAction2
//					newRow.createCell(14).setCellValue("");
//					//playerInput1
//					newRow.createCell(15).setCellValue("");
//					//playerInput2
//					newRow.createCell(16).setCellValue("");
//					//playerOutput1
//					newRow.createCell(17).setCellValue("");
//					//playerOutput2
//					newRow.createCell(18).setCellValue("");
//					//nextAction
//					newRow.createCell(19).setCellValue("");
//					//playerRevival1
//					newRow.createCell(20).setCellValue("");
//					//playerRevival2
//					newRow.createCell(21).setCellValue("");
//					//playerSurrend1
//					newRow.createCell(22).setCellValue("");
//					//playerSurrend2
//					newRow.createCell(23).setCellValue("");
//					//turnCount
//					newRow.createCell(24).setCellValue("0");
//					//nextAction1
//					newRow.createCell(25).setCellValue("");
//					//status1
//					newRow.createCell(26).setCellValue("");
//					//nextAction2
//					newRow.createCell(27).setCellValue("");
//					//status2
//					newRow.createCell(28).setCellValue("");
//					//nextAction3
//					newRow.createCell(29).setCellValue("");
//					//status3
//					newRow.createCell(30).setCellValue("");
//					//nextAction4
//					newRow.createCell(31).setCellValue("");
//					//status4
//					newRow.createCell(32).setCellValue("");
//					//以下、予備領域
//					newRow.createCell(33).setCellValue("");
//					newRow.createCell(34).setCellValue("");
//					newRow.createCell(35).setCellValue("");
//					newRow.createCell(36).setCellValue("");
//					newRow.createCell(37).setCellValue("");
//					newRow.createCell(38).setCellValue("");
//					newRow.createCell(39).setCellValue("");
//					newRow.createCell(40).setCellValue("");
//					newRow.createCell(41).setCellValue("");
//					newRow.createCell(42).setCellValue("");
//					newRow.createCell(43).setCellValue("");
//					newRow.createCell(44).setCellValue("");
//					newRow.createCell(45).setCellValue("");
//					newRow.createCell(46).setCellValue("");
//					newRow.createCell(47).setCellValue("");
//					newRow.createCell(48).setCellValue("");
//					newRow.createCell(49).setCellValue("");
//					newRow.createCell(50).setCellValue("");
//
//				} else {
//
//					//フェーズがinitの場合、プレイヤー２と判断してプレイヤー２の情報を更新する
//					player1Flg = false;
//
//					battleControllRow.getCell(9).setCellValue("start");
//				}
//
//				outStream = new FileOutputStream(this.battleControllFilePath);
//				battleControllBook.write(outStream);
//
//				return player1Flg;
//
//		 } finally {
//
//			 if (battleControllBook != null) {
//				 battleControllBook.close();
//			 }
//
//			 if (outStream != null) {
//				 outStream.close();
//			 }
//
//		 }
	}

//	//ファイル書き込み開始処理
//	public void openBook() throws Exception {
//
//		FileInputStream bfStream = null;
//
//
//		while (true) {
//			try {
//				Thread.sleep(500);
//
//				//読み取り専用でファイルを開く
//				bfStream = new FileInputStream(this.battleControllFilePath);
//				this.controllBook = new XSSFWorkbook(bfStream);
//				bfStream.close();
//
//				break;
//			} catch (Exception e) {
//				AodemiLogger.write("Controll ファイルが操作中のため待機...");
//				//AodemiLogger.write("Controll ファイルが操作中のため待機...");
//			}
//		}
//
//
//	}

//	//ファイル書き込み、クローズ処理
//	public void closeBook() throws Exception {
//
//		 FileOutputStream outStream = null;
//
//		try {
//			outStream = new FileOutputStream(this.battleControllFilePath);
//			this.controllBook.write(outStream);
//
//		} finally {
//
//			 if (this.controllBook != null) {
//				 this.controllBook.close();
//			 }
//
//			 if (outStream != null) {
//				 outStream.close();
//			 }
//		}
//	}

	//初期化処理
	public void initBattle(boolean player1Flg, String playerId, String deckNumber, String spcialColor, String specialId, String life) throws Exception {

		BattleControllDTO dto = dao.getAllValue(battleID);

		//それぞれのプレイヤー情報を更新する。
		if (player1Flg) {
			dto.setPlayer_id_1(playerId);
			dto.setSpecial_color_1(spcialColor);
			dto.setSpecial_id_1(specialId);
			dto.setLife_1((int)Double.parseDouble(life));
			dto.setDeck_no_1((int)Double.parseDouble(deckNumber));

//			battleControllRow.getCell(1).setCellValue(playerId);
//			battleControllRow.getCell(2).setCellValue(spcialColor);
//			battleControllRow.getCell(3).setCellValue(life);
//			battleControllRow.getCell(4).setCellValue(deckNumber);
		} else {
			dto.setPlayer_id_2(playerId);
			dto.setSpecial_color_2(spcialColor);
			dto.setSpecial_id_2(specialId);
			dto.setLife_2((int)Double.parseDouble(life));
			dto.setDeck_no_2((int)Double.parseDouble(deckNumber));

//			battleControllRow.getCell(5).setCellValue(playerId);
//			battleControllRow.getCell(6).setCellValue(spcialColor);
//			battleControllRow.getCell(7).setCellValue(life);
//			battleControllRow.getCell(8).setCellValue(deckNumber);
		}

		dao.update(dto);

//		//1シート目
//		Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//		//2行目
//		Row battleControllRow = battleControllSheet.getRow(1);
//
//		//それぞれのプレイヤー情報を更新する。
//		if (player1Flg) {
//			battleControllRow.getCell(1).setCellValue(playerId);
//			battleControllRow.getCell(2).setCellValue(spcialColor);
//			battleControllRow.getCell(3).setCellValue(life);
//			battleControllRow.getCell(4).setCellValue(deckNumber);
//		} else {
//			battleControllRow.getCell(5).setCellValue(playerId);
//			battleControllRow.getCell(6).setCellValue(spcialColor);
//			battleControllRow.getCell(7).setCellValue(life);
//			battleControllRow.getCell(8).setCellValue(deckNumber);
//		}
	}

//	//入力した値とコントロールシートの内容のデータチェック
//	public boolean cheackFileValue(String battleID, String cheakStr, String checkColumn) throws Exception {
//
//		boolean ret = false;
//
//		DaoFactory factory = new DaoFactory();
//		BattleControllDAO dao = factory.createControllDAO();
//
//		BattleControllDTO dto = dao.getAllValue(battleID);
//
//		 Workbook battleControllBook = null;
//		 FileInputStream bfStream = null;
//		 FileOutputStream outStream = null;
//
//		boolean ret = false;
//
//		 try {
//			bfStream = new FileInputStream(this.battleControllFilePath);
//			 while (true) {
//				 try {
//					 Thread.sleep(500);
//
//					 //読み取り専用でファイルを開く
//					 battleControllBook = new XSSFWorkbook(bfStream);
//					 break;
//				 } catch (Exception e) {
//					 AodemiLogger.write("Controll ファイルが操作中のため待機...");
//				 }
//			 }
//	        bfStream.close();
//
//			//1シート目
//			Sheet battleControllSheet = battleControllBook.getSheetAt(0);
//			//2行目
//			Row battleControllRow = battleControllSheet.getRow(1);
//
//			//入力した値をチェックしたい値が同じ場合はtrue、それ以外はfalse
//			if (battleControllRow.getCell(checkNumber).getStringCellValue() == null) {
//				ret = true;
//			} else if (cheakStr.equals(battleControllRow.getCell(checkNumber).getStringCellValue())) {
//				ret = true;
//			}
//		 } finally {
//
//			 if (battleControllBook != null) {
//				 battleControllBook.close();
//			 }
//
//			 if (outStream != null) {
//				 outStream.close();
//			 }
//
//		 }
//
//		 return ret;
//
//	}

	//コントロールシートの全ての値の取得
	public BattleControllDTO getAllValue(String battleID) throws Exception {

		BattleControllDTO dto = dao.getAllValue(battleID);

		return dto;

//		//1シート目
//		Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//		//2行目
//		Row battleControllRow = battleControllSheet.getRow(1);
//
//		ret.put("battleID", battleControllRow.getCell(0).getStringCellValue());
//		ret.put("player1", battleControllRow.getCell(1).getStringCellValue());
//		ret.put("specialColor1", battleControllRow.getCell(2).getStringCellValue());
//		ret.put("life1", battleControllRow.getCell(3).getStringCellValue());
//		ret.put("deckNumber1", battleControllRow.getCell(4).getStringCellValue());
//		ret.put("player2", battleControllRow.getCell(5).getStringCellValue());
//		ret.put("specialColor2", battleControllRow.getCell(6).getStringCellValue());
//		ret.put("life2", battleControllRow.getCell(7).getStringCellValue());
//		ret.put("deckNumber2", battleControllRow.getCell(8).getStringCellValue());
//		ret.put("phase", battleControllRow.getCell(9).getStringCellValue());
//		ret.put("status1", battleControllRow.getCell(10).getStringCellValue());
//		ret.put("status2", battleControllRow.getCell(11).getStringCellValue());
//		ret.put("out", battleControllRow.getCell(12).getStringCellValue());
//		//ret.put("nextAction1", battleControllRow.getCell(13).getStringCellValue());
//		//ret.put("nextAction2", battleControllRow.getCell(14).getStringCellValue());
//		ret.put("playerInput1", battleControllRow.getCell(15).getStringCellValue());
//		ret.put("playerInput2", battleControllRow.getCell(16).getStringCellValue());
//		ret.put("playerOutput1", battleControllRow.getCell(17).getStringCellValue());
//		ret.put("playerOutput2", battleControllRow.getCell(18).getStringCellValue());
//		ret.put("nextAction", battleControllRow.getCell(19).getStringCellValue());
//		ret.put("playerRevival1", battleControllRow.getCell(20).getStringCellValue());
//		ret.put("playerRevival2", battleControllRow.getCell(21).getStringCellValue());
//		ret.put("playerSurrend1", battleControllRow.getCell(22).getStringCellValue());
//		ret.put("playerSurrend2", battleControllRow.getCell(23).getStringCellValue());
//		ret.put("turnCount", battleControllRow.getCell(24).getStringCellValue());
//		ret.put("nextAction1", battleControllRow.getCell(25).getStringCellValue());
//		ret.put("status1", battleControllRow.getCell(26).getStringCellValue());
//		ret.put("nextAction2", battleControllRow.getCell(27).getStringCellValue());
//		ret.put("status2", battleControllRow.getCell(28).getStringCellValue());
//		ret.put("nextAction3", battleControllRow.getCell(29).getStringCellValue());
//		ret.put("status3", battleControllRow.getCell(30).getStringCellValue());
//		ret.put("nextAction4", battleControllRow.getCell(31).getStringCellValue());
//		ret.put("status4", battleControllRow.getCell(32).getStringCellValue());
//
//		return ret;
	}

//	//セットフェーズ用初期化処理
//	public void setInit() throws Exception {
//
//		DaoFactory factory = new DaoFactory();
//		BattleControllDAO dao = factory.createControllDAO();
//
//		BattleControllDTO dto = dao.getAllValue(battleID);
//		dto.setPhase("set");
//		dto.setStatus_1("ready");
//		dto.setStatus_2("ready");
//		dto.setTurncount(dto.getTurncount() + 1);
//
//		dao.update(dto);
//
//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//		//それぞれのプレイヤー情報を更新する。
//		battleControllRow.getCell(9).setCellValue("set");
//		battleControllRow.getCell(10).setCellValue("ready");
//		battleControllRow.getCell(11).setCellValue("ready");
//		//ターン数を1増やす
//		BigDecimal turnCount = new BigDecimal(battleControllRow.getCell(24).toString());
//		turnCount = turnCount.add(BigDecimal.ONE);
//		battleControllRow.getCell(24).setCellValue(turnCount.toString());
//
//	}

//	//remove用の初期化処理
//	public void removeInit(BattleControllDTO controllDto) throws Exception {
//
//		controllDto.setPhase("remove");
//		controllDto.setStatus_1("ready");
//		controllDto.setStatus_2("ready");
//
////        //1シート目
////        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
////        //2行目
////        Row battleControllRow = battleControllSheet.getRow(1);
////
////		//それぞれのプレイヤー情報を更新する。
////		battleControllRow.getCell(9).setCellValue("remove");
////		battleControllRow.getCell(10).setCellValue("ready");
////		battleControllRow.getCell(11).setCellValue("ready");
//	}

//	//battle用の初期化処理
//	public void battleInit() throws Exception {
//
//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//		//それぞれのプレイヤー情報を更新する。
//		battleControllRow.getCell(9).setCellValue("battle");
//		battleControllRow.getCell(10).setCellValue("ready");
//		battleControllRow.getCell(11).setCellValue("ready");
//	}
//
//	//open用の初期化処理
//	public void openInit() throws Exception {
//
//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//		//それぞれのプレイヤー情報を更新する。
//        battleControllRow.getCell(9).setCellValue("open");
//		battleControllRow.getCell(10).setCellValue("ready");
//		battleControllRow.getCell(11).setCellValue("ready");
//	}

//	//コントロールシートの指定位置の値を更新
//	public void updateStatus(boolean player1Flg, String end) throws Exception {
//
//		DaoFactory factory = new DaoFactory();
//		BattleControllDAO dao = factory.createControllDAO();
//
//		BattleControllDTO dto = dao.getAllValue(battleID);
//		dto.setPhase("set");
//		dto.setStatus_1("ready");
//		dto.setStatus_2("ready");
//		dto.setTurncount(dto.getTurncount() + 1);
//
//		dao.update(dto);
//
////        //1シート目
////        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
////        //2行目
////        Row battleControllRow = battleControllSheet.getRow(1);
////
////		//それぞれのプレイヤー情報を更新する。
////		battleControllRow.getCell(index).setCellValue(value);
//
//	}
/*
	//コントロールシートの指定位置の値を更新(WorkBook指定)
	public void updateValue(Workbook battleControllBook, String value, int index) throws Exception {

        //1シート目
        Sheet battleControllSheet = battleControllBook.getSheetAt(0);
        //2行目
        Row battleControllRow = battleControllSheet.getRow(1);

		//それぞれのプレイヤー情報を更新する。
		battleControllRow.getCell(index).setCellValue(value);

	}
*/
	//コントロールシートのプレイヤー入力を初期化(セット用)
	public void initPlayerSetInput(BattleControllDTO controllDTO) throws Exception {

		controllDTO.setOut("");
		controllDTO.setNext_action("");
		controllDTO.setPlayer_input_1("");
		controllDTO.setPlayer_input_2("");
		controllDTO.setPlayer_output_1("");
		controllDTO.setPlayer_output_2("");
		controllDTO.setPlayer_revival_1("");
		controllDTO.setPlayer_revival_2("");


//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//		//一時入力情報を初期化
//        battleControllRow.getCell(12).setCellValue("");
//        //battleControllRow.getCell(13).setCellValue("");
//        //battleControllRow.getCell(14).setCellValue("");
//		battleControllRow.getCell(15).setCellValue("");
//		battleControllRow.getCell(16).setCellValue("");
//		battleControllRow.getCell(17).setCellValue("");
//		battleControllRow.getCell(18).setCellValue("");
//		battleControllRow.getCell(19).setCellValue("");
//		battleControllRow.getCell(20).setCellValue("");
//		battleControllRow.getCell(21).setCellValue("");
////		battleControllRow.getCell(25).setCellValue("");
////		battleControllRow.getCell(26).setCellValue("");
////		battleControllRow.getCell(27).setCellValue("");
////		battleControllRow.getCell(28).setCellValue("");
////		battleControllRow.getCell(29).setCellValue("");
////		battleControllRow.getCell(30).setCellValue("");
////		battleControllRow.getCell(31).setCellValue("");
////		battleControllRow.getCell(32).setCellValue("");

	}

	//コントロールシートのプレイヤー入力を初期化
	public void initPlayerInput(BattleControllDTO controllDTO) throws Exception {

//		DaoFactory factory = new DaoFactory();
//		BattleControllDAO dao = factory.createControllDAO();
//
//		BattleControllDTO dto = dao.getAllValue(battleID);
		controllDTO.setOut("");
		controllDTO.setNext_action("");
		controllDTO.setPlayer_input_1("");
		controllDTO.setPlayer_input_2("");
		controllDTO.setPlayer_output_1("");
		controllDTO.setPlayer_output_2("");
		controllDTO.setPlayer_revival_1("");
		controllDTO.setPlayer_revival_2("");
		controllDTO.setNext_special_player_1("");
		controllDTO.setNext_special_player_2("");
		controllDTO.setNext_special_status_1("");
		controllDTO.setNext_special_status_2("");
		controllDTO.setNext_open_player_1("");
		controllDTO.setNext_open_player_2("");
		controllDTO.setNext_open_status_1("");
		controllDTO.setNext_open_status_2("");

//		dao.update(dto);

//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//		//一時入力情報を初期化
//        battleControllRow.getCell(12).setCellValue("");
//        battleControllRow.getCell(13).setCellValue("");
//        battleControllRow.getCell(14).setCellValue("");
//		battleControllRow.getCell(15).setCellValue("");
//		battleControllRow.getCell(16).setCellValue("");
//		battleControllRow.getCell(17).setCellValue("");
//		battleControllRow.getCell(18).setCellValue("");
//		battleControllRow.getCell(19).setCellValue("");
//		battleControllRow.getCell(20).setCellValue("");
//		battleControllRow.getCell(21).setCellValue("");
//		battleControllRow.getCell(25).setCellValue("");
//		battleControllRow.getCell(26).setCellValue("");
//		battleControllRow.getCell(27).setCellValue("");
//		battleControllRow.getCell(28).setCellValue("");
//		battleControllRow.getCell(29).setCellValue("");
//		battleControllRow.getCell(30).setCellValue("");
//		battleControllRow.getCell(31).setCellValue("");
//		battleControllRow.getCell(32).setCellValue("");
	}

//	//奥義の行動順を設定する
//	public void updateSpecialCardInfo(String value1, String value2, String status1, String status2) throws Exception {
//
//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//        //各プレイヤーのステータスとＩＤを設定
//		battleControllRow.getCell(25).setCellValue(value1);
//		battleControllRow.getCell(26).setCellValue(status1);
//		battleControllRow.getCell(27).setCellValue(value2);
//		battleControllRow.getCell(28).setCellValue(status2);
//	}
//
//	//セットカードの行動順を設定する
//	public void updateSetCardInfo(String value1, String value2, String status1, String status2) throws Exception {
//
//        //1シート目
//        Sheet battleControllSheet = this.controllBook.getSheetAt(0);
//        //2行目
//        Row battleControllRow = battleControllSheet.getRow(1);
//
//        //各プレイヤーのステータスとＩＤを設定
//		battleControllRow.getCell(29).setCellValue(value1);
//		battleControllRow.getCell(30).setCellValue(status1);
//		battleControllRow.getCell(31).setCellValue(value2);
//		battleControllRow.getCell(32).setCellValue(status2);
//
//	}

	//オープンの優先順位計算
	public String nextOpenAction(BattleControllDTO controllDto, Card card1, Card card2, String playerId1, String playerId2 ) throws Exception {
		String ret = "";
		StringUtil util = new StringUtil();
		String player1 = "";
    	String player2 = "";
    	String status1 = "end";
    	String status2 = "end";

    	if (card1 != null && (card1.getOpenSkillName() == null || "".equals(card1.getOpenSkillName()))) {
    		card1 = null;
    	}

    	if (card2 != null && (card2.getOpenSkillName() == null || "".equals(card2.getOpenSkillName()))) {
    		card2 = null;
    	}

    	//オープンスキルの判定
		if (card1 == null && card2 == null) {
			//どちらもノーセットの場合はendを設定
			ret = "end";
			player1 = "end";
			player2 = "end";
			status1 = "end";
			status2 = "end";

		} else if (card1 == null) {
			//カード２のみオープンがある
			ret = playerId2;

			player1 = playerId2;
			status1 = "ready";

			player2 = "end";
			status2 = "end";


		} else if (card2 == null) {

			//カード１のみオープンがある
			ret = playerId1;

			player1 = playerId1;
			status1 = "ready";

			player2 = "end";
			status2 = "end";

		} else {
			//両方のカードのタイプを判定
			String type1 = card1.getType();
			String type2 = card2.getType();

			if (UtilConst.CARD_TYPE_MAGIC.equals(type1) && UtilConst.CARD_TYPE_MAGIC.equals(type2)) {

				//両方魔法の場合
				//ランダムの０，１を生成して抽選
				Random rand = new Random();
			    int num = rand.nextInt(2);

			    if (num == 0) {
			    	ret = playerId1;

			    	player1 = playerId1;
			    	status1 = "ready";

			    	player2 = playerId2;
			    	status2= "ready";

			    } else {
			    	ret = playerId2;

			    	player1 = playerId2;
			    	status1= "ready";

			    	player2 = playerId1;
			    	status2 = "ready";

			    }

			} else if (!UtilConst.CARD_TYPE_MAGIC.equals(type1) && !UtilConst.CARD_TYPE_MAGIC.equals(type2)) {
			    //どちらも魔法ではない

//				//オープンスキルの有無をチェック。片方のみオープンがある場合、オープンがある方優先
//				if (util.checkNull(card1.getOpenSkillName()) && util.checkNull(card2.getOpenSkillName())) {
//					//どちらもopenなし
//					ret = "end";
//
//					player1 = "end";
//					player2 = "end";
//					status1 = "end";
//					status2 = "end";
//
//				} else if (util.checkNull(card1.getOpenSkillName()) && !util.checkNull(card2.getOpenSkillName())) {
//
//					ret = playerId2;
//
//					player1 = playerId2;
//					status1= "ready";
//
//					player2 = "end";
//					status2 = "end";
//
//				} else if (!util.checkNull(card1.getOpenSkillName()) && util.checkNull(card2.getOpenSkillName())) {
//
//					ret = playerId1;
//
//					player1 = playerId1;
//					status1 = "ready";
//
//					player2 = "end";
//					status2 = "end";
//
//				} else

				if (card1.getAgi().equals(card2.getAgi())) {
					//両方にオープンがあり、速度が同じ場合、ランダム
					//ランダムの０，１を生成して抽選
					Random rand = new Random();
				    int num = rand.nextInt(2);

				    if (num == 0) {
				    	ret = playerId1;

				    	player1 = playerId1;
				    	status1 = "ready";

				    	player2 = playerId2;
				    	status2= "ready";

				    } else {
				    	ret = playerId2;

				    	player1 = playerId2;
				    	status1= "ready";

				    	player2 = playerId1;
				    	status2 = "ready";

				    }
			    } else {
			    	//両方にオープンがあり、速度に差があるなら速度が早い方
			    	if (card1.getAgi().compareTo(card2.getAgi()) > 0) {
			    		ret = playerId1;

			    		player1 = playerId1;
			    		status1 = "ready";

			    		player2 = playerId2;
			    		status2= "ready";

			    	} else {
			    		ret = playerId2;

			    		player1 = playerId2;
			    		status1= "ready";

			    		player2 = playerId1;
			    		status2 = "ready";
			    	}
			    }

			} else {
				//どちらかが魔法の場合、魔法優先
				if (UtilConst.CARD_TYPE_MAGIC.equals(type1)) {
					ret = playerId1;

					player1 = playerId1;
					status1 = "ready";

					player2 = playerId2;
					status2 = "ready";


				} else {
					ret = playerId2;

					player1 = playerId2;
					status1= "ready";

					player2 = playerId1;
					status2 = "ready";
				}
			}
		}

		controllDto.setNext_open_player_1(player1);
		controllDto.setNext_open_player_2(player2);
		controllDto.setNext_open_status_1(status1);
		controllDto.setNext_open_status_2(status2);

		return ret;

	}

	//奥義の優先順位計算
	public Boolean nextSpecialAction(BattleControllDTO controllDto, BattleFieldUtil battleFieldUtil, String playerId1, String playerId2 ) throws Exception {
		Boolean ret = null;
		String player1 = "";
    	String player2 = "";
    	String status1 = "end";
    	String status2 = "end";

    	BattleBaseDTO baseDto = battleFieldUtil.getBaseValue(playerId1);
    	BattleBaseDTO enemyBaseDto = battleFieldUtil.getBaseValue(playerId2);

    	//まず奥義の判定。奥義が使用されていた場合は１と２に奥義のユーザーＩＤを追加
    	int mySpecial = baseDto.getSpecial_use();
    	int enemySpecial = enemyBaseDto.getSpecial_use();

    	if (mySpecial == 0 && enemySpecial == 0) {
    		player1 = "end";
    		status1 = "end";
    		player2 = "end";
    		status2 = "end";
    	} else if (mySpecial == 1 && enemySpecial == 0) {
    		ret = true;
    		player1 = playerId1;
    		status1 = "ready";
    		player2 = "end";
    		status2 = "end";

    		//奥義利用フラグを利用済に更新
    		baseDto.setSpecial_use(2);

    	} else if (mySpecial == 0 && enemySpecial == 1) {
    		ret = false;
    		player1 = playerId2;
    		status1 = "ready";
    		player2 = "end";
    		status2 = "end";

    		//奥義利用フラグを利用済に更新
    		enemyBaseDto.setSpecial_use(2);

    	} else if (mySpecial == 1 && enemySpecial == 1) {
    		//同時に起動した場合は優先順位を決める

    		Random rand = new Random();
		    int num = rand.nextInt(2);

		    if (num == 0) {
		    	//1優先
		    	ret = true;
	    		player1 = playerId1;
	    		status1 = "ready";
	    		player2 = playerId2;
	    		status2 = "ready";
		    } else {
		    	//2優先
		    	ret = false;
	    		player1 = playerId2;
	    		status1 = "ready";
	    		player2 = playerId1;
	    		status2 = "ready";
		    }

		    baseDto.setSpecial_use(2);
		    enemyBaseDto.setSpecial_use(2);
    	}

    	BattleBaseDAO baseDao = factory.createBaseDAO();
    	baseDao.update(baseDto);
    	baseDao.update(enemyBaseDto);

    	controllDto.setNext_special_player_1(player1);
    	controllDto.setNext_special_player_2(player2);
    	controllDto.setNext_special_status_1(status1);
    	controllDto.setNext_special_status_2(status2);

		return ret;

	}

	//スタートスキルの優先順位計算
	public HashMap<String, Object> nextStartAction(BattleControllDTO controllDto, BattleFieldUtil battleFieldUtil) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

    	DaoFactory factory = new DaoFactory();
    	BattleFieldDAO fieldDao = factory.createFieldDAO();

    	//盤面の情報を取得
    	ArrayList<BattleFieldDTO> myFieldList = fieldDao.getAllList(battleID, controllDto.getPlayer_id_1());

    	int maxAgi = 0;
    	ArrayList<ArrayList<Object>> actionList = new ArrayList<ArrayList<Object>>();

    	//盤面で一番速度が高いユニットを取得
		 for (int i = 0; i < myFieldList.size(); i++) {

			 BattleFieldDTO dto = myFieldList.get(i);

			 //クローズしておらず、スタートが行動済でもない場合
			 if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && dto.getStart_action() == 0) {
				 ArrayList<Object> koList = new ArrayList<Object>();

				 //速度
				 int sumAgi = dto.getCur_speed() + dto.getTurn_speed() + dto.getPermanent_speed();

				 if (maxAgi < sumAgi) {
					 maxAgi = sumAgi;
					 //最大速度が変わった場合は対象配列を初期化
					 actionList = new ArrayList<ArrayList<Object>>();
				 } else if (maxAgi == sumAgi) {
					 //最大速度と同じ場合は配列に追加
				 } else {
					 //速度が低い場合は対象としない
					 continue;
				 }

				 koList.add(sumAgi);

				 //プレイヤーID
				 koList.add(controllDto.getPlayer_id_1());

				 //盤面位置
				 koList.add(i);
				 //カードＩＤ
				 koList.add(dto.getCard_id());
				 actionList.add(koList);
			 }
		 }

		 ArrayList<BattleFieldDTO> player2List = fieldDao.getAllList(battleID, controllDto.getPlayer_id_2());

		 for (int i = 0; i < player2List.size(); i++) {

			BattleFieldDTO dto = player2List.get(i);

			//クローズしておらず、行動済でもない場合
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && dto.getStart_action() == 0) {
				 ArrayList<Object> koList = new ArrayList<Object>();
				 //速度
				 int sumAgi = dto.getCur_speed() + dto.getTurn_speed() + dto.getPermanent_speed();

				 if (maxAgi < sumAgi) {
					 maxAgi = sumAgi;
					 //最大速度が変わった場合は対象配列を初期化
					 actionList = new ArrayList<ArrayList<Object>>();
				 } else if (maxAgi == sumAgi) {
					 //最大速度と同じ場合は配列に追加
				 } else {
					 //速度が低い場合は対象としない
					 continue;
				 }

				 koList.add(sumAgi);

				 //プレイヤーID
				 koList.add(controllDto.getPlayer_id_2());

				 //盤面位置
				 koList.add(i);
				 //カードＩＤ
				 koList.add(dto.getCard_id());
				 actionList.add(koList);
			 }
		 }

		 //候補が居ない場合はendを返却
		 if (actionList.size() == 0) {
			 ret.put("playerId", "end");
			 ret.put("fieldNumber", null);
		 } else {
			//最大速度の一覧が作成されたので、その中からランダムで一つ選ぶ
			Random rand = new Random();
			int num = rand.nextInt(actionList.size());

			//次の行動が決定したらスタートスキルを呼び出す
			AbilityFactory open = new AbilityFactory();
			CardAbility cardAbility = open.getCardAbility(actionList.get(num).get(3).toString());

			HashMap<String, Object> startMap = cardAbility.start(battleID, actionList.get(num).get(1).toString(), Integer.parseInt(actionList.get(num).get(2).toString()));
			ret.put("updateInfo", startMap.get("updateInfo"));
			ret.put("target", startMap.get("target"));

			ret.put("playerId", actionList.get(num).get(1));
			ret.put("fieldNumber", actionList.get(num).get(2));

			//スタートスキルを行動済に更新しておく
			BattleFieldDTO dto = fieldDao.getAllValue(battleID, actionList.get(num).get(1).toString(), Integer.parseInt(actionList.get(num).get(2).toString()));
			dto.setStart_action(1);
			fieldDao.update(dto);
		}

		return ret;

	}

	//クローズの順番計算
	public HashMap<String, Object> nextClose(BattleControllDTO controllDto, BattleFieldUtil battleFieldUtil) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
    	BattleFieldDAO fieldDao = factory.createFieldDAO();

    	//盤面の情報を取得
    	ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, controllDto.getPlayer_id_1());


		return ret;

	}

	//自分が敗北しているかのチェック
	public boolean cheackMyWinLose(boolean player1Flg, BattleControllDTO controllDto) {
		boolean ret = false;

		if (player1Flg) {

			if (controllDto.getPlayer_surrend_1() == 1) {
				ret = true;
			}
		} else {

			if (controllDto.getPlayer_surrend_2() == 1) {
				ret = true;
			}
		}

		return ret;

	}

	//相手が敗北しているかのチェック
	public boolean cheackEnemyWinLose(boolean player1Flg) throws Exception {

		BattleControllDTO dto = dao.getAllValue(battleID);

		boolean ret = false;

		if (player1Flg) {

			if (dto.getPlayer_surrend_2() == 1) {
				ret = true;
			}
		} else {

			if (dto.getPlayer_surrend_1() == 1) {
				ret = true;
			}
		}

		return ret;

	}

}
