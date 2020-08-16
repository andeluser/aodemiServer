package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//影縛り
public class b24 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		ArrayList<Object> myretTargetList = new ArrayList<Object>();
		ArrayList<Object> enemyretTargetList = new ArrayList<Object>();

		//相手プレイヤーのID取得
		String enemyPlayerId = playerId;
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//対象を計算する
		//自分の対象計算
		ArrayList<BattleFieldDTO> myFieldDtoList = fieldDao.getAllList(battleID, playerId);
		ArrayList<Object> myTargetList = new ArrayList<Object>();

		for (int i = 0; i < myFieldDtoList.size(); i++) {
			BattleFieldDTO list = myFieldDtoList.get(i);
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				myTargetList.add(myFieldDtoList.get(i).getField_no());
			}
		}

		if (myTargetList.size() != 0) {
			HashMap<String, Object> myTarget = new HashMap<String, Object>();
			myTarget.put("playerId", playerId);
			myTarget.put("list", myTargetList);
			myretTargetList.add(myTarget);
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0) {
				enemyTargetList.add(enemyFieldDtoList.get(i).getField_no());
			}
		}

		if (enemyTargetList.size() != 0) {
			HashMap<String, Object> enemyTarget = new HashMap<String, Object>();
			enemyTarget.put("playerId", enemyPlayerId);
			enemyTarget.put("list", enemyTargetList);
			enemyretTargetList.add(enemyTarget);
		}

		//対象が一人も居ない場合は処理終了
		if (myTargetList.size() == 0 && enemyTargetList.size() == 0) {
			return new HashMap<String, Object>();
		}

		//戻り値設定
		HashMap<String, Object> myretMap = new HashMap<String, Object>();
		HashMap<String, Object> enemyretMap = new HashMap<String, Object>();
		ArrayList retList = new ArrayList();

		if (myTargetList.size() > 0) {
			myretMap.put("selectCount", 1);
			myretMap.put("targetList", myretTargetList);
			retList.add(myretMap);
		}
		if (enemyTargetList.size() > 0) {
			enemyretMap.put("selectCount", 1);
			enemyretMap.put("targetList", enemyretTargetList);
			retList.add(enemyretMap);
		}

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

		//効果処理
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

					//対象ユニットを行動済にする
					fieldDto.setAction(1);
					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("remove", "actionEnd");
					retList.add(detailMap);
				}
			}
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

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
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
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
