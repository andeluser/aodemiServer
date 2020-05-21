package util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import card.CardAbility;
import cardDataObject.Card;
import cardDataObject.Deck;
import cardDataObject.Shield;
import dao.BattleBaseDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dao.BattleShieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import dto.BattleShieldDTO;
import factory.AbilityFactory;
import factory.DaoFactory;
import shield.ShieldAbility;

public class BattleFieldUtil {

	private String battleID = "T0001";
	private StringUtil stringUtil = null;

	BattleFieldDAO fieldDao = null;
	BattleDeckDAO  deckDao = null;
	BattleBaseDAO baseDao = null;
	BattleShieldDAO shieldDao = null;
	DaoFactory factory = null;

	public BattleFieldUtil() throws Exception {
		stringUtil = new StringUtil();
		factory = new DaoFactory();

		fieldDao = factory.createFieldDAO();
		deckDao = factory.createDeckDAO();
		baseDao = factory.createBaseDAO();
		shieldDao = factory.createShieldDAO();
	}

	//盤面情報の初期化(ベース)
	public void initBattleBase(boolean player1Flg, String playerId) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO dao = factory.createBaseDAO();

		BattleBaseDTO dto = new BattleBaseDTO();

		dto.setBattle_id("T0001");
		dto.setSub_no(1);
		dto.setPlayer_id(playerId);
		dto.setYellow(0);
		dto.setBlack(0);
		dto.setYellow(0);
		dto.setRed(0);
		dto.setBlue(0);
		dto.setSpecial_gage(0);
		dto.setSpecial_stock(0);
		dto.setSpecial_use(0);
		dto.setLife(0);
		dto.setSet_card("");
		dto.setSet_deck_no(0);
		dto.setCemetery("");
		dto.setDisappearance("");
		dto.setSp(3);
		dto.setTurn_up_sp(2);
		dto.setMagic(0);
		dto.setDivine(0);

		dao.deleteBattle(dto);
		dao.insert(dto);
	}

	//盤面情報の初期化(フィールド)
	public void initBattleField(boolean player1Flg, String playerId) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO dao = factory.createFieldDAO();

		BattleFieldDTO dto = new BattleFieldDTO();
		dto.setPlayer_id(playerId);
		dao.deleteBattle(dto);

		for (int i = 0; i < 9; i++) {
			dto.setBattle_id("T0001");
			dto.setPlayer_id(playerId);
			dto.setField_no(i);
			dto.setSub_no(1);
			dto.setNew_flg(1);
			dto.setCard_id("");
			dto.setClose(0);
			dto.setClose_number(0);
			dto.setOpen_close_number(0);
			dto.setClose_skill(0);
			dto.setStart_action(0);
			dto.setAction(0);
			dto.setColor("");
			dto.setType1("");
			dto.setType2("");
			dto.setPermanent_hp(0);
			dto.setTurn_hp(0);
			dto.setBase_hp(0);
			dto.setCur_hp(0);
			dto.setPermanent_level(0);
			dto.setTurn_level(0);
			dto.setCur_level(0);
			dto.setPermanent_atk(0);
			dto.setTurn_atk(0);
			dto.setCur_atk(0);
			dto.setPermanent_def(0);
			dto.setTurn_def(0);
			dto.setCur_def(0);
			dto.setPermanent_speed(0);
			dto.setTurn_speed(0);
			dto.setCur_speed(0);
			dto.setPermanent_range(0);
			dto.setTurn_range(0);
			dto.setCur_range(0);
			dto.setDeck_no(0);

			dao.insert(dto);
		}
	}

	//盤面情報の初期化(シールド)
	public void initBattleShield(boolean player1Flg, String playerId, Deck deckInfo) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleShieldDAO dao = factory.createShieldDAO();
		ArrayList<Shield> shieldList = deckInfo.getShieldList();

		BattleShieldDTO dto = new BattleShieldDTO();

		dto.setPlayer_id(playerId);
		dao.deleteBattle(dto);

		for (int i = 0; i < 5; i++) {
			dto.setBattle_id("T0001");
			dto.setPlayer_id(playerId);
			dto.setShield_no(i);
			dto.setSub_no(1);
			dto.setNew_flg(1);
			dto.setShield_id(shieldList.get(i).getId());
			dto.setShield_life(stringUtil.getIntForString(shieldList.get(i).getLife()));
			dto.setShield_select(0);
			dto.setShield_open(0);

			dao.insert(dto);
		}
	}

	//盤面情報の初期化(デッキ)
	public void initBattleDeck(boolean player1Flg, String playerId, Deck deckInfo) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleDeckDAO dao = factory.createDeckDAO();
		ArrayList<Card> cardList = deckInfo.getCardList();

		BattleDeckDTO dto = new BattleDeckDTO();

		dto.setPlayer_id(playerId);
		dao.deleteBattle(dto);

		StringUtil util = new StringUtil();

		for (int i = 0; i < 25; i++) {
			Card card = cardList.get(i);

			dto.setBattle_id("T0001");
			dto.setPlayer_id(playerId);
			dto.setDeck_no(i);
			dto.setSub_no(1);
			dto.setNew_flg(1);
			dto.setCard_id(card.getId());
			dto.setColor(card.getColor());
			dto.setCard_type(card.getType());
			dto.setCard_type1(card.getType1());
			dto.setCard_type2(card.getType2());
			dto.setLevel(util.getIntForString(card.getLevel()));
			dto.setStock(util.getIntForString(card.getStock()));
			dto.setHp(util.getIntForString(card.getHp()));
			dto.setAtk(util.getIntForString(card.getAtk()));
			dto.setDef(util.getIntForString(card.getDef()));
			dto.setAgi(util.getIntForString(card.getAgi()));
			dto.setRng(util.getIntForString(card.getRng()));
			dto.setCard_lock(0);
			dto.setCard_out(0);

			if (card.getOpenSkillName() != null &&  !"".equals(card.getOpenSkillName())) {
				dto.setOpen_skill(1);
			} else {
				dto.setOpen_skill(0);
			}

			if (card.getAutoSkillName() != null &&  !"".equals(card.getAutoSkillName())) {
				dto.setStart_skill(1);
			} else {
				dto.setStart_skill(0);
			}

			if (card.getCloseSkillName() != null &&  !"".equals(card.getCloseSkillName())) {
				dto.setClose_skill(1);
			} else {
				dto.setClose_skill(0);
			}

			dao.insert(dto);
		}
	}

	//盤面の基本情報取得
	public BattleBaseDTO getBaseValue(String playerId) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO dao = factory.createBaseDAO();

		BattleBaseDTO dto = dao.getAllValue(battleID, playerId);

		return dto;
	}

//	//盤面の属性情報更新
//	public BattleBaseDTO addMana(String playerId, String yellow, String black, String red, String blue) throws Exception {
//
//		DaoFactory factory = new DaoFactory();
//		BattleBaseDAO dao = factory.createBaseDAO();
//
//		BattleBaseDTO dto = dao.getAllValue(battleID, playerId);
//
//		dto.setYellow(dto.getYellow() + stringUtil.getIntForString(yellow));
//		dto.setBlack(dto.getBlack() + stringUtil.getIntForString(black));
//		dto.setRed(dto.getRed() + stringUtil.getIntForString(red));
//		dto.setBlue(dto.getBlue() + stringUtil.getIntForString(blue));
//
//		dao.update(dto);
//
//		return dto;
//
//	}


	//対象計算処理　射程から候補を計算する
	public ArrayList<Object> getRangeTargetList(BattleControllDTO controllDto, String playerId, int fieldNumber) throws Exception {

		ArrayList<Object> ret = new ArrayList<Object>();
		ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, playerId);

		//対象ユニットの射程を取得
		int unlimited = fieldList.get(fieldNumber).getPermanent_range();
		int limited = fieldList.get(fieldNumber).getTurn_range();
		int base = fieldList.get(fieldNumber).getCur_range();

		//射程の計算
		int rng = unlimited + limited + base;

		//ユニット配置情報を取得する
		//位置が4以上で1～3にユニットが居れば射程-1
		if (fieldNumber >= 4) {
			if ((!"".equals(fieldList.get(0).getCard_id()) && fieldList.get(0).getClose() == 0) ||
					(!"".equals(fieldList.get(1).getCard_id()) && fieldList.get(1).getClose() == 0) ||
					(!"".equals(fieldList.get(2).getCard_id()) && fieldList.get(2).getClose() == 0)) {
				rng = rng - 1;
			}
		}

		//位置が7以上で4～6にユニットが居れば射程-1
		if (fieldNumber >= 7) {
			if ((!"".equals(fieldList.get(3).getCard_id()) && fieldList.get(3).getClose() == 0) ||
					(!"".equals(fieldList.get(4).getCard_id()) && fieldList.get(4).getClose() == 0) ||
					(!"".equals(fieldList.get(5).getCard_id()) && fieldList.get(5).getClose() == 0)) {
				rng = rng - 1;
			}
		}

		String enemyPlayer = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayer = controllDto.getPlayer_id_2();
		} else {
			enemyPlayer = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> enemyFieldList = fieldDao.getAllList(battleID, enemyPlayer);

		//射程が１以上なら対象を計算する
		if (rng > 0) {

			for (int row = 0; row < 3; row++) {

				//射程ゼロなら処理中断
				if (rng <= 0) {
					break;
				}

				boolean existFlg = false;

				for (int column = 0; column < 3; column++) {
					//ユニットが居れば対象
					BattleFieldDTO dto = enemyFieldList.get((row * 3) + column);
					if (!"".equals(dto.getCard_id()) && dto.getClose() == 0) {

						ret.add((row * 3) + column);
						existFlg = true;

					}
				}

				//ユニットが居た場合は射程をマイナスする
				if (existFlg) {
					rng = rng - 1;
				}
			}
		}

		return ret;
	}

	//攻撃判定処理
	public HashMap<String, Object> attack(BattleControllDTO controllDto, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();
		ArrayList<Object> targetList = new ArrayList<Object>();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//射程から相手の候補を計算する
		targetList = getRangeTargetList(controllDto, playerId, fieldNumber);

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//攻撃力を取得
			int atack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

			//攻撃対象のHPを減らす
			String enemyPlayer = "";
			if (playerId.equals(controllDto.getPlayer_id_1())) {
				enemyPlayer = controllDto.getPlayer_id_2();
			} else {
				enemyPlayer = controllDto.getPlayer_id_1();
			}

			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayer, target);

			//HP
			int enemyHp = enemyFieldDto.getCur_hp();
			//DEF
			int enemyDef = enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def();

			//ダメージを算出
			int damage = atack - enemyDef;

			if (damage < 0) {
				damage = 0;
			}

			//HPを計算
			enemyHp = enemyHp - damage;

			//相手のHPを設定
			enemyFieldDto.setCur_hp(enemyHp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (enemyHp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			//両方の状態を更新する。
			fieldDao.update(fieldDto);
			fieldDao.update(enemyFieldDto);

			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayer);
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
			updateList.add(updateMap);

			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);

		}

		return ret;

	}

	//待機処理
	public HashMap<String, Object> rest(BattleControllDTO controllDto, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//最大HPの計算
		int max = fieldDto.getPermanent_hp() + fieldDto.getTurn_hp() + fieldDto.getBase_hp();

		//回復量の計算
		BigDecimal cureHp = new BigDecimal(max).multiply(new BigDecimal(0.25d));
		cureHp.setScale(0, BigDecimal.ROUND_HALF_UP);

		//現在HPに加算する
		int current = fieldDto.getCur_hp() + cureHp.intValue();

		//最大を超える場合は最大とする
		if (current > max) {
			current = max;
		}

		//計算結果で現在HPを更新
		fieldDto.setCur_hp(current);

		fieldDao.update(fieldDto);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		//返却用マップを作成
		updateMap.put("fieldNumber", fieldNumber);
		updateMap.put("hp", current);
		updateMap.put("playerId", playerId);
		ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
		updateList.add(updateMap);

		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		ArrayList<Object> orderList = new ArrayList<Object>();

		orderMap.put("field", updateList);
		orderList.add(orderMap);

		ret.put("updateInfo", orderList);

		return ret;
	}

	//アクションスキル処理
	public HashMap<String, Object> actionSkill(BattleControllDTO controllDto, String playerId, int fieldNumber, String battleAction) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		HashMap<String, Object> result = new HashMap<String, Object>();

		AbilityFactory ability = new AbilityFactory();
		CardAbility cardAbility = ability.getCardAbility(fieldDto.getCard_id());

		if ("actionSkill1".equals(battleAction)) {
			//アクションスキル１
			result = cardAbility.action1(battleID, playerId, fieldNumber);
		} else {
			//アクションスキル２
			result = cardAbility.action2(battleID, playerId, fieldNumber);
		}

		ret.put("target", result.get("target"));
		ret.put("updateInfo", result.get("updateInfo"));

		//行動済判定を立てる
		fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
		fieldDto.setAction(1);

		fieldDao.update(fieldDto);

		return ret;
	}

	//移動処理
	public HashMap<String, Object> move(BattleControllDTO controllDto, String playerId, int pre, int aft) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, pre);

		//移動先を更新
		fieldDto.setField_no(aft);
		fieldDto.setAction(1);
		fieldDao.update(fieldDto);

//		//移動前は消す
		fieldDto.setField_no(pre);
		fieldDto.setSub_no(1);
		fieldDto.setNew_flg(1);
		fieldDto.setCard_id("");
		fieldDto.setClose(0);
		fieldDto.setClose_number(0);
		fieldDto.setOpen_close_number(0);
		fieldDto.setClose_skill(0);
		fieldDto.setStart_action(0);
		fieldDto.setAction(0);
		fieldDto.setColor("");
		fieldDto.setType1("");
		fieldDto.setType2("");
		fieldDto.setPermanent_hp(0);
		fieldDto.setTurn_hp(0);
		fieldDto.setBase_hp(0);
		fieldDto.setCur_hp(0);
		fieldDto.setPermanent_level(0);
		fieldDto.setTurn_level(0);
		fieldDto.setCur_level(0);
		fieldDto.setPermanent_atk(0);
		fieldDto.setTurn_atk(0);
		fieldDto.setCur_atk(0);
		fieldDto.setPermanent_def(0);
		fieldDto.setTurn_def(0);
		fieldDto.setCur_def(0);
		fieldDto.setPermanent_speed(0);
		fieldDto.setTurn_speed(0);
		fieldDto.setCur_speed(0);
		fieldDto.setPermanent_range(0);
		fieldDto.setTurn_range(0);
		fieldDto.setCur_range(0);
		fieldDto.setDeck_no(0);

		fieldDao.update(fieldDto);

		//返却値を設定
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("fieldNumber", pre);
		detailMap.put("move", aft);
		detailMap.put("playerId", playerId);

		ArrayList<Object> mylist = new ArrayList<Object>();
		mylist.add(detailMap);

		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("field", mylist);

		ArrayList<Object> updateList = new ArrayList<Object>();
		updateList.add(myMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;

	}

	//リムーブ処理
	public HashMap<String, Object> remove(BattleControllDTO controllDto, CardUtil cardUtil) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		//盤面情報から、次の処理順を判定する。
//		int removePlayerNumber = 0;
//		String removePlayer = "";
//		int removeIndex = 0;
//		ArrayList<String> list = new ArrayList<String>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		//次のクローズ対象を取得する
		BattleFieldDTO dto = fieldDao.getNextClose(battleID);

//		for (int index = 0; index < 9; index++) {
//
//			//処理している盤面位置
//			removeIndex = index;
//
//			BattleFieldDTO dto1 = fieldDao.getAllValue(battleID, controllDto.getPlayer_id_1(), index);
//
//			//プレイヤー１の情報がクローズ対象となっている場合
//			if (dto1.getCard_id() != null && !"".equals(dto1.getCard_id()) && dto1.getClose() == 1) {
//				list.add("1");
//			}
//
//			//プレイヤー２の情報がクローズ対象となっている場合
//			BattleFieldDTO dto2 = fieldDao.getAllValue(battleID, controllDto.getPlayer_id_2(), index);
//
//			if (dto2.getCard_id() != null && !"".equals(dto2.getCard_id()) && dto2.getClose() == 1) {
//				list.add("2");
//			}
//
//			if (list.size() != 0) {
//
//				if (list.size() > 1) {
//
//					//サイズが２以上の場合は複数リムーブ候補が居るのでどちらを処理するか決める
//					Random rand = new Random();
//					int num = rand.nextInt(2);
//
//					removePlayerNumber = Integer.parseInt(list.get(num));
//				} else {
//					removePlayerNumber = Integer.parseInt(list.get(0));
//				}
//
//				//リムーブ対象が居た時点で一旦処理を終了する。
//				break;
//			}
//		}
//
//		if (removePlayerNumber == 1) {
//			removePlayer = controllDto.getPlayer_id_1();
//		} else {
//			removePlayer = controllDto.getPlayer_id_2();
//		}

		//リムーブ対象が無かった場合は、リムーブフェーズは終了
//		if (removePlayerNumber == 0) {
		if (dto == null || dto.getCard_id() == null || "".equals(dto.getCard_id())) {
			//次の行動にendを設定する
			ret.put("updateInfo", new HashMap<String, Object>());
			ret.put("fieldNumber", null);

		} else {
			String removePlayer = dto.getPlayer_id();
			int removeFieldNumber = dto.getField_no();
			int removeIndex = dto.getDeck_no();
			int level = stringUtil.getIntForString(cardUtil.getCard(dto.getCard_id()).getLevel());

			//リムーブするデッキ番号を墓地に追記
			//カード番号を墓地に書き込む
			BattleBaseDTO baseDto = baseDao.getAllValue(battleID, removePlayer);
			baseDto.setCemetery(addCardNumberForCamma(baseDto.getCemetery(), removeIndex));

			//SPをレベル分増加させる
			baseDto.setSp(baseDto.getSp() + level);
			baseDao.update(baseDto);

			//盤面からカードの情報を削除
			BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, removePlayer, removeFieldNumber);

			fieldDto.setCard_id("");
			fieldDto.setClose(0);
			fieldDto.setClose_number(0);
			fieldDto.setOpen_close_number(0);
			fieldDto.setClose_skill(0);
			fieldDto.setColor("");;
			fieldDto.setType1("");;
			fieldDto.setType2("");;
			fieldDto.setStart_action(0);
			fieldDto.setAction(0);
			fieldDto.setPermanent_hp(0);
			fieldDto.setTurn_hp(0);
			fieldDto.setBase_hp(0);
			fieldDto.setCur_hp(0);
			fieldDto.setPermanent_level(0);
			fieldDto.setTurn_level(0);
			fieldDto.setCur_level(0);
			fieldDto.setPermanent_atk(0);
			fieldDto.setTurn_atk(0);
			fieldDto.setCur_atk(0);
			fieldDto.setPermanent_def(0);
			fieldDto.setTurn_def(0);
			fieldDto.setCur_def(0);
			fieldDto.setPermanent_speed(0);
			fieldDto.setTurn_speed(0);
			fieldDto.setCur_speed(0);
			fieldDto.setPermanent_range(0);
			fieldDto.setTurn_range(0);
			fieldDto.setCur_range(0);
			fieldDto.setDeck_no(0);

			//シールド処理を呼び出す（99の場合は左から順にシールドを減らす）
			HashMap<String, Object> shieldUpdateInfo = activateShield(removePlayer, 99);

			fieldDao.update(fieldDto);

			//更新
			ret.put("playerId", shieldUpdateInfo.get("playerId"));
			ret.put("updateInfo", shieldUpdateInfo.get("updateInfo"));
			ret.put("target", shieldUpdateInfo.get("target"));
			ret.put("shieldId", shieldUpdateInfo.get("shieldId"));
			ret.put("fieldNumber", removeFieldNumber);

		}

		return ret;

	}

	//復活処理
	public void revial(String playerId, ArrayList<Boolean> revivalNumber, CardUtil cardUtil, BattleBaseDTO baseDto) throws Exception {

		ExpansionUtil expansion = new ExpansionUtil();

		for (int index = 0; index < revivalNumber.size() ; index++) {
			Boolean flg = revivalNumber.get(index);

			if (flg) {

				BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, index);

				if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id())) {

					Card card = cardUtil.getCard(fieldDto.getCard_id());

					//復活に伴う初期化処理
					fieldDto.setClose(0);
					if (card.getStartSkillName() != null &&  !"".equals(card.getStartSkillName())) {
						fieldDto.setStart_action(0);
					} else {
						fieldDto.setStart_action(1);
					}

					fieldDto.setClose_number(0);
					fieldDto.setOpen_close_number(0);

					if (card.getCloseSkillName() != null &&  !"".equals(card.getCloseSkillName())) {
						fieldDto.setClose_skill(0);
					} else {
						fieldDto.setClose_skill(1);
					}
					fieldDto.setAction(0);
					fieldDto.setPermanent_hp(0);
					fieldDto.setTurn_hp(0);
					fieldDto.setCur_hp(fieldDto.getBase_hp());
					fieldDto.setPermanent_level(0);
					fieldDto.setTurn_level(0);
					fieldDto.setPermanent_atk(0);
					fieldDto.setTurn_atk(0);
					fieldDto.setPermanent_def(0);
					fieldDto.setTurn_def(0);
					fieldDto.setPermanent_speed(0);
					fieldDto.setTurn_speed(0);
					fieldDto.setPermanent_range(0);
					fieldDto.setTurn_range(0);

					fieldDao.update(fieldDto);

					//復活時のSP減少確認
					if (expansion.returnCheack(fieldDto.getCard_id())) {
						//復帰持ちの場合はSPを減らさない
					} else {
						baseDto.setSp(baseDto.getSp() - 1);
					}

				}
			}
		}
	}


	//墓地、消滅へのカード番号追加
	private String addCardNumberForCamma(String value, int addValue) {

		String ret = "";

		if (value != null && !"".equals(value)) {
			value = value + "," + addValue;
		} else {
			value = "" + addValue;
		}

		ret = value;

		return ret;

	}


	//シールド発動処理（結果的に発動しない場合もあり）
	private HashMap<String, Object> activateShield(String playerId, int breakShieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();
		String shieldId = "";

		HashMap<String, Object> updateInfo = new HashMap<String, Object>();

		if (breakShieldNumber != 99) {

			BattleShieldDTO shieldDto = shieldDao.getAllValue(battleID, playerId, breakShieldNumber);

			//シールドブレイク処理
			updateInfo = breakShield (playerId, breakShieldNumber);
			shieldId = shieldDto.getShield_id();
		} else {
			for (int index = 0; index < 5; index++) {

				BattleShieldDTO shieldDto = shieldDao.getAllValue(battleID, playerId, index);

				//シールドが開いているかを判定し、１なら次のループへ
				if (shieldDto.getShield_open() == 1){
					continue;
				} else {
					//シールドブレイク処理
					updateInfo = breakShield (playerId, index);
					shieldId = shieldDto.getShield_id();

					//シールド処理が完了したらループは抜ける
					break;
				}
			}
		}

		//返却用マップを作成
		ret.put("playerId", playerId);
		ret.put("updateInfo", updateInfo.get("updateInfo"));
		ret.put("shieldId", shieldId);
		ret.put("target", updateInfo.get("target"));

		return ret;
	}

	public HashMap<String, Object> breakShield (String playerId, int index) throws Exception {

		BattleShieldDTO shieldDto = shieldDao.getAllValue(battleID, playerId, index);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		int shieldLife = shieldDto.getShield_life() - 1;

		if (shieldLife <= 0) {
			//シールドクローズ判定を１にする
			shieldDto.setShield_open(1);

			//シールドスキルを呼び出す
			HashMap<String, Object> result = new HashMap<String, Object>();

			AbilityFactory ability = new AbilityFactory();
			ShieldAbility shieldAbility = ability.getShieldAbility(shieldDto.getShield_id());

			result = shieldAbility.shieldSkill(battleID, playerId);

    		updateMap.put("shieldId", shieldDto.getShield_id());
    		updateMap.put("updateInfo", result.get("updateInfo"));
    		updateMap.put("target", result.get("target"));
		}

		//減らしたライフを設定する
		shieldDto.setShield_life(shieldLife);

		shieldDao.update(shieldDto);

		return updateMap;

	}


	//盤面にカードをセットする
	public void updateSetCard(String playerId, Card cardInfo, int setPositon, int deckNumber) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO dao = factory.createFieldDAO();

		BattleFieldDTO dto = new BattleFieldDTO();

		dto.setBattle_id(battleID);
		dto.setPlayer_id(playerId);
		dto.setField_no(setPositon);
		dto.setSub_no(1);
		dto.setNew_flg(1);
		dto.setCard_id(cardInfo.getId());
		dto.setClose(0);
		dto.setClose_number(0);
		dto.setOpen_close_number(0);

		//スタートスキルがある場合は１
		if (cardInfo.getStartSkillName() != null && !"".equals(cardInfo.getStartSkillName()))  {
			dto.setStart_action(0);
		} else {
			dto.setStart_action(1);
		}

		//クローズスキルがある場合は１
		if (cardInfo.getCloseSkillName() != null && !"".equals(cardInfo.getCloseSkillName()))  {
			dto.setClose_skill(0);
		} else {
			dto.setClose_skill(1);
		}
		dto.setAction(0);
		dto.setColor(cardInfo.getColor());
		dto.setType1(cardInfo.getType1());
		dto.setType2(cardInfo.getType2());
		dto.setPermanent_hp(0);
		dto.setTurn_hp(0);
		dto.setBase_hp(stringUtil.getIntForString(cardInfo.getHp()));
		dto.setCur_hp(stringUtil.getIntForString(cardInfo.getHp()));
		dto.setPermanent_level(0);
		dto.setTurn_level(0);
		dto.setCur_level(stringUtil.getIntForString(cardInfo.getLevel()));
		dto.setPermanent_atk(0);
		dto.setTurn_atk(0);
		dto.setCur_atk(stringUtil.getIntForString(cardInfo.getAtk()));
		dto.setPermanent_def(0);
		dto.setTurn_def(0);
		dto.setCur_def(stringUtil.getIntForString(cardInfo.getDef()));
		dto.setPermanent_speed(0);
		dto.setTurn_speed(0);
		dto.setCur_speed(stringUtil.getIntForString(cardInfo.getAgi()));
		dto.setPermanent_range(0);
		dto.setTurn_range(0);
		dto.setCur_range(stringUtil.getIntForString(cardInfo.getRng()));
		dto.setDeck_no(deckNumber);

		dao.update(dto);
	}

	//盤面から未行動の一覧を取得し、行動順計算を行い返却する
	public HashMap<String, Object> getNextActionMap(BattleControllDTO controllDto, CardUtil cardUtil) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();
		ArrayList<ArrayList<Object>> actionList = new ArrayList<ArrayList<Object>>();

		//最大速度
		int maxAgi = 0;

		ArrayList<BattleFieldDTO> player1List = fieldDao.getAllList(battleID, controllDto.getPlayer_id_1());

		//プレイヤー１の未行動一覧から速度が最も早いユニットを探す
		for (int i = 0; i < player1List.size(); i++) {

			BattleFieldDTO dto = player1List.get(i);

			//クローズしておらず、行動済でもない場合
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getAction() == 0 && dto.getClose() == 0) {
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

				//カードID
				koList.add(dto.getCard_id());

				actionList.add(koList);
			}

		}

		//プレイヤー２の未行動一覧から速度が最も早いユニットを探す
		ArrayList<BattleFieldDTO> player2List = fieldDao.getAllList(battleID, controllDto.getPlayer_id_2());

		for (int i = 0; i < player2List.size(); i++) {

			BattleFieldDTO dto = player2List.get(i);

			//クローズしておらず、行動済でもない場合
			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getAction() == 0 && dto.getClose() == 0) {
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

				//速度
				koList.add(sumAgi);

				//プレイヤーID
				koList.add(controllDto.getPlayer_id_2());

				//盤面位置
				koList.add(i);

				//カードID
				koList.add(dto.getCard_id());

				actionList.add(koList);
			}
		}

		String carId = "";
		//候補が居ない場合はendを返却
		if (actionList.size() == 0) {
			ret.put("playerId", "end");
			ret.put("fieldNumber", null);
		} else {
			//最大速度の一覧が作成されたので、その中からランダムで一つ選ぶ
			Random rand = new Random();
			int num = rand.nextInt(actionList.size());

			ret.put("playerId", actionList.get(num).get(1));
			ret.put("fieldNumber", actionList.get(num).get(2));

			carId = actionList.get(num).get(3).toString();
		}

		ret.put("autoUpdateInfo", new HashMap());
		ret.put("autoTargetList", new ArrayList());

		//次の行動が決まった場合、対象がオートスキルを保持してるかをチェックし更新内容を決める
		if (!"".equals(carId)) {
			Card card = cardUtil.getCard(carId);

			//オートスキルが存在した場合は更新内容を確認する
			if (card.getAutoSkillName() != null && !"".equals(card.getAutoSkillName())) {

				AbilityFactory open = new AbilityFactory();
				CardAbility cardAbility = open.getCardAbility(carId);

				HashMap<String, Object> result = cardAbility.auto(battleID, (String)ret.get("playerId"), (int)ret.get("fieldNumber"));

				if (result != null) {
					ret.put("autoUpdateInfo", result.get("updateInfo"));
					ret.put("autoTargetList", result.get("target"));
				}
			}
		}

		return ret;
	}

	//セット時にターン限定情報を初期化する
	public void initTurnStartInfo(String playerId, CardUtil cardUtil) throws Exception {

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO dao = factory.createFieldDAO();

		ArrayList<BattleFieldDTO> list = new ArrayList<BattleFieldDTO>();

		//ターン限定の情報を初期化する
		for (int index = 0; index < 9; index++) {
			//カードセットの情報がある場合
			BattleFieldDTO dto = dao.getAllValue(battleID, playerId, index);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id())) {
				Card card = cardUtil.getCard(dto.getCard_id());

				if (card.getStartSkillName() != null && !"".equals(card.getStartSkillName())) {
					dto.setStart_action(0);
				} else {
					dto.setStart_action(1);
				}
				if (card.getCloseSkillName() != null && !"".equals(card.getCloseSkillName())) {
					dto.setClose_skill(0);
				} else {
					dto.setClose_skill(1);
				}
				dto.setAction(0);
				dto.setTurn_atk(0);
				dto.setTurn_def(0);
				dto.setTurn_hp(0);
				dto.setTurn_level(0);
				dto.setTurn_range(0);
				dto.setTurn_speed(0);

				list.add(dto);
			}
		}

		dao.update(list);

	}
}
