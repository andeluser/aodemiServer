package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//精鋭弓兵
public class m32 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				enemyTargetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() == 0 && enemyTargetList.size() == 0) {

			//対象が居ない場合、相手のストックを１増やす
			BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
			int stock = enemyBaseDto.getSpecial_stock() + 1;

			if (stock >= 5) {
				stock = 5;
			}

			enemyBaseDto.setSpecial_stock(stock);
			baseDao.update(enemyBaseDto);

			HashMap<String, Object> detailMapSpecial = new HashMap<String, Object>();
			detailMapSpecial.put("playerId", enemyPlayerId);
			detailMapSpecial.put("stock", enemyBaseDto.getSpecial_stock());

			ArrayList<Object> listSpecial = new ArrayList<Object>();
			listSpecial.add(detailMapSpecial);

			HashMap<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("special", listSpecial);

			ArrayList<Object> updateList = new ArrayList<Object>();
			updateList.add(updateMap);

			ret.put("updateInfo", new HashMap());
			ret.put("target", new ArrayList());

			return ret;
		}

		HashMap<String, Object> enemyTarget = new HashMap<String, Object>();
		enemyTarget.put("playerId", enemyPlayerId);
		enemyTarget.put("list", enemyTargetList);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(enemyTarget);

		HashMap<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		//更新は墓地送還のみ
		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		ret.put("updateInfo", new HashMap());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		//処理前にストックを増加させる
		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		int stock = enemyBaseDto.getSpecial_stock() + 1;

		if (stock > 5) {
			stock = 5;
		}

		enemyBaseDto.setSpecial_stock(stock);
		baseDao.update(enemyBaseDto);

		HashMap<String, Object> detailMapSpecial = new HashMap<String, Object>();
		detailMapSpecial.put("playerId", enemyPlayerId);
		detailMapSpecial.put("stock", enemyBaseDto.getSpecial_stock());

		ArrayList<Object> listSpecial = new ArrayList<Object>();
		listSpecial.add(detailMapSpecial);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("special", listSpecial);

		//戻り値設定
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のユニットにダメージ
					int def = fieldDto.getPermanent_def() + fieldDto.getTurn_def() + fieldDto.getCur_def();

					int attack = enemyBaseDto.getSpecial_stock() * 10;

					attack = attack - def;
					if (attack < 0) {
						attack = 0;
					}

					int hp = fieldDto.getCur_hp();
					hp = hp - attack;

					if (hp <= 0) {
						fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
					}

					fieldDto.setCur_hp(hp);
					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", hp);
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
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		int special_use = baseDto.getSpecial_use();
		int special_use_bye_skill = baseDto.getSpecial_use_bye_skill();

		//このターンに奥義が発動していない場合は処理しない
		if(special_use != 1 && special_use_bye_skill != 1) {
			return ret;
		}

		//奥義ストックを１増やす
		int stock = baseDto.getSpecial_stock() + 1;

		if (stock > 5) {
			stock = 5;
		}

		baseDto.setSpecial_stock(stock);
		baseDao.update(baseDto);

		HashMap<String, Object> detailMapSpecial = new HashMap<String, Object>();
		detailMapSpecial.put("playerId", playerId);
		detailMapSpecial.put("stock", stock);

		ArrayList<Object> listSpecial = new ArrayList<Object>();
		listSpecial.add(detailMapSpecial);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("special", listSpecial);

		ArrayList<Object> updateList = new ArrayList<Object>();
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;

	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action2(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> close(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> closeSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
