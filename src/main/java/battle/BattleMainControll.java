package battle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import card.CardAbility;
import cardDataObject.Card;
import cardDataObject.Deck;
import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.AbilityFactory;
import factory.DaoFactory;
import shield.ShieldAbility;
import special.SpecialAbility;
import util.AodemiLogger;
import util.BattleControllUtil;
import util.BattleFieldUtil;
import util.CardUtil;
import util.StringUtil;
import util.UtilConst;

public class BattleMainControll {

	String outputFile = "";
	String playerPath = "";
	boolean player1Flg = true;
	String deckNumber = "1";
	String battleID = "T0001";
	DaoFactory factory = new DaoFactory();
	BattleControllDAO controllDao = factory.createControllDAO();
	BattleBaseDAO baseDao = factory.createBaseDAO();
	BattleFieldDAO fieldDao = factory.createFieldDAO();
	BattleDeckDAO deckDao = factory.createDeckDAO();

	AodemiLogger logger = null;

	public BattleMainControll(String inputFilePath, boolean player1Flg) throws SecurityException, IOException {

		this.player1Flg = player1Flg;
		logger = AodemiLogger.getInstance(this.battleID, "", player1Flg);

		if (player1Flg) {
    		this.playerPath = inputFilePath + "プレイヤー１\\";
    		System.out.println("こちらはプレイヤー１です");
    	} else {
    		this.playerPath = inputFilePath + "プレイヤー２\\";
    		System.out.println("こちらはプレイヤー２です");
    	}

		this.outputFile = this.playerPath + "output.json";

	}

	public void init(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception {

		String playerId = "";
		String deckNumber = "";

    	playerId = map.get("playerId").toString();
    	deckNumber = map.get("deckNumber").toString();

    	logger.setPlayerId(playerId);

		AodemiLogger.write("initを開始します");
		AodemiLogger.write("プレイヤーＩＤ：" + playerId);

    	HashMap<String, Object> outMap = new HashMap<String, Object>();

    	//最初に初期データの確認と登録を行う

    	if (player1Flg) {

    		BattleControllDTO dto = battleControllUtil.getAllValue(this.battleID);

    		AodemiLogger.write("相手プレイヤーの入力を待機中");

		   	//相手プレイヤーIDが設定されるまで待つ
		   	if ("".equals(dto.getPlayer_id_2())) {

		   		//相手情報が未入力の場合、入力されるまで待つ
		   		while ("".equals(dto.getPlayer_id_2())) {
		   			dto = battleControllUtil.getAllValue(this.battleID);
		       		//１秒停止
		       		Thread.sleep(500);
		       	}
		   	}
    	}

    	//デッキ情報の取得
    	Deck deck = cardUtil.getDeck(playerId, deckNumber, playerPath);

    	//戦闘状況を初期化する
    	battleControllUtil.initBattle(player1Flg, playerId, deckNumber, deck.getSpecial().getColor(), deck.getSpecial().getId(), deck.getLife());

    	//盤面情報を初期化する
    	battleFieldUtil.initBattleBase(player1Flg, playerId);
    	battleFieldUtil.initBattleField(player1Flg, playerId);
    	battleFieldUtil.initBattleShield(player1Flg, playerId, deck);
    	battleFieldUtil.initBattleDeck(player1Flg, playerId, deck);

        //プレイヤー１の場合
        if (player1Flg) {
        	BattleControllDTO dto = battleControllUtil.getAllValue(this.battleID);

    		//相手が入力が完了したら返却値を作成する
        	ArrayList<String> idList = new ArrayList<String>();
        	for (int i = 0; i < deck.getCardList().size(); i++) {
        		idList.add(deck.getCardList().get(i).getId());
        	}
    		outMap.put("deckId", idList);

    		ArrayList<String> shieldList = new ArrayList<String>();
    		for (int i = 0; i < deck.getShieldList().size(); i++) {
    			shieldList.add(deck.getShieldList().get(i).getId());
        	}
    		outMap.put("shieldId", shieldList);
    		outMap.put("specialId", dto.getSpecial_id_1());
    		outMap.put("enemyPlayerId", dto.getPlayer_id_2());
    		outMap.put("enemyLife", dto.getLife_2());
    		outMap.put("enemySpecialColor", dto.getSpecial_color_2());

        	stringUtil.serializeJson(outMap, outputFile);

        } else {

        	BattleControllDTO dto = battleControllUtil.getAllValue(this.battleID);

        	//相手プレイヤーIDが設定されるまで待つ
		   	if ("".equals(dto.getPlayer_id_1())) {

		   		AodemiLogger.write("相手プレイヤーの入力を待機中");

		   		//相手情報が未入力の場合、入力されるまで待つ
		   		while ("".equals(dto.getPlayer_id_1())) {
		   			dto = battleControllUtil.getAllValue(this.battleID);
		       		//１秒停止
		       		Thread.sleep(500);
		       	}
		   	}

    		//相手が入力が完了したら返却値を作成する
        	ArrayList<String> idList = new ArrayList<String>();
        	for (int i = 0; i < deck.getCardList().size(); i++) {
        		idList.add(deck.getCardList().get(i).getId());
        	}
    		outMap.put("deckId", idList);

    		ArrayList<String> shieldList = new ArrayList<String>();
    		for (int i = 0; i < deck.getShieldList().size(); i++) {
    			shieldList.add(deck.getShieldList().get(i).getId());
        	}
    		outMap.put("shieldId", shieldList);
    		outMap.put("specialId", dto.getSpecial_id_2());
    		outMap.put("enemyPlayerId", dto.getPlayer_id_1());
    		outMap.put("enemyLife", dto.getLife_1());
    		outMap.put("enemySpecialColor", dto.getSpecial_color_1());

        	stringUtil.serializeJson(outMap, outputFile);
        }

        AodemiLogger.write("initを処理しました");
	}

	public void set(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception {

		String playerId = map.get("playerId").toString();
//		logger = new AodemiLogger(this.battleID, playerId, player1Flg);

		String setPositon = "";

    	String yellowMana = map.get("yellowMana").toString();
    	String blackMana = map.get("blackMana").toString();
    	String redMana = map.get("redMana").toString();
    	String blueMana = map.get("blueMana").toString();
    	String specialUse =  map.get("specialUse").toString();

    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	String enemyCardId = "";
    	String nextAction = "";
    	Boolean nextSpecialFlg = null;
    	String nextSpecialAction = null;
    	HashMap<String, Object> updateInfo = new HashMap<String, Object>();
    	String specialId = "";

    	BattleControllDTO controllDTO = battleControllUtil.getAllValue(this.battleID);

    	AodemiLogger.write("set読み込み開始");

    	if (player1Flg) {
    		AodemiLogger.write("プレイヤー２を待っています");

    		//まず相手の入力を待つ
    		while (!"wait".equals(controllDTO.getNext_action())) {
    			controllDTO = battleControllUtil.getAllValue(this.battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    	}

    	//一時情報の初期化
    	battleFieldUtil.initTurnStartInfo(playerId, cardUtil);

    	/**
    	*
    	* セットカードのフィールド配置
    	*
    	*/

    	BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

    	//配置情報がある場合は配置処理を行う
    	if (map.get("setPosition") != null && !"".equals(map.get("setPosition").toString())) {
    		setPositon = map.get("setPosition").toString();

    		//自分のセットカードを読み取り、盤面情報の指定位置にセットカードを書き込む
			String setCardId = baseDto.getSet_card();
			int setCardNumber = baseDto.getSet_deck_no();
			Card cardInfo = cardUtil.getCard(setCardId);

			battleFieldUtil.updateSetCard(playerId, cardInfo, Integer.parseInt(setPositon), setCardNumber);
//			controllDTO = battleControllUtil.getAllValue(this.battleID);

			if (player1Flg) {
				//自分のセット位置情報を記入する
				controllDTO.setPlayer_output_1(setPositon);
			} else {
				//自分のセット位置情報を記入する
				controllDTO.setPlayer_output_2(setPositon);
			}

			controllDao.update(controllDTO);
    	}

    	/**
	  	*
	  	* 復活情報の設定
	  	*
	  	*/

    	ArrayList<Boolean> revivalNumber = null;
    	if (map.get("revivalNumber") == null) {
    		revivalNumber = null;
    	} else {
    		revivalNumber = (ArrayList<Boolean>)map.get("revivalNumber");
    	}

    	//自分の復活情報を書き込む
    	String revivalStr = stringUtil.getJsonStr(revivalNumber);

    	controllDTO = battleControllUtil.getAllValue(this.battleID);

		if (player1Flg) {
			//自分のセット位置情報を記入する
			controllDTO.setPlayer_revival_1(revivalStr);
		} else {
			//自分のセット位置情報を記入する
			controllDTO.setPlayer_revival_2(revivalStr);
		}

		//復活位置から戦闘状況シートを更新
    	battleFieldUtil.revial(playerId, revivalNumber, cardUtil, baseDto);

    	/**
	  	*
	  	* セットカードの情報設定
	  	*
	  	*/

    	Object setCardNumber = map.get("setCard");
    	if (map.get("setCard") == null || "99".equals(setCardNumber.toString())) {
    		setCardNumber = null;
    	} else {
    		setCardNumber = map.get("setCard").toString();
    	}

    	/**
	  	*
	  	* 属性情報の設定
	  	*
	  	*/

		//属性情報を増加させる
    	baseDto.setYellow(baseDto.getYellow() + stringUtil.getIntForString(yellowMana));
    	baseDto.setBlack(baseDto.getBlack() + stringUtil.getIntForString(blackMana));
    	baseDto.setRed(baseDto.getRed() + stringUtil.getIntForString(redMana));
    	baseDto.setBlue(baseDto.getBlue() + stringUtil.getIntForString(blueMana));

    	/**
	  	*
	  	* 奥義の設定
	  	*
	  	*/

		//奥義ゲージ、奥義ストックを増加させる
		baseDto.setSpecial_gage(baseDto.getSpecial_gage() + 5);

		if (baseDto.getSpecial_gage() >= 20) {
			baseDto.setSpecial_gage(baseDto.getSpecial_gage() - 20);
			baseDto.setSpecial_stock(baseDto.getSpecial_stock() + 1);

			if (baseDto.getSpecial_stock() > 5) {
				baseDto.setSpecial_stock(5);
			}
		}

		//自分の奥義の利用有無を設定
		if ("1".equals(specialUse)) {
			baseDto.setSpecial_use(1);
		}

    	/**
	  	*
	  	* SPの計算
	  	*
	  	*/

		baseDto.setSp(baseDto.getSp()
				+ baseDto.getTurn_up_sp()
				- stringUtil.getIntForString(yellowMana)
				- stringUtil.getIntForString(blackMana)
				- stringUtil.getIntForString(redMana)
				- stringUtil.getIntForString(blueMana));

		//次の増加SPは計算に入れたら元に戻しておく
		baseDto.setTurn_up_sp(2);

    	//復活で使ったSPを引く
//    	int revivalSp = 0;
//    	for (int i = 0; i < revivalNumber.size(); i++) {
//    		if (revivalNumber.get(i)) {
//    			revivalSp = revivalSp + 1;
//    		}
//    	}
//
//    	if (revivalSp != 0) {
//    		baseDto.setSp(baseDto.getSp() - revivalSp);
//    	}

    	//カードのレベルをSPから引く
    	Deck deck = cardUtil.getDeck(playerId, deckNumber, playerPath);
    	Card mySetCard = null;

    	//セットカードの書き込み、レベルでSPを減算
    	if (setCardNumber != null) {
    		mySetCard = deck.getCardList().get(Integer.parseInt(setCardNumber.toString()));
    		baseDto.setSp(baseDto.getSp() - stringUtil.getIntForString(mySetCard.getLevel()));
    		//自分のセットカードを記入
    		baseDto.setSet_card(mySetCard.getId());
    		baseDto.setSet_deck_no(stringUtil.getIntForString(setCardNumber.toString()));

			//実行したカードが魔法だった場合、墓地に追加する
			Card card = cardUtil.getCard(mySetCard.getId());
			if (UtilConst.CARD_TYPE_MAGIC.equals(card.getType())) {
				baseDto.setCemetery(stringUtil.addCardNumberForCamma(baseDto.getCemetery(), baseDto.getSet_deck_no()));
			}

			//利用したカードをデッキから利用済にする
			BattleDeckDTO deckDto =  deckDao.getAllValue(battleID, playerId, baseDto.getSet_deck_no());
			deckDto.setCard_out(1);
			deckDao.update(deckDto);

    	} else {
    		//セットが無い場合は空白を設定する。
    		baseDto.setSet_card("");
    		baseDto.setSet_deck_no(0);
    	}

    	//基本情報を書き込み
    	baseDao.update(baseDto);

    	/**
	  	*
	  	* セットカードの順序計算
	  	*
	  	*/

    	BattleBaseDTO enemyBaseDto = new BattleBaseDTO();
    	ArrayList<Object> target = new ArrayList<Object>();
    	boolean battingFlg = false;

		//親はプレイヤー１固定
		if (player1Flg) {

			enemyBaseDto = baseDao.getAllValue(battleID, controllDTO.getPlayer_id_2());
			enemyCardId = enemyBaseDto.getSet_card();

    		Card enemyCard = null;

    		if (!stringUtil.checkNull(enemyCardId)) {
    			enemyCard = cardUtil.getCard(enemyCardId);
        	}

    		//相手のIDを取得する
    		outMap.put("enemySetPosition", controllDTO.getPlayer_output_2());

    		//奥義利用チェック
    		nextSpecialFlg = battleControllUtil.nextSpecialAction(
    				controllDTO
    				, battleFieldUtil
    				, playerId
    				, controllDTO.getPlayer_id_2());

        	//バッティングフラグの判定
        	if (mySetCard != null && mySetCard.getId().equals(enemyBaseDto.getSet_card())) {
        		battingFlg = true;
        		nextAction = "end";

        		//バッティングしたカードを墓地へ送る。魔法カードの場合は直前の処理で既に墓地にあるので対象外
        		Card card1 = cardUtil.getCard(mySetCard.getId());
    			if (!UtilConst.CARD_TYPE_MAGIC.equals(card1.getType())) {
    				baseDto.setCemetery(stringUtil.addCardNumberForCamma(baseDto.getCemetery(), baseDto.getSet_deck_no()));
    			}

    			baseDao.update(baseDto);

    			Card card2 = cardUtil.getCard(enemyCard.getId());
    			if (!UtilConst.CARD_TYPE_MAGIC.equals(card2.getType())) {
    				enemyBaseDto.setCemetery(stringUtil.addCardNumberForCamma(enemyBaseDto.getCemetery(), enemyBaseDto.getSet_deck_no()));
    			}

    			baseDao.update(enemyBaseDto);

    			mySetCard = null;
    			enemyCard = null;
        	}

    		//次の行動順の計算
    		nextAction = battleControllUtil.nextOpenAction(
    				controllDTO
    				, mySetCard
    				, enemyCard
    				, playerId
    				, controllDTO.getPlayer_id_2());

    		//相手の復活情報を取得し、戻り値に設定する
    		String enemyRevivalStr = controllDTO.getPlayer_revival_2();
    		ArrayList<Object> enemyRevivalNumber = stringUtil.getJsonArray(enemyRevivalStr);

    		//取得した情報をMapに変換する
        	outMap.put("enemyRevivalNumber", enemyRevivalNumber);

    		//最後に状態をendに更新する。
        	controllDTO.setPlayer_input_1("end");

        	String out = "{}";

    		//判定が終わったnextActionと奥義ＩＤから更新内容を決める。
    		if (nextSpecialFlg != null) {
    			//次の行動が奥義の場合
    			if (nextSpecialFlg) {
    				//プレイヤー１の情報を設定
    				nextSpecialAction = controllDTO.getPlayer_id_1();
    				specialId = controllDTO.getSpecial_id_1();
    			} else {
    				//プレイヤー２の情報を設定
    				nextSpecialAction = controllDTO.getPlayer_id_2();
    				specialId = controllDTO.getSpecial_id_2();
    			}

    			//どちらの奥義か判定し、発動ユーザーＩＤ，奥義ＩＤの設定と更新内容の計算を行う
//				Deck nextActionDeck = cardUtil.getDeck(nextSpecialAction, deckNumber, playerPath);
//				specialId = nextActionDeck.getSpecial().getId();
//    			specialId = controllDTO.getSpecial_id_1();

				AbilityFactory ability = new AbilityFactory();
				SpecialAbility specialAbility = ability.getSpecialAbility(specialId);

				HashMap<String, Object> result = specialAbility.specialSkill(battleID, nextSpecialAction);

				if (result != null && result.get("updateInfo") != null) {
					updateInfo.put("updateInfo", result.get("updateInfo"));
				} else {
					updateInfo.put("updateInfo", new HashMap());
				}

				if (result != null && result.get("target") != null) {
    				target = (ArrayList<Object>)result.get("target");
    				updateInfo.put("target", target);
    			}

    			updateInfo.put("specialId", specialId);

    			out = stringUtil.getJsonStr(updateInfo);

    			//行動した奥義を行動済にする
    			controllDTO.setNext_special_status_1("end");

    		} else if (nextAction != null && !"".equals(nextAction) && !"end".equals(nextAction)) {
    			//次の行動がオープンスキルの場合
    			BattleBaseDTO openBaseDto = baseDao.getAllValue(battleID, nextAction);

				AbilityFactory open = new AbilityFactory();
				CardAbility cardAbility = open.getCardAbility(openBaseDto.getSet_card());

				HashMap<String, Object> result = cardAbility.open(battleID, nextAction);

				if (result != null && result.get("updateInfo") != null) {
					updateInfo.put("updateInfo", result.get("updateInfo"));
				} else {
					updateInfo.put("updateInfo", new HashMap());
				}

    			if (result != null && result.get("target") != null) {
    				target = (ArrayList<Object>)result.get("target");
    			}

    			//行動したオープンを行動済にする
    			controllDTO.setNext_open_status_1("end");

    			out = stringUtil.getJsonStr(result);

    		}

			//outを記入する
			controllDTO.setOut(out);

    		//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {
    			//奥義の選択の場合
    			if (nextSpecialAction != null && !"".equals(nextSpecialAction)) {
    				if (playerId.equals(nextSpecialAction)) {
        				controllDTO.setStatus_1("select");
        				controllDTO.setStatus_2("end");
        				AodemiLogger.write("自分の奥義selectです");
        			} else {
        				controllDTO.setStatus_1("end");
        				controllDTO.setStatus_2("select");
        				AodemiLogger.write("相手の奥義selectです");
        			}
    			} else {
    				//オープンの選択の場合
    				if (playerId.equals(nextAction)) {
        				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
        				controllDTO.setStatus_1("select");
        				controllDTO.setStatus_2("end");
        				AodemiLogger.write("自分のselectです");

        			} else {
        				controllDTO.setStatus_1("end");
        				controllDTO.setStatus_2("select");
        				AodemiLogger.write("相手のselectです");

        			}
    			}
    		} else {
    			controllDTO.setStatus_1("end");
    		}

    		controllDTO.setNext_action("start");
    		controllDao.update(controllDTO);

		} else {

    		AodemiLogger.write("プレイヤー１の判定を待っています");

			//フェーズがset以外ならフェーズをsetに更新、状態をreadyに更新
		   	if (!"set".equals(controllDTO.getPhase())) {

		   		//セット開始処理
		   		controllDTO.setPhase("set");
		   		controllDTO.setStatus_1("ready");
		   		controllDTO.setStatus_2("ready");
		   	}

    		//nextActionをwaitに
    		controllDTO.setNext_action("wait");
		   	controllDao.update(controllDTO);

		   	while (!"start".equals(controllDTO.getNext_action())) {
    			controllDTO = battleControllUtil.getAllValue(this.battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//次の行動情報取得
    		nextSpecialAction = controllDTO.getNext_special_player_1();

    		if (nextSpecialAction != null && "end".equals(nextSpecialAction)) {
    			nextAction = controllDTO.getNext_open_player_1();
    		}

    		//相手の前ターンのセットカード配置位置を設定する
    		outMap.put("enemySetPosition", controllDTO.getPlayer_output_1());

    		//更新情報を取得する
    		String out = controllDTO.getOut();

    		if ("".equals(out)) {
    			updateInfo = new HashMap<String, Object>();
    		} else {
    			updateInfo = stringUtil.getJsonMap(out);
    		}

    		if (updateInfo.get("specialId") != null && !"".equals(updateInfo.get("specialId"))) {
    			specialId = updateInfo.get("specialId").toString();
    		}

    		//入力完了したら、相手の盤面情報を取得する
    		enemyBaseDto = baseDao.getAllValue(battleID, controllDTO.getPlayer_id_1());

        	//バッティングフラグの判定
        	if (mySetCard != null && mySetCard.getId().equals(enemyBaseDto.getSet_card())) {
        		battingFlg = true;
        		nextAction = "end";
        	}

    		//相手の復活情報を取得し、戻り値に設定する
    		String enemyRevivalStr = controllDTO.getPlayer_revival_1();
    		ArrayList<Object> enemyRevivalNumber = stringUtil.getJsonArray(enemyRevivalStr);

    		//取得した情報を戻りに設定する
        	outMap.put("enemyRevivalNumber", enemyRevivalNumber);

        	if (updateInfo != null && updateInfo.get("target") != null) {
        		target = (ArrayList<Object>)updateInfo.get("target");
        	}

    		//対象選択がある場合
    		if (target.size() != 0) {

    			if (nextSpecialAction != null && !"".equals(nextSpecialAction)) {

    				//相手が対象選択の場合は相手が終了するまで待機する。自分の場合はそのまま一度返却
        			if ("select".equals(controllDTO.getStatus_1())) {

        				AodemiLogger.write("相手の奥義selectです。");

        			}

    			} else {

    				//相手が対象選択の場合は相手が終了するまで待機する。自分の場合はそのまま一度返却
        			if ("select".equals(controllDTO.getStatus_1())) {

        				AodemiLogger.write("相手のselectです。");

        			}
    			}

    		} else {
    			controllDTO.setStatus_2("end");
    			//入力状況は初期化しておく
        		battleControllUtil.initPlayerSetInput(controllDTO);
    		}

    		controllDTO.setNext_action("");
    		controllDao.update(controllDTO);
		}

    	//両方の入力が完了したところで、値を返却する。
    	outMap.put("severCheack", true);
    	outMap.put("enemyCardId", enemyBaseDto.getSet_card());

    	outMap.put("battingFlg", battingFlg);

    	//次行動の設定
		if (nextSpecialAction != null && !"end".equals(nextSpecialAction) && !"".equals(nextSpecialAction)) {
			//奥義利用している場合は奥義IDを設定、nextActionは空
			outMap.put("specialId", specialId);
    		outMap.put("nextAction", nextSpecialAction);
    		if (updateInfo != null) {
        		outMap.put("updateInfo", updateInfo.get("updateInfo"));
        	} else {
        		outMap.put("updateInfo", new HashMap());
        	}
    	} else {
    		outMap.put("specialId", "");

    		if (updateInfo != null) {
        		outMap.put("updateInfo", updateInfo.get("updateInfo"));
        	} else {
        		outMap.put("updateInfo", new HashMap());
        	}

    		//奥義利用してない場合は次の行動を設定
    		if (nextAction == null || "".equals(nextAction)) {
        		outMap.put("nextAction", "");
        	} else {
        		outMap.put("nextAction", nextAction);
        	}
    	}

    	outMap.put("enemyYellowMana", enemyBaseDto.getYellow());
    	outMap.put("enemyBlackMana", enemyBaseDto.getBlack());
    	outMap.put("enemyRedMana", enemyBaseDto.getRed());
    	outMap.put("enemyBlueMana", enemyBaseDto.getBlue());
    	outMap.put("targetList", target);

    	stringUtil.serializeJson(outMap, this.outputFile);

    	AodemiLogger.write("setを処理しました");

	}

	public void open(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

    	String playerId = map.get("playerId").toString();

    	AodemiLogger.write("open開始");

    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);
    	BattleBaseDTO baseDto = new BattleBaseDTO();

		if (player1Flg) {

			AodemiLogger.write("プレイヤー２の応答を待っています");

			while (!"wait".equals(controllDto.getNext_action())) {
				controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

		   	//フェーズがopen以外ならフェーズをsetに更新、状態をreadyに更新
		   	if (!"open".equals(controllDto.getPhase())) {
		   		controllDto.setPhase("open");
		   		controllDto.setStatus_1("ready");
		   		controllDto.setStatus_2("ready");
		   		controllDao.update(controllDto);
		   	}

		} else {

			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

			AodemiLogger.write("プレイヤー１の処理を待っています");

			//相手の処理が終わったら処理開始する
			while (!"start".equals(controllDto.getNext_action())) {
				controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}
		}

    	//nextActionがある場合、行動側が親として計算する。
    	HashMap<String, Object> outMap = new HashMap<String, Object>();
		HashMap<String, Object> updateInfo = new HashMap<String, Object>();

    	String nextAction = "";
		String myStatus = "";
		String nextSpecialId = "";
		String nextOpenCardId = "";

		myStatus = controllDto.getNext_special_status_2();

		//奥義チェック。２番目が設定されているなら奥義を起動する
		if ("ready".equals(myStatus)) {

			//奥義処理
			nextAction = controllDto.getNext_special_player_2();
//			Deck deck = cardUtil.getDeck(playerId, deckNumber, playerPath);
//			nextSpecialId = deck.getSpecial().getId();

			if (nextAction.equals(controllDto.getPlayer_id_1())) {
				nextSpecialId = controllDto.getSpecial_id_1();
			} else {
				nextSpecialId = controllDto.getSpecial_id_2();
			}

			if (!player1Flg) {
				controllDto.setNext_special_status_2("end");
			}

		} else {

			//奥義の設定が無いなら、openの処理
			myStatus = controllDto.getNext_open_status_1();

			//１番目のopen処理
			if ("ready".equals(myStatus)) {
				nextAction = controllDto.getNext_open_player_1();

				if (controllDto.getPlayer_id_1().equals(nextAction)) {
					//プレイヤー１の場合
					baseDto =battleFieldUtil.getBaseValue(controllDto.getPlayer_id_1());

				} else {
					//プレイヤー２の場合
					baseDto =battleFieldUtil.getBaseValue(controllDto.getPlayer_id_2());
				}
				nextOpenCardId = baseDto.getSet_card();

				if (!player1Flg) {
					controllDto.setNext_open_status_1("end");
				}

			} else {

				//２番目のopen処理
				myStatus = controllDto.getNext_open_status_2();

				if ("ready".equals(myStatus)) {
					nextAction = controllDto.getNext_open_player_2();

					if (controllDto.getPlayer_id_1().equals(nextAction)) {
						//プレイヤー１の場合
						baseDto = battleFieldUtil.getBaseValue(controllDto.getPlayer_id_1());

					} else {
						//プレイヤー２の場合
						baseDto = battleFieldUtil.getBaseValue(controllDto.getPlayer_id_2());
					}
					nextOpenCardId = baseDto.getSet_card();

					if (!player1Flg) {
						controllDto.setNext_open_status_2("end");
					}

				} else {
					//全てendの場合、open終了
					nextAction = "end";
				}
			}
		}

		ArrayList<Object> target = new ArrayList<Object>();

    	if ("end".equals(nextAction)) {

    		//空白の場合はオープン終了。
    		if (player1Flg) {
    			controllDto.setStatus_1("end");
    		} else {
    			controllDto.setStatus_2("end");
    		}

    	} else if (player1Flg) {

			AbilityFactory ability = new AbilityFactory();
			HashMap<String, Object> result = new HashMap<String, Object>();

			//奥義の判定かオープンスキルの判定かで処理を分ける
			if (nextSpecialId != null && !"".equals(nextSpecialId)) {

				SpecialAbility cardAbility = ability.getSpecialAbility(nextSpecialId);

				result = cardAbility.specialSkill(battleID, nextAction);
			} else {

				CardAbility cardAbility = ability.getCardAbility(nextOpenCardId);

				result = cardAbility.open(battleID, nextAction);

				//実行したカードが魔法だった場合、墓地に追加する
    			Card card = cardUtil.getCard(nextOpenCardId);
    			if (UtilConst.CARD_TYPE_MAGIC.equals(card.getType())) {
    				baseDto.setCemetery(stringUtil.addCardNumberForCamma(baseDto.getCemetery(), baseDto.getSet_deck_no()));
    			}

    			//利用したカードをデッキから利用済にする
    			BattleDeckDTO deckDto =  deckDao.getAllValue(battleID, nextAction, baseDto.getSet_deck_no());
    			deckDto.setCard_out(1);
    			deckDao.update(deckDto);
			}

			if (result != null && result.get("updateInfo") != null) {
				updateInfo.put("updateInfo", result.get("updateInfo"));
			} else {
				updateInfo.put("updateInfo", new HashMap());
			}

			if (result != null && result.get("target") != null) {
				target = (ArrayList<Object>)result.get("target");
			}

			//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {

    			//奥義の対象選択かオープンの対象選択か
    			if (nextSpecialId != null && !"".equals(nextSpecialId)) {
    				//次の行動が自分
        			if (playerId.equals(nextAction)) {
        				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
        				controllDto.setStatus_1("select");
        				controllDto.setStatus_2("end");

        				AodemiLogger.write("自分の奥義selectです。");

        			} else {
        				controllDto.setStatus_1("end");
        				controllDto.setStatus_2("select");

        				AodemiLogger.write("相手の奥義selectです。");

        			}
    			} else {
    				//次の行動が自分
        			if (playerId.equals(nextAction)) {
        				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
        				controllDto.setStatus_1("select");
        				controllDto.setStatus_2("end");

        				AodemiLogger.write("自分のselectです。");

        			} else {
        				controllDto.setStatus_1("end");
        				controllDto.setStatus_2("select");

        				AodemiLogger.write("相手のselectです。");

        			}
    			}

    		} else {
    			controllDto.setStatus_1("end");
    		}

			String out = stringUtil.getJsonStr(result);

			//outを記入する
			controllDto.setOut(out);

    	} else {

    		//更新内容が存在すれば返却
    		String out = controllDto.getOut();

    		if ("".equals(out)) {
    			updateInfo = new HashMap<String, Object>();
    		} else {
    			updateInfo = stringUtil.getJsonMap(out);
    		}

    		if (updateInfo != null && updateInfo.get("target") != null) {
    			target = (ArrayList<Object>)updateInfo.get("target");
    		}

    		//対象選択がある場合
    		if (target.size() != 0) {

    			if (nextSpecialId != null && !"".equals(nextSpecialId)) {
    				//自分が対象選択の場合はそのまま返却。それ以外は相手が終了するまで待機する。
        			if (!"select".equals(controllDto.getStatus_2())) {

        				AodemiLogger.write("相手の奥義selectです。");
        			}
    			} else {
    				//自分が対象選択の場合はそのまま返却。それ以外は相手が終了するまで待機する。
        			if (!"select".equals(controllDto.getStatus_2())) {

        				AodemiLogger.write("相手のselectです。");
        			}
    			}

    		} else {
    			controllDto.setStatus_2("end");
    			//入力状況は初期化しておく
        		battleControllUtil.initPlayerSetInput(controllDto);
    		}

    	}

    	AodemiLogger.write("nextAction:" + nextAction);

    	//処理終了したのでステータスをendに更新しておく
		if (player1Flg) {

			controllDto.setNext_action("start");

		} else if (!"end".equals(nextAction)) {

			if (!"select".equals(controllDto.getStatus_1()) && !"select".equals(controllDto.getStatus_2())) {
				controllDto.setStatus_1("ready");
				controllDto.setStatus_2("ready");
			}

			controllDto.setNext_action("");

		} else if ("end".equals(nextAction)) {
			//全て終了したので入力状況は初期化しておく
    		battleControllUtil.initPlayerInput(controllDto);
		}

		controllDao.update(controllDto);

    	//両方の入力が完了したところで、値を返却する。
    	outMap.put("severCheack", true);
    	outMap.put("nextAction", nextAction);
    	outMap.put("specialId", nextSpecialId);
    	outMap.put("targetList", target);

    	if (updateInfo != null) {
    		outMap.put("updateInfo", updateInfo.get("updateInfo"));
    	} else {
    		outMap.put("updateInfo", new HashMap());
    	}

    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("open終了");

	}

	public void remove(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();
//		logger = new AodemiLogger(this.battleID, playerId, player1Flg);

    	AodemiLogger.write("remove開始");
    	HashMap<String, Object> outMap = new HashMap<String, Object>();

    	//プレイヤー１が親として行動する
    	HashMap<String, Object> shieldMap = new HashMap<String, Object>();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		ArrayList<Object> target = new ArrayList<Object>();

    	if (player1Flg) {

    		AodemiLogger.write("プレイヤー２を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

        	//フェーズ更新
        	if (!"remmove".equals(controllDto.getPhase())) {
        		controllDto.setPhase("remove");
        		controllDto.setStatus_1("ready");
        		controllDto.setStatus_2("ready");
        		battleControllUtil.initPlayerInput(controllDto);
        		controllDao.update(controllDto);
        	}

    		//リムーブ処理を行う
    		shieldMap = battleFieldUtil.remove(controllDto, cardUtil);

    		//map情報
    		String out = stringUtil.getJsonStr(shieldMap);

    		//結果をoutに記入する
    		controllDto.setOut(out);

    		if (shieldMap.get("target") != null) {
    			target = (ArrayList<Object>) shieldMap.get("target");
    		}

    		//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {
    			//次の行動が自分
    			if (playerId.equals(shieldMap.get("playerId"))) {
    				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
    				controllDto.setStatus_1("select");

    			} else {
    				controllDto.setStatus_2("select");
//    				controllDao.update(controllDto);

    				AodemiLogger.write("相手のselectです。");

//    				//相手が選択の場合は、相手の計算が完了するまで待機する
//    				while (!"end".equals(controllDto.getStatus_2())) {
//    					controllDto = controllDao.getAllValue(battleID);
//    	       		//１秒停止
//    	       		Thread.sleep(500);
//    	       	}
//
//    	   		//取得した情報をMapに変換する
//    				shieldMap.put("updateInfo", stringUtil.getJsonMap(controllDto.getNext_action()));
//
//    	   		controllDto.setStatus_1("end");
//
//    	   		//自分が対象選択じゃない場合はtargetは初期化しておく
//    	   		target = new ArrayList();
//
//    	   		//入力内容は初期化しておく
//    	   		battleControllUtil.initPlayerInput(controllDto);

    			}
    		} else {
    			controllDto.setStatus_1("end");
    		}

    		//未設定の場合はリムーブなしとしてend
    		if (shieldMap.get("fieldNumber") == null || "".equals(shieldMap.get("fieldNumber"))) {
    			outMap.put("playerId", "end");
            	outMap.put("fieldNumber", null);
            	outMap.put("selectCount", 0);
            	outMap.put("shieldId", "");
            	outMap.put("targetList", new HashMap<String, Object>());
            	outMap.put("updateInfo", new HashMap<String, Object>());
    		} else {

        		//開始前に覚醒ゲージを１０増やす
        		BattleBaseDAO baseDao = factory.createBaseDAO();
        		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, shieldMap.get("playerId").toString());
        		int gage = baseDto.getSpecial_gage() + 10;

        		if (gage >= 20) {
        			gage = gage - 20;
        			baseDto.setSpecial_stock(baseDto.getSpecial_stock() + 1);

        			if (baseDto.getSpecial_stock() > 5) {
        				baseDto.setSpecial_stock(5);
        			}
        		}

        		baseDto.setSpecial_gage(gage);
        		baseDao.update(baseDto);

    			//リムーブ対象が存在する場合は戻り値を設定する
    			outMap.put("playerId", shieldMap.get("playerId"));
    			outMap.put("fieldNumber", shieldMap.get("fieldNumber"));
    			outMap.put("shieldId", shieldMap.get("shieldId"));
    			outMap.put("targetList", target);

    			if (shieldMap.get("updateInfo") != null) {
    				outMap.put("updateInfo", shieldMap.get("updateInfo"));

    			} else {
    				outMap.put("updateInfo", new HashMap<String, Object>());
    			}
    		}

    		controllDto.setNext_action("start");

    		controllDao.update(controllDto);

    	} else {

    		//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

    		AodemiLogger.write("相手の計算を待っています。");

    		//相手情報が未完了場合、入力されるまで待つ
    		while (!"start".equals(controllDto.getNext_action())) {
    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//取得した情報をMapに変換する
    		shieldMap = stringUtil.getJsonMap(controllDto.getOut());

    		if (shieldMap.get("target") != null) {
    			target = (ArrayList<Object>) shieldMap.get("target");
    		}

    		//対象選択がある場合
    		if (target.size() != 0) {

    			//自分が対象選択の場合はそのまま返却。それ以外は相手が終了するまで待機する。
    			if (!"select".equals(controllDto.getStatus_2())) {

    				AodemiLogger.write("相手のselectです。");

//    				while (!"end".equals(controllDto.getStatus_1())) {
//    					controllDto = controllDao.getAllValue(battleID);
//    	       		//１秒停止
//    	       		Thread.sleep(500);
//    	       	}
//    				//対象選択後の更新結果を変更する
//    				shieldMap.put("updateInfo", stringUtil.getJsonMap(controllDto.getNext_action()));
//
//    				//自分が対象選択じゃない場合はtargetは初期化しておく
//    	   		target = new ArrayList();
//    	   		//対象が存在する場合は自分が最後なので初期化
//    	   		battleControllUtil.initPlayerInput(controllDto);
    			}

    		} else {
    			controllDto.setStatus_2("end");
    			//入力状況は初期化しておく
        		battleControllUtil.initPlayerInput(controllDto);
    		}

    		//未設定の場合はリムーブなしとしてend
    		if (shieldMap.get("fieldNumber") == null || "".equals(shieldMap.get("fieldNumber"))) {
    			outMap.put("playerId", "end");
            	outMap.put("fieldNumber", null);
            	outMap.put("selectCount", 0);
            	outMap.put("shieldId", "");
            	outMap.put("targetList", new HashMap<String, Object>());
            	outMap.put("updateInfo", new HashMap<String, Object>());

            	//リムーブ対象が居ない状態なら、オープンをクローズに移す
        		fieldDao.checkOpenClose(battleID);

    		} else {
    			//リムーブ対象が存在する場合は戻り値を設定する
    			outMap.put("playerId", shieldMap.get("playerId"));
    			outMap.put("fieldNumber", shieldMap.get("fieldNumber"));
    			outMap.put("shieldId", shieldMap.get("shieldId"));
    			outMap.put("targetList", target);

    			if (shieldMap.get("updateInfo") != null) {
    				outMap.put("updateInfo", shieldMap.get("updateInfo"));
    			} else {
    				outMap.put("updateInfo", new HashMap<String, Object>());
    			}
    		}

    		controllDto.setNext_action("");
    		controllDao.update(controllDto);

    	}

		outMap.put("severCheack", true);
    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("remove終了");

	}

	public void start(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();

    	AodemiLogger.write("start開始");
    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	HashMap<String, Object> nextMap = new HashMap<String, Object>();
    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);
    	ArrayList<Object> target = new ArrayList<Object>();

    	//プレイヤー１が親として行動する
    	if (player1Flg) {

    		AodemiLogger.write("プレイヤー２を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//フェーズ更新
        	if (!"start".equals(controllDto.getPhase())) {
        		controllDto.setPhase("start");
        		controllDto.setStatus_1("ready");
        		controllDto.setStatus_2("ready");
        		battleControllUtil.initPlayerInput(controllDto);
        		controllDao.update(controllDto);
        	}

        	//存在するユニットの中で、スタートスキルを保持しているユニットで速度が高い順に行動する
        	nextMap = battleControllUtil.nextStartAction(controllDto, battleFieldUtil);

        	String out = stringUtil.getJsonStr(nextMap);

        	//次の行動順と更新内容を設定して処理を終了する。
    		//結果をoutに記入する
    		controllDto.setOut(out);

    		if (nextMap.get("target") != null) {
    			target = (ArrayList<Object>)nextMap.get("target");
    		}

			//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {
    			//次の行動が自分
    			if (playerId.equals(nextMap.get("playerId"))) {
    				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
    				controllDto.setStatus_1("select");

    			} else {
    				controllDto.setStatus_2("select");
    				controllDao.update(controllDto);

    				AodemiLogger.write("相手のselectです");
//
//    				//相手が選択の場合は、相手の計算が完了するまで待機する
//    				while (!"end".equals(controllDto.getStatus_2())) {
//    					controllDto = controllDao.getAllValue(battleID);
//    	       		//１秒停止
//    	       		Thread.sleep(500);
//    	       	}
//
//    	   		//取得した情報をMapに変換する
//    				nextMap.put("updateInfo", stringUtil.getJsonMap(controllDto.getNext_action()).get("updateInfo"));
//
//    				controllDto.setStatus_1("ready");
//    	   		controllDto.setStatus_2("ready");
//
//    	   		//自分が対象選択じゃない場合はtargetは初期化しておく
//    	   		target = new ArrayList();
//    	   		battleControllUtil.initPlayerInput(controllDto);
    			}
    		} else {
    			controllDto.setStatus_1("end");
    		}

    		controllDto.setNext_action("start");

    		controllDao.update(controllDto);

    	} else {

    		//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

			AodemiLogger.write("相手の計算を待っています");

    		//相手が計算完了するまで待つ
    		while (!"start".equals(controllDto.getNext_action())) {
    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//次の行動情報取得
    		String out = controllDto.getOut();

    		//取得した情報をMapに変換する
    		nextMap = stringUtil.getJsonMap(out);

    		if (nextMap.get("target") != null) {
    			target = (ArrayList<Object>)nextMap.get("target");
    		}

    		//対象選択がある場合
    		if (target.size() != 0) {

//    			//自分が対象選択の場合はそのまま返却。それ以外は相手が終了するまで待機する。
    			if (!"select".equals(controllDto.getStatus_2())) {
//
//    				controllDto = controllDao.getAllValue(battleID);
//
    				AodemiLogger.write("相手のselectです");
//
//    				while (!"end".equals(controllDto.getStatus_1())) {
//    					controllDto = controllDao.getAllValue(battleID);
//    	       		//１秒停止
//    	       		Thread.sleep(500);
//    	       	}
//    				//対象選択後の更新結果を変更する
//    				nextMap.put("updateInfo", stringUtil.getJsonMap(controllDto.getNext_action()).get("updateInfo"));
//
//    				//自分が対象選択じゃない場合はtargetは初期化しておく
//    	   		target = new ArrayList();
//    	   		//ステータスはreadyに戻す
//    	   		controllDto.setStatus_1("ready");
//    	   		controllDto.setStatus_2("ready");
//    	   		//対象が存在する場合は自分が最後なので初期化
//    	   		battleControllUtil.initPlayerInput(controllDto);
    			}

    		} else {
    			//ステータスはreadyに戻す
	   		controllDto.setStatus_1("ready");
	   		controllDto.setStatus_2("ready");
    			//入力状況は初期化しておく
        		battleControllUtil.initPlayerInput(controllDto);
    		}

    		controllDto.setNext_action("");
    		controllDao.update(controllDto);
    	}

    	outMap.put("severCheack", true);
    	outMap.put("nextPlayerId", nextMap.get("playerId"));
    	outMap.put("nextFieldNumber", nextMap.get("fieldNumber"));
    	outMap.put("updateInfo", nextMap.get("updateInfo"));
    	outMap.put("targetList", target);
    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("start終了");
	}

	public void select(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();

		ArrayList<Object> target = new ArrayList<Object>();

		if (map.get("target") != null) {
			target = (ArrayList<Object>)map.get("target");
		}

		String phase = map.get("phase").toString();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

    	AodemiLogger.write("select開始");
    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	HashMap<String, Object> nextMap = new HashMap<String, Object>();

    	//次の行動情報を取得する
		String out = controllDto.getOut();

		//取得した情報をMapに変換する
		if (out != null && !"".equals(out)) {
			nextMap = stringUtil.getJsonMap(out);
		}

		HashMap<String, Object> selectMap = new HashMap<String, Object>();

		//プレイヤー１が親
		if (player1Flg) {

			AodemiLogger.write("プレイヤー２を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

			out = controllDto.getOut();

			if (out != null && !"".equals(out)) {
				nextMap = stringUtil.getJsonMap(out);
			}

			String selectPlayer = "";

			int fieldNumber = 0;

			//どちらのselectなのかを判定
			if ("select".equals(controllDto.getStatus_1())) {
				selectPlayer = controllDto.getPlayer_id_1();

				target = (ArrayList) map.get("target");

				if (map.get("fieldNumber") != null) {
					fieldNumber = (int)map.get("fieldNumber");
				}

			} else {
				selectPlayer = controllDto.getPlayer_id_2();

				//プレイヤ２が選択の場合、選択情報と盤面位置を取得する
				target = (ArrayList)nextMap.get("selectTarget");

				if (nextMap.get("selectFieldNumber") != null) {
					fieldNumber = (int)nextMap.get("selectFieldNumber");
				}

			}

		   	//どちらのselectなのかで処理を分ける
		   	if ("openSelect".equals(phase)) {
		   		//行動するカードＩＤを取得する
		   		BattleBaseDTO basedto = baseDao.getAllValue(battleID, selectPlayer);

		   		//セットカードを取得する
		   		String cardId = basedto.getSet_card();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//オープンスキル用計算処理を呼び出す
				selectMap = cardAbility.openSelect(battleID, selectPlayer, target);

		   	} else if ("startSelect".equals(phase)) {

		   		BattleFieldDTO fieldto = fieldDao.getAllValue(battleID, selectPlayer, fieldNumber);

		   		//セットカードを取得する
		   		String cardId = fieldto.getCard_id();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//スタートスキル用計算処理を呼び出す
				selectMap = cardAbility.startSelect(battleID, selectPlayer, target, fieldNumber);

		   	} else if ("autoSelect".equals(phase)) {

		   		BattleFieldDTO fieldto = fieldDao.getAllValue(battleID, selectPlayer, fieldNumber);

		   		//セットカードを取得する
		   		String cardId = fieldto.getCard_id();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//スタートスキル用計算処理を呼び出す
				selectMap = cardAbility.autoSelect(battleID, selectPlayer, target, fieldNumber);

		   	} else if ("skill1Select".equals(phase)) {

		   		BattleFieldDTO fieldto = fieldDao.getAllValue(battleID, selectPlayer, fieldNumber);

		   		//セットカードを取得する
		   		String cardId = fieldto.getCard_id();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//アクション１スキル用計算処理を呼び出す
				selectMap = cardAbility.actionSelect1(battleID, selectPlayer, target, fieldNumber);

		   	} else if ("skill2Select".equals(phase)) {

		   		BattleFieldDTO fieldto = fieldDao.getAllValue(battleID, selectPlayer, fieldNumber);

		   		//セットカードを取得する
		   		String cardId = fieldto.getCard_id();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//アクション１スキル用計算処理を呼び出す
				selectMap = cardAbility.actionSelect2(battleID, selectPlayer, target, fieldNumber);

		   	} else if ("shieldSelect".equals(phase)) {
		   		//行動するカードＩＤを取得する
		   		String shieldId = nextMap.get("shieldId").toString();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				ShieldAbility shieldAbility = ability.getShieldAbility(shieldId);

				//シールドスキル用計算処理を呼び出す
				selectMap = shieldAbility.shieldSkillSelect (battleID, selectPlayer, target);
		   	} else if ("closeSelect".equals(phase)) {

		   		//クローズカードを取得する
		   		String cardId = nextMap.get("closeCardId").toString();

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				CardAbility cardAbility = ability.getCardAbility(cardId);

				//スタートスキル用計算処理を呼び出す
				selectMap = cardAbility.closeSelect(battleID, selectPlayer, target, fieldNumber);

		   	} else if ("specialSelect".equals(phase)) {

		   		//奥義カードIDを取得
//		   		Deck nextActionDeck = cardUtil.getDeck(selectPlayer, deckNumber, playerPath);
//		   		String specialId = nextActionDeck.getSpecial().getId();

		   		String specialId =  "";

		   		if (selectPlayer.equals(controllDto.getSpecial_id_1())) {
		   			specialId = controllDto.getSpecial_id_1();
		   		} else {
		   			specialId = controllDto.getSpecial_id_2();
		   		}

		   		//カードの内容と対象情報から更新内容を決定する
		   		AbilityFactory ability = new AbilityFactory();
				SpecialAbility specialAbility = ability.getSpecialAbility(specialId);

				//スタートスキル用計算処理を呼び出す
				selectMap = specialAbility.specialSkillSelect(battleID, selectPlayer, target);
		   	}

		   	//決定した更新内容でnextActionを更新する
		   	if (selectMap.get("updateInfo") == null || "".equals(selectMap.get("updateInfo"))) {
		   		nextMap.put("updateInfo", new ArrayList());
		   	} else {
		   		nextMap.put("updateInfo", selectMap.get("updateInfo"));
		   	}

//	    	//戦闘フェーズの場合、選択結果で次の行動が変わる可能性があるため、次の行動を再計算する。
//	    	HashMap<String, Object> reMap = new HashMap<String, Object>();
//
//	    	//オートスキルの選択の場合は行動順は変わらないため再計算しない
//	    	if ("battle".equals(controllDto.getPhase()) && !"autoSelect".equals(phase)) {
//	    		//次の行動の計算
//	    		reMap = battleFieldUtil.getNextActionMap(controllDto, cardUtil);
//
//	    		nextMap.put("nextPlayerId", reMap.get("playerId"));
//	    		nextMap.put("nextFieldNumber", reMap.get("fieldNumber"));
//	    		nextMap.put("autoUpdateInfo", reMap.get("autoUpdateInfo"));
//	    		nextMap.put("autoTargetList", reMap.get("autoTargetList"));
//
//	    	} else {
//	    		nextMap.put("nextPlayerId", "");
//	    		nextMap.put("nextFieldNumber", null);
//	    		nextMap.put("autoUpdateInfo", new HashMap());
//	    		nextMap.put("autoTargetList", new ArrayList());
//	    	}

		   	controllDto.setOut(stringUtil.getJsonStr(nextMap));

		   	//最後にnextActionをstartにする
			controllDto.setNext_action("start");

		} else {

			//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");

			if (map.get("target") != null) {
				//targetを持っている場合、outに追記する
				out = controllDto.getOut();

				if (out != null && !"".equals(out)) {
					nextMap = stringUtil.getJsonMap(out);
				}

				nextMap.put("selectTarget", map.get("target"));
				nextMap.put("selectFieldNumber", map.get("fieldNumber"));

				controllDto.setOut(stringUtil.getJsonStr(nextMap));
			}

			controllDao.update(controllDto);

			//プレイヤー２は相手が終わるまで待機
			AodemiLogger.write("プレイヤー１の計算を待機中");

			while (!"start".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

			//結果を取得
			out = controllDto.getOut();
			nextMap = stringUtil.getJsonMap(out);

			//フェーズで処理を分ける
			if ("specialSelect".equals(phase) || "openSelect".equals(phase)) {
				//セットフェーズ用の初期化処理
				controllDto.setStatus_1("end");
				controllDto.setStatus_2("end");

				if ("end".equals(controllDto.getNext_special_status_1())
						&& "end".equals(controllDto.getNext_special_status_2())
						&& "end".equals(controllDto.getNext_open_status_1())
						&& "end".equals(controllDto.getNext_open_status_2())) {
					battleControllUtil.initPlayerInput(controllDto);
				} else {
					battleControllUtil.initPlayerSetInput(controllDto);
				}

			} else if ("startSelect".equals(phase)) {
				//ステータスはreadyに戻す
		   		controllDto.setStatus_1("ready");
		   		controllDto.setStatus_2("ready");

		   		//最後なので一旦初期化
		   		battleControllUtil.initPlayerInput(controllDto);

			} else {
				//ステータスはendに更新
		   		controllDto.setStatus_1("end");
		   		controllDto.setStatus_2("end");
				battleControllUtil.initPlayerInput(controllDto);
			}

			controllDto.setNext_action("");

		}

    	controllDao.update(controllDto);

    	outMap.put("severCheack", true);
    	outMap.put("updateInfo", nextMap.get("updateInfo"));

//    	outMap.put("nextPlayerId", nextMap.get("nextPlayerId"));
//    	outMap.put("nextFieldNumber", nextMap.get("nextFieldNumber"));
//    	outMap.put("autoUpdateInfo", nextMap.get("autoUpdateInfo"));
//    	outMap.put("autoTargetList", nextMap.get("autoTargetList"));

    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("select終了");
	}

	public void battle(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();

    	AodemiLogger.write("battle開始");
    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	HashMap<String, Object> nextMap = new HashMap<String, Object>();
    	ArrayList<Object> autoTarget = new ArrayList<Object>();

    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);

    	//プレイヤー１を親として行動する
    	if (player1Flg) {

    		AodemiLogger.write("プレイヤー２を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

        	//フェーズ更新
        	if (!"battle".equals(controllDto.getPhase())) {
        		controllDto.setPhase("battle");
        		controllDto.setStatus_1("ready");
        		controllDto.setStatus_2("ready");

        		//一時入力情報の初期化
        		battleControllUtil.initPlayerInput(controllDto);
        	}

    		//次の行動順の計算
    		nextMap = battleFieldUtil.getNextActionMap(controllDto, cardUtil);

    		if (nextMap != null && nextMap.get("autoTargetList") != null) {
        		autoTarget = (ArrayList<Object>)nextMap.get("autoTargetList");
        	}

    		if (autoTarget.size() != 0) {

     			//ステータスを一度selectに更新して処理を返却する
     			if (playerId.equals(nextMap.get("playerId"))) {
     				controllDto.setStatus_1("select");
     				AodemiLogger.write("プレイヤー１のautoSelectです。");
     			} else {
     				controllDto.setStatus_2("select");
     				AodemiLogger.write("プレイヤー２のautoSelectです。");
     			}

     		}

    		//map情報
    		String out = stringUtil.getJsonStr(nextMap);

    		//結果をoutに記入する
    		controllDto.setOut(out);

    		//最後に状態をendに更新する。
    		controllDto.setPlayer_input_1("end");
    		controllDto.setNext_action("start");

    	} else {

    		//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

    		AodemiLogger.write("待機開始");
    		//相手情報が未完了場合、入力されるまで待つ
    		while (!"start".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//次の行動情報取得
    		String out = controllDto.getOut();

    		//取得した情報をMapに変換する
    		nextMap = stringUtil.getJsonMap(out);

    		//最後に状態をendに更新する。
    		controllDto.setPlayer_input_2("end");

    		//入力状況は初期化しておく
    		battleControllUtil.initPlayerInput(controllDto);

    	}

    	controllDao.update(controllDto);

    	outMap.put("severCheack", true);
    	outMap.put("nextPlayerId", nextMap.get("playerId"));
    	outMap.put("nextFieldNumber", nextMap.get("fieldNumber"));
    	outMap.put("autoUpdateInfo", nextMap.get("autoUpdateInfo"));
    	outMap.put("autoTargetList", nextMap.get("autoTargetList"));

    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("battle終了");
	}

	@SuppressWarnings({ "unused", "unchecked" })
	public void battleAction(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {
		String playerId = map.get("playerId").toString();

		AodemiLogger.write("battleAction開始");

    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	outMap.put("severCheack", true);

    	HashMap<String, Object> nextMap = new HashMap<String, Object>();

    	boolean mainFlg = false;

    	//設定内容で処理を分ける
    	String battleAction = "";
    	int fieldNumber = 0;
    	if (map.get("fieldNumber") != null && !"".equals(map.get("fieldNumber"))) {
    		fieldNumber = Integer.parseInt(map.get("fieldNumber").toString());
    	}

    	//battleActionが設定されている方が親
    	if (map.get("battleAction") != null && !"".equals(map.get("battleAction"))) {
    		battleAction = map.get("battleAction").toString();
    		mainFlg = true;
    	}

    	//行動する側が親として計算
    	HashMap<String, Object> fieldUpdate = new HashMap<String, Object>();

    	int len = battleAction.length();
    	String shieldId = "";

    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);
    	ArrayList<Object> target = new ArrayList<Object>();
    	ArrayList<Object> autoTarget = new ArrayList<Object>();
    	String actionPlayer = "";

    	if (mainFlg) {

    		AodemiLogger.write("相手を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

        	//フェーズ更新
        	if (!"battleAction".equals(controllDto.getPhase())) {

        		//一時入力情報の初期化
        		battleControllUtil.initPlayerInput(controllDto);
        	}

        	if (len == 3) {
        		//攻撃
        		fieldUpdate = battleFieldUtil.attack(controllDto, playerId, fieldNumber);
        		actionPlayer = playerId;

        		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, actionPlayer, fieldNumber);
        		fieldDto.setAction(1);
        		fieldDao.update(fieldDto);

        	} else if (len == 4) {
        		//待機
        		fieldUpdate = battleFieldUtil.rest(controllDto, playerId, fieldNumber);
        		actionPlayer = playerId;

        		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, actionPlayer, fieldNumber);
        		fieldDto.setAction(1);
        		fieldDao.update(fieldDto);

        	} else if (len == 12) {
        		//アクションスキル
        		fieldUpdate = battleFieldUtil.actionSkill(controllDto, playerId, fieldNumber, battleAction);
        		actionPlayer = playerId;

        		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, actionPlayer, fieldNumber);
        		fieldDto.setAction(1);
        		fieldDao.update(fieldDto);

        	} else if (len == 6) {
        		//シールドブレイク処理
        		String enemyPlayerId = "";
        		if (playerId.equals(controllDto.getPlayer_id_1())) {
        			enemyPlayerId = controllDto.getPlayer_id_2();
        		} else {
        			enemyPlayerId = controllDto.getPlayer_id_1();
        		}

        		//処理前にＳＰを１減らす
        		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, enemyPlayerId);
        		baseDto.setSp(baseDto.getSp() - 1);
        		baseDao.update(baseDto);

        		//開始前に覚醒ゲージを１０増やす
        		int gage = baseDto.getSpecial_gage() + 10;

        		if (gage >= 20) {
        			gage = gage - 20;
        			baseDto.setSpecial_stock(baseDto.getSpecial_stock() + 1);

        			if (baseDto.getSpecial_stock() > 5) {
        				baseDto.setSpecial_stock(5);
        			}
        		}

        		baseDto.setSpecial_gage(gage);
        		baseDao.update(baseDto);

        		//自分をアクション終了にする
        		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
        		fieldDto.setAction(1);
        		fieldDao.update(fieldDto);

        		int breakShieldNumber = Integer.parseInt(battleAction.substring(5, 6));
        		HashMap<String, Object> breakInfo = battleFieldUtil.breakShield(enemyPlayerId, breakShieldNumber);

       		    fieldUpdate.put("updateInfo", breakInfo.get("updateInfo"));
       		    fieldUpdate.put("target", breakInfo.get("target"));

       		    if (breakInfo != null && breakInfo.get("shieldId") != null) {
       		    	shieldId = breakInfo.get("shieldId").toString();
       		    }

        		actionPlayer = enemyPlayerId;

        	} else if (len == 5) {

        		if ("break".equals(battleAction)) {
        			//プレイヤーアタックが成功したら勝利
                	outMap.put("severCheack", "win");

                	//相手の敗北フラグを設定
                	if (player1Flg) {
                		controllDto.setPlayer_surrend_2(1);
                	} else {
                		controllDto.setPlayer_surrend_1(1);
                	}

        		} else {
        			//移動
            		int aft = Integer.parseInt(battleAction.substring(4, 5));
            		fieldUpdate = battleFieldUtil.move(controllDto, playerId, fieldNumber, aft);
            		actionPlayer = playerId;
        		}
        	}

        	if (fieldUpdate != null && fieldUpdate.get("target") != null) {
        		target = (ArrayList<Object>)fieldUpdate.get("target");
        	}

        	//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {

				//ステータスを一度selectに更新して処理を返却する
    			if (actionPlayer.equals(controllDto.getPlayer_id_1())) {
    				controllDto.setStatus_1("select");
    				AodemiLogger.write("プレイヤー１のselectです。");
    			} else {
    				controllDto.setStatus_2("select");
    				AodemiLogger.write("プレイヤー２のselectです。");
    			}

    			//targetがある場合、次の行動を計算しない
    			nextMap.put("playerId", "");
    			nextMap.put("fieldNumber", null);
//    			nextMap.put("autoUpdateInfo", new HashMap());
//    			nextMap.put("autoTargetList", new ArrayList());

    		} else {
    			controllDto.setStatus_1("end");

//    			//targetが無い場合、次の行動順の計算
//            	nextMap = battleFieldUtil.getNextActionMap(controllDto, cardUtil);
//
//            	if (nextMap != null && nextMap.get("autoTargetList") != null) {
//            		autoTarget = (ArrayList<Object>)nextMap.get("autoTargetList");
//            	}
//
//        		if (autoTarget.size() != 0) {
//
//        			String action = nextMap.get("playerId").toString();
//
//        			//ステータスを一度selectに更新して処理を返却する
//        			if (action.equals(controllDto.getPlayer_id_1())) {
//        				controllDto.setStatus_1("select");
//        				AodemiLogger.write("プレイヤー１のautoSelectです。");
//        			} else {
//        				controllDto.setStatus_2("select");
//        				AodemiLogger.write("プレイヤー２のautoSelectです。");
//        			}
//
//        		}
    		}

        	//nextMapにbattleActionを追加しておく
        	nextMap.put("updateInfo", fieldUpdate.get("updateInfo"));
        	nextMap.put("target", fieldUpdate.get("target"));

        	nextMap.put("battleAction", battleAction);
        	nextMap.put("shieldId", shieldId);

        	//行動順をnextActionに記入する
    		String nextAction = stringUtil.getJsonStr(nextMap);
    		controllDto.setOut(nextAction);

    		controllDto.setNext_action("start");

    		controllDao.update(controllDto);

    	} else {

    		//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

    		//相手が入力完了するまで待つ
    		AodemiLogger.write("待機開始");

    		//相手情報が未完了場合、入力されるまで待つ
    		while (!"start".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);

        		//１秒停止
        		Thread.sleep(500);
        	}

    		//自分が敗北している場合は負けの情報を返却する
    		if (battleControllUtil.cheackMyWinLose(player1Flg, controllDto)) {
    			outMap.put("severCheack", "lose");
    		} else {
    			//負けでなければ更新情報を返却する
    			String nextAction = controllDto.getOut();
        		nextMap = stringUtil.getJsonMap(nextAction);

        		if (nextMap != null && nextMap.get("target") != null) {
            		target = (ArrayList<Object>)nextMap.get("target");
            	}

//        		if (nextMap != null && nextMap.get("autoTargetList") != null) {
//            		autoTarget = (ArrayList<Object>)nextMap.get("autoTargetList");
//            	}

        		//対象選択がある場合、ステータスを変更しない
        		if (target.size() == 0) {
        			controllDto.setStatus_2("end");
        			//入力状況は初期化しておく
            		battleControllUtil.initPlayerInput(controllDto);
        		}
    		}

    		if (nextMap.get("shieldId") != null) {
    			shieldId = nextMap.get("shieldId").toString();
    		}

    		controllDto.setNext_action("");
    		controllDao.update(controllDto);

    	}

    	outMap.put("shieldId", shieldId);

    	if (nextMap != null) {
    		outMap.put("updateInfo", nextMap.get("updateInfo"));
        	outMap.put("battleAction", nextMap.get("battleAction"));
//        	outMap.put("nextPlayerId", nextMap.get("playerId"));
//        	outMap.put("nextFieldNumber", nextMap.get("fieldNumber"));
//        	outMap.put("autoUpdateInfo", nextMap.get("autoUpdateInfo"));
//        	outMap.put("autoTargetList", nextMap.get("autoTargetList"));
        	outMap.put("targetList", target);
    	} else {
    		outMap.put("updateInfo", new HashMap());
        	outMap.put("battleAction", "");
//        	outMap.put("nextPlayerId", "");
//        	outMap.put("nextFieldNumber", null);
//        	outMap.put("autoUpdateInfo", new HashMap());
//        	outMap.put("autoTargetList", new ArrayList());
        	outMap.put("targetList", new ArrayList());
    	}

    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("battleAction終了");

	}

	public void close(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();

    	AodemiLogger.write("close開始");
    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	HashMap<String, Object> nextMap = new HashMap<String, Object>();
    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);
    	ArrayList<Object> target = new ArrayList<Object>();

    	//プレイヤー１が親として行動する
    	if (player1Flg) {

    		AodemiLogger.write("プレイヤー２を待っています");

			//最初にプレイヤー２を待機
			while (!"wait".equals(controllDto.getNext_action())) {

    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

			//クローズ対象を取得
			BattleFieldDTO dto = fieldDao.getNextCloseSkill(battleID);

			//カードの情報が存在しない場合、処理終了する
			if (dto == null || dto.getCard_id() == null || "".equals(dto.getCard_id())) {
				//クローズスキルの対象なし
				nextMap.put("playerId", "end");
				nextMap.put("fieldNumber", null);
				nextMap.put("updateInfo", new HashMap());

			} else {
				//クローズ対象が決まったので、クローズスキルを呼びだす
				AbilityFactory close = new AbilityFactory();
				CardAbility cardAbility = close.getCardAbility(dto.getCard_id());

				HashMap<String, Object> closeMap = cardAbility.close(battleID, dto.getPlayer_id(), dto.getField_no());

				nextMap.put("playerId", dto.getPlayer_id());
				nextMap.put("fieldNumber", dto.getField_no());
				nextMap.put("closeCardId", dto.getCard_id());
				nextMap.put("updateInfo", closeMap.get("updateInfo"));
				nextMap.put("target", closeMap.get("target"));

				//クローズフラグを１にする
				BattleFieldDTO updateDto = fieldDao.getAllValue(battleID, dto.getPlayer_id(), dto.getField_no());
				updateDto.setClose_skill(1);
				fieldDao.update(updateDto);
			}

    		if (nextMap.get("target") != null) {
    			target = (ArrayList<Object>)nextMap.get("target");
    		}

//			nextMap.put("nextPlayerId", "");
//    		nextMap.put("nextFieldNumber", null);
//    		nextMap.put("autoUpdateInfo", new HashMap());
//    		nextMap.put("autoTargetList", new ArrayList());

			//対象選択の有無で処理を切り分ける
    		if (target.size() != 0) {
    			//次の行動が自分
    			if (playerId.equals(nextMap.get("playerId"))) {
    				//自分が選択行動の場合はステータスを一度selectに更新して処理を返却する
    				controllDto.setStatus_1("select");

    			} else {
    				controllDto.setStatus_2("select");
    				controllDao.update(controllDto);

    				AodemiLogger.write("相手のselectです");
    			}

    		} else {
    			controllDto.setStatus_1("end");

//    			//targetが無い場合戦闘フェーズの場合、結果で次の行動が変わる可能性があるため、次の行動を再計算する
//            	HashMap<String, Object> reMap = new HashMap<String, Object>();
//
//            	if ("battle".equals(controllDto.getPhase())) {
//            		//次の行動の計算
//            		reMap = battleFieldUtil.getNextActionMap(controllDto, cardUtil);
//
//            		nextMap.put("nextPlayerId", reMap.get("playerId"));
//            		nextMap.put("nextFieldNumber", reMap.get("fieldNumber"));
//            		nextMap.put("autoUpdateInfo", reMap.get("autoUpdateInfo"));
//            		nextMap.put("autoTargetList", reMap.get("autoTargetList"));
//
//            	}
    		}

    		controllDto.setNext_action("start");

        	String out = stringUtil.getJsonStr(nextMap);

        	//次の行動順と更新内容を設定して処理を終了する。
    		//結果をoutに記入する
    		controllDto.setOut(out);

    		controllDao.update(controllDto);

    	} else {

    		//最初にnextActionをwaitにする
			controllDto.setNext_action("wait");
			controllDao.update(controllDto);

			AodemiLogger.write("相手の計算を待っています");

    		//相手が計算完了するまで待つ
    		while (!"start".equals(controllDto.getNext_action())) {
    			controllDto = controllDao.getAllValue(battleID);
        		//１秒停止
        		Thread.sleep(500);
        	}

    		//次の行動情報取得
    		String out = controllDto.getOut();

    		//取得した情報をMapに変換する
    		nextMap = stringUtil.getJsonMap(out);

    		if (nextMap.get("target") != null) {
    			target = (ArrayList<Object>)nextMap.get("target");
    		}

    		//対象選択がある場合
    		if (target.size() != 0) {

    			//自分が対象選択の場合はそのまま返却。それ以外は相手が終了するまで待機する。
    			if (!"select".equals(controllDto.getStatus_2())) {
    				AodemiLogger.write("相手のselectです");
    			}

    		} else {
    			//ステータスはreadyに戻す
    			controllDto.setStatus_1("ready");
    			controllDto.setStatus_2("ready");
    			//入力状況は初期化しておく
        		battleControllUtil.initPlayerInput(controllDto);
    		}

    		controllDto.setNext_action("");
    		controllDao.update(controllDto);
    	}

    	outMap.put("severCheack", true);
    	outMap.put("playerId", nextMap.get("playerId"));
    	outMap.put("fieldNumber", nextMap.get("fieldNumber"));
    	outMap.put("updateInfo", nextMap.get("updateInfo"));
    	outMap.put("targetList", target);

//    	outMap.put("nextPlayerId", nextMap.get("nextPlayerId"));
//    	outMap.put("nextFieldNumber", nextMap.get("nextFieldNumber"));
//    	outMap.put("autoUpdateInfo", nextMap.get("autoUpdateInfo"));
//    	outMap.put("autoTargetList", nextMap.get("autoTargetList"));

    	stringUtil.serializeJson(outMap, outputFile);

    	AodemiLogger.write("close終了");
	}

	//ギブアップ
	public void surrender(
			HashMap<String, Object> map,
			BattleFieldUtil battleFieldUtil,
			BattleControllUtil battleControllUtil,
			CardUtil cardUtil,
			StringUtil stringUtil) throws Exception, InterruptedException {

		String playerId = map.get("playerId").toString();

    	AodemiLogger.write("あなたはギブアップしました");

    	BattleControllDTO controllDto = controllDao.getAllValue(battleID);

    	HashMap<String, Object> outMap = new HashMap<String, Object>();
    	outMap.put("severCheack", "lose");

    	//自分の敗北情報を設定する
    	if (player1Flg) {
    		controllDto.setPlayer_surrend_1(1);
    	} else {
    		controllDto.setPlayer_surrend_2(1);
    	}

    	controllDao.update(controllDto);

    	stringUtil.serializeJson(outMap, outputFile);

	}

}
