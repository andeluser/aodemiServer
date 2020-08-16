package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//自爆特攻
public class b37 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleFieldDAO fieldDao = factory.createFieldDAO();

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> myFieldDtoList = fieldDao.getAllList(battleID, playerId);
		ArrayList myTargetList = new ArrayList();

		for (int i = 0; i < myFieldDtoList.size(); i++) {
			BattleFieldDTO dto = myFieldDtoList.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				myTargetList.add(dto.getField_no());
			}
		}

		ArrayList<BattleFieldDTO> enemyFieldDto = fieldDao.getAllList(battleID, enemyPlayerId);
		ArrayList enemyTargetList = new ArrayList();
		for (int i = 0; i < enemyFieldDto.size(); i++) {
			BattleFieldDTO dto = enemyFieldDto.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				enemyTargetList.add(dto.getField_no());
			}
		}

		//対象が一人も居ないなら、選択しない
		if (myTargetList.size() == 0 && enemyTargetList.size() == 0) {
			return new HashMap();
		}

		//自分の対象
		HashMap<String, Object> targetMap = new HashMap<String, Object>();
		targetMap.put("list", myTargetList);
		targetMap.put("playerId", playerId);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(targetMap);

		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		//相手の対象
		HashMap<String, Object> targetMap2 = new HashMap<String, Object>();
		targetMap2.put("list", enemyTargetList);
		targetMap2.put("playerId", enemyPlayerId);

		ArrayList<Object> retTargetList2 = new ArrayList<Object>();
		retTargetList2.add(targetMap2);

		HashMap<String, Object> retMap2 = new HashMap<String, Object>();
		retMap2.put("selectCount", 1);
		retMap2.put("targetList", retTargetList2);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		if (retMap2.size() != 0) {
			retList.add(retMap2);
		}

		ret.put("updateInfo", new HashMap());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));
					int hp = fieldDto.getCur_hp();

					int attack = 999;
					attack = attack - fieldDto.getPermanent_def() - fieldDto.getTurn_def() - fieldDto.getCur_def();

					if (attack <= 0) {
						attack = 0;
					}

					hp = hp - attack;

					fieldDto.setCur_hp(hp);

					//Hpが１以下ならクローズ情報を設定
					if (fieldDto.getCur_hp() <= 0) {
						fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
					}

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

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
				return null;
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
	public HashMap<String, Object> actionSelect2(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
