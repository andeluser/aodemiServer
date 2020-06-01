package shield;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//トラップボックス
public class m54 implements ShieldAbility {

	@Override
	public HashMap<String, Object> shieldSkill(String battleID, String playerId) throws Exception {
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

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				targetList.add(fieldDtoList.get(i).getField_no());
			}
		}

		if (targetList.size() == 0) {
			return ret;
		}

		HashMap<String, Object> myTarget = new HashMap<String, Object>();
		myTarget.put("playerId", enemyPlayerId);
		myTarget.put("list", targetList);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(myTarget);

		HashMap<String, Object> retMap = new HashMap<String, Object>();

		//フェーズによって対象選択数を変える
		if ("remove".equals(controllDTO.getPhase())) {

			retMap.put("selectCount", 1);

		} else {

			if (targetList.size() >= 2) {
				retMap.put("selectCount", 2);
			} else {
				retMap.put("selectCount", 1);
			}

		}

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
	public HashMap<String, Object> shieldSkillSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					if ("remove".equals(controllDTO.getPhase())) {
						//リムーブフェーズの場合は40ダメージ
						int def = fieldDto.getCur_def() + fieldDto.getTurn_def() + fieldDto.getPermanent_def();
						fieldDto.setCur_hp(fieldDto.getCur_hp() + def - 40);

						if (fieldDto.getCur_hp() <= 0) {
							fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
						}
					} else {
						//リムーブフェーズ以外の場合は100ダメージ
						int def = fieldDto.getCur_def() + fieldDto.getTurn_def() + fieldDto.getPermanent_def();
						fieldDto.setCur_hp(fieldDto.getCur_hp() + def - 100);

						if (fieldDto.getCur_hp() <= 0) {
							fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
						}
					}

					//DBを更新
					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", fieldDto.getCur_hp());
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

}
