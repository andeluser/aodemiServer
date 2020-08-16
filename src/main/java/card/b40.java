package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;

//蛇目の参謀
public class b40 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
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

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//まず、自分は行動終了
		BattleFieldDTO myDto = fieldDao.getAllValue(battleID, enemyPlayerId, fieldNumber);
		myDto.setAction(1);
		fieldDao.update(myDto);

		HashMap<String, Object> detailMap3 = new HashMap<String, Object>();
		ArrayList retList3 = new ArrayList();

		detailMap3.put("playerId", playerId);
		detailMap3.put("fieldNumber", fieldNumber);
		detailMap3.put("remove", "actionEnd");
		retList3.add(detailMap3);

		HashMap<String, Object> updateMap3 = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap3.put("field", retList3);
		updateList.add(updateMap3);

		//スキルの対象を計算
		ArrayList<Object> targetList = new ArrayList<Object>();
		ArrayList<BattleFieldDTO> dtoList = fieldDao.getAllList(battleID, playerId);

		//自分以外のユニットを選択
		for (int i = 0; i < dtoList.size(); i++) {

			BattleFieldDTO dto = dtoList.get(i);

			if (fieldNumber == i) {
				continue;
			}

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
				targetList.add(dto.getField_no());
			}
		}

		if (targetList.size() != 0) {
			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//対象のHPをゼロにする
			BattleFieldDTO myTeamDto = dtoList.get(target);
			myTeamDto.setCur_hp(0);

			if (myTeamDto.getCur_hp() <= 0) {
				fieldDao.setClose(battleID, myTeamDto.getPlayer_id(), myTeamDto.getField_no(), myTeamDto);
			}

			fieldDao.update(myTeamDto);

			//戻り値の作成
			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			ArrayList retList = new ArrayList();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", target);
			detailMap.put("hp", 0);
			retList.add(detailMap);

			//戻り値設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("field", retList);
			updateList.add(updateMap);

			//相手２体に４０ダメージ（２回）
			for (int k = 0; k < 2; k ++) {

				ArrayList<BattleFieldDTO> enemyFieldList = fieldDao.getAllList(battleID, enemyPlayerId);
				ArrayList enemyTargetList = new ArrayList();

				//対象を探す
				for (int i = 0; i < enemyFieldList.size(); i++) {
					BattleFieldDTO dto = enemyFieldList.get(i);

					if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
						enemyTargetList.add(dto.getField_no());
					}
				}

				if (enemyTargetList.size() == 0) {
					break;
				}

				//候補から対象を決める
				ArrayList retList2 = new ArrayList();

				int  enemyNum = rand.nextInt(enemyTargetList.size());
				int  enemyTarget = (int)enemyTargetList.get(enemyNum);

				BattleFieldDTO fieldDto = enemyFieldList.get(enemyTarget);

				//対象のHPに40ダメージを与える
				int def = fieldDto.getCur_def() + fieldDto.getTurn_def() + fieldDto.getPermanent_def();
				int attack = 40;

				attack = attack - def;

				if (attack < 0) {
					attack = 0;
				}

				fieldDto.setCur_hp(fieldDto.getCur_hp() - attack);

				if (fieldDto.getCur_hp() <= 0) {
					fieldDao.setClose(battleID, fieldDto.getPlayer_id(), fieldDto.getField_no(), fieldDto);
				}

				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap2 = new HashMap<String, Object>();

				detailMap2.put("playerId", enemyPlayerId);
				detailMap2.put("fieldNumber", fieldDto.getField_no());
				detailMap2.put("hp", fieldDto.getCur_hp());
				retList2.add(detailMap2);

				HashMap<String, Object> updateMap2 = new HashMap<String, Object>();
				updateMap2.put("field", retList2);
				updateList.add(updateMap2);
			}
		}

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> autoSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
