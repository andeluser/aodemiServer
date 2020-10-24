package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

public class b60 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();

		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		ArrayList retTargetList = new ArrayList();

		//自分の対象計算
		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 0) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() != 0) {
			HashMap<String, Object> target = new HashMap<String, Object>();
			target.put("playerId", playerId);
			target.put("list", targetList);
			retTargetList.add(target);
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 0) {
				enemyTargetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		if (enemyTargetList.size() != 0) {
			HashMap<String, Object> enemyTarget = new HashMap<String, Object>();
			enemyTarget.put("playerId", enemyPlayerId);
			enemyTarget.put("list", enemyTargetList);
			retTargetList.add(enemyTarget);
		}

		if (enemyTargetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		HashMap<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		//戻り値設定
		ret.put("updateInfo", new HashMap<String, Object>());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//デッキに戻す
					BattleDeckDTO deckDto = deckDao.getAllValue(battleID, player1, fieldDto.getDeck_no());
					deckDto.setCard_out(0);
					deckDao.update(deckDto);

					//盤面から削除する
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
					fieldDto.setPermanent_frm(0);
					fieldDto.setTurn_frm(0);
					fieldDto.setCur_frm(0);
					fieldDto.setDeck_no(0);

					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("remove", "deck");
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストック４以外は発動しない
		if (baseDto.getSpecial_stock() != 4) {
			return ret;
		}

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		ArrayList targetList = new ArrayList();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 0) {
				targetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		ArrayList<Object> updateList = new ArrayList<Object>();
		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		if (targetList.size() != 0) {
			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//行動済みにする
			BattleFieldDTO dto = enemyFieldDtoList.get(target);
			dto.setAction(1);
			fieldDao.update(dto);

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			ArrayList retList = new ArrayList();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", target);
			detailMap.put("remove", "actionEnd");
			retList.add(detailMap);

			updateMap.put("field", retList);
			updateList.add(updateMap);
		}

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList retList2 = new ArrayList();
		HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
		HashMap<String, Object> detailMap3 = new HashMap<String, Object>();

		//自分と相手のストックを２にする
		baseDto.setSpecial_stock(2);
		baseDao.update(baseDto);

		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		enemyBaseDto.setSpecial_stock(2);
		baseDao.update(enemyBaseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);

		detailMap3.put("playerId", enemyPlayerId);
		detailMap3.put("special", enemyBaseDto.getSpecial_stock());
		retList2.add(detailMap3);

		updateMap2.put("special", retList2);
		updateList.add(updateMap2);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストック２以外は発動しない
		if (baseDto.getSpecial_stock() != 2) {
			return ret;
		}

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		ArrayList targetList = new ArrayList();

		ArrayList<Object> updateList = new ArrayList<Object>();
		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();

		BattleFieldDTO myFieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
		int attack = myFieldDto.getPermanent_atk() + myFieldDto.getTurn_atk() + myFieldDto.getCur_atk();
		attack = attack * 2;

		ArrayList retList = new ArrayList();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {

			BattleFieldDTO dto = enemyFieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && dto.getAction() == 1) {
				int def =dto.getPermanent_def() + dto.getTurn_def() + dto.getCur_def();

				attack = attack - def;

				if (attack < 0) {
					attack = 0;
				}

				int enemyHp = dto.getCur_hp();

				//HPを計算
				enemyHp = enemyHp - attack;

				//相手のHPを設定
				dto.setCur_hp(enemyHp);

				//相手のHPがゼロ以下となった場合はクローズ判定を立てる
				if (enemyHp <= 0) {
					fieldDao.setClose(battleID, dto.getPlayer_id(), dto.getField_no(), dto);
				}

				fieldDao.update(dto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", i);
				detailMap.put("hp", dto.getCur_hp());
				retList.add(detailMap);
			}

		}

		if (retList.size() != 0) {
			updateMap.put("field", retList);
			updateList.add(updateMap);
		}

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList retList2 = new ArrayList();
		HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
		HashMap<String, Object> detailMap3 = new HashMap<String, Object>();

		//自分と相手のストックを３にする
		baseDto.setSpecial_stock(3);
		baseDao.update(baseDto);

		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		enemyBaseDto.setSpecial_stock(3);
		baseDao.update(enemyBaseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);

		detailMap3.put("playerId", enemyPlayerId);
		detailMap3.put("special", enemyBaseDto.getSpecial_stock());
		retList2.add(detailMap3);

		updateMap2.put("special", retList2);
		updateList.add(updateMap2);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストック3以外は発動しない
		if (baseDto.getSpecial_stock() != 3) {
			return ret;
		}

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		ArrayList targetList = new ArrayList();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			int level = list.getPermanent_level() + list.getTurn_level() + list.getCur_level();
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && level <= 4) {
				targetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		//対象なしの場合は処理終了
		ArrayList<Object> updateList = new ArrayList<Object>();

		if (targetList.size() == 0) {

			HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
			ArrayList retList2 = new ArrayList();
			HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
			HashMap<String, Object> detailMap3 = new HashMap<String, Object>();

			//自分と相手のストックを４にする
			baseDto.setSpecial_stock(4);
			baseDao.update(baseDto);

			BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
			enemyBaseDto.setSpecial_stock(4);
			baseDao.update(enemyBaseDto);

			detailMap2.put("playerId", playerId);
			detailMap2.put("stock", baseDto.getSpecial_stock());
			retList2.add(detailMap2);

			detailMap3.put("playerId", enemyPlayerId);
			detailMap3.put("special", enemyBaseDto.getSpecial_stock());
			retList2.add(detailMap3);

			updateMap2.put("special", retList2);
			updateList.add(updateMap2);

			ret.put("updateInfo", updateList);
			ret.put("target", new ArrayList<Object>());

			return ret;
		}

		HashMap<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("selectCount", 1);
		retMap.put("targetList", targetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		//戻り値設定
		ret.put("updateInfo", new HashMap<String, Object>());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//デッキに戻す
					BattleDeckDTO deckDto = deckDao.getAllValue(battleID, player1, fieldDto.getDeck_no());
					deckDto.setCard_out(0);
					deckDao.update(deckDto);

					//盤面から削除する
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
					fieldDto.setPermanent_frm(0);
					fieldDto.setTurn_frm(0);
					fieldDto.setCur_frm(0);
					fieldDto.setDeck_no(0);

					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("remove", "deck");
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		//奥義ゲージを４にする
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList retList2 = new ArrayList();
		HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
		HashMap<String, Object> detailMap3 = new HashMap<String, Object>();

		//自分と相手のストックを４にする
		baseDto.setSpecial_stock(4);
		baseDao.update(baseDto);

		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		enemyBaseDto.setSpecial_stock(4);
		baseDao.update(enemyBaseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);

		detailMap3.put("playerId", enemyPlayerId);
		detailMap3.put("special", enemyBaseDto.getSpecial_stock());
		retList2.add(detailMap3);

		updateMap2.put("special", retList2);
		updateList.add(updateMap2);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
