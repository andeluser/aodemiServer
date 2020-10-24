package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleControllDAO;
import dao.BattleDeckDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleDeckDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

public class b51 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleDeckDAO deckDao = factory.createDeckDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDTO = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDTO.getPlayer_id_1())) {
			enemyPlayerId = controllDTO.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDTO.getPlayer_id_1();
		}

		ArrayList<Object> retTargetList = new ArrayList<Object>();

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, playerId);

		int targetCount = 0;

		//対象を計算する
		ArrayList<Object> targetList = new ArrayList<Object>();

		for (int i = 0; i < fieldDtoList.size(); i++) {
			BattleFieldDTO list = fieldDtoList.get(i);

			BattleDeckDTO deckDto = deckDao.getAllValue(battleID, playerId, list.getDeck_no());

			//ネームドのみ
			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0 && list.getAction() == 0 && "ネームド".equals(deckDto.getCard_type()) ) {
				targetList.add(fieldDtoList.get(i).getField_no());
				targetCount++;
			}
		}

		if (targetList.size() != 0) {
			HashMap<String, Object> myTarget = new HashMap<String, Object>();
			myTarget.put("playerId", playerId);
			myTarget.put("list", targetList);
			retTargetList.add(myTarget);
		}

		//相手の対象計算
		ArrayList<BattleFieldDTO> enemyFieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		//対象を計算する
		ArrayList<Object> enemyTargetList = new ArrayList<Object>();

		for (int i = 0; i < enemyFieldDtoList.size(); i++) {
			BattleFieldDTO list = enemyFieldDtoList.get(i);

			BattleDeckDTO deckDto = deckDao.getAllValue(battleID, playerId, list.getDeck_no());

			if (list.getCard_id() != null && !"".equals(list.getCard_id()) && list.getClose() == 0  && list.getAction() == 0 && "ネームド".equals(deckDto.getCard_type())) {
				enemyTargetList.add(enemyFieldDtoList.get(i).getField_no());
				targetCount++;
			}
		}

		if (enemyTargetList.size() != 0) {
			HashMap<String, Object> enemyTarget = new HashMap<String, Object>();
			enemyTarget.put("playerId", enemyPlayerId);
			enemyTarget.put("list", enemyTargetList);
			retTargetList.add(enemyTarget);
		}

		if (targetList.size() == 0 && enemyTargetList.size() == 0) {
			//対象が一人も居ない場合は処理終了
			return new HashMap<String, Object>();
		}

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		int loopCount = 0;
		if (targetCount == 1) {
			loopCount = 1;
		} else if (targetCount >= 2) {
			loopCount = 2;
		}

		ArrayList<Object> retList = new ArrayList<Object>();

		//候補から対象を決定する
		for (int i = 0; i < loopCount; i ++) {

			String targetPlayer = "";
			int target = 0;
			BattleFieldDTO fieldDto = new BattleFieldDTO();

			//既に選択済の場合は変更する
			while (true) {

				//候補から対象を決める
				Random rand = new Random();
				int num = rand.nextInt(retTargetList.size());

				//対象プレイヤーが決まったら対象を選択する
				HashMap<String, Object> map = (HashMap)retTargetList.get(num);

				targetPlayer = (String)map.get("playerId");

				//対象位置を決める
				ArrayList list = (ArrayList)map.get("list");
				int num2 = rand.nextInt(list.size());

				target = (int)list.get(num2);

				fieldDto = fieldDao.getAllValue(battleID, targetPlayer, target);

				if (fieldDto.getAction() == 0) {
					break;
				}
			}

			fieldDto.setAction(1);
			fieldDao.update(fieldDto);

			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			//対象をアクション終了する
			detailMap.put("playerId", targetPlayer);
			detailMap.put("fieldNumber", target);
			detailMap.put("remove", "actionEnd");
			retList.add(detailMap);

		}

		if (retList.size() != 0) {
			updateMap.put("field", retList);
			updateList.add(updateMap);
		}

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> openSelect(String battleID, String playerId, ArrayList<Object> targetList)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
