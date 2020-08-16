package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;
import util.StringUtil;

//極東会頭『ゴトウ』
public class m20 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ストックが2以上の場合は発動しない
		if (baseDto.getSpecial_stock() >= 2) {
			return ret;
		}

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

		//自分の選択対象が居ない場合、処理を終了する
		if (myTargetList.size() == 0) {
			return ret;
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

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		//相手の対象
		ArrayList<BattleFieldDTO> enemyFieldDto = fieldDao.getAllList(battleID, enemyPlayerId);
		ArrayList enemyTargetList = new ArrayList();
		for (int i = 0; i < enemyFieldDto.size(); i++) {
			BattleFieldDTO dto = enemyFieldDto.get(i);

			if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0 && dto.getAction() == 0) {
				enemyTargetList.add(dto.getField_no());
			}
		}

		if (enemyTargetList.size() != 0) {
			HashMap<String, Object> targetMap2 = new HashMap<String, Object>();
			targetMap2.put("list", enemyTargetList);
			targetMap2.put("playerId", enemyPlayerId);

			ArrayList<Object> retTargetList2 = new ArrayList<Object>();
			retTargetList2.add(targetMap2);

			HashMap<String, Object> retMap2 = new HashMap<String, Object>();
			retMap2.put("selectCount", 1);
			retMap2.put("targetList", retTargetList2);

			if (retMap2.size() != 0) {
				retList.add(retMap2);
			}
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
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		StringUtil util = new StringUtil();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//複数クローズの可能性があるので番号を取得する
		int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

		//対象をアクション終了する
		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//対象のHPをゼロに
					fieldDto.setCur_hp(0);

					//Hpが１以下ならクローズ情報を設定
					if (fieldDto.getCur_hp() <= 0) {
						fieldDto.setClose(1);
						fieldDto.setClose_number(closeNumber);
					}

					fieldDao.update(fieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", 0);
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {

		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//自分の情報を取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		int count = 1;
		if (baseDto.getSpecial_stock() <= 2) {
			count = 2;
		}

		BattleFieldDTO myDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		ArrayList<Object> retList = new ArrayList<Object>();

		for (int j = 0; j < count; j++) {

			//スキルの対象を計算
			BattleFieldUtil battleUtil = new BattleFieldUtil();
			ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

			//対象なし
			if (tmpList.size() == 0) {
				break;
			}

			//対象からランダムで２体選択する
			List result = new ArrayList();
			if (tmpList.size() != 1) {

				ArrayList updateList = new ArrayList();
				Collections.shuffle(tmpList);
				result = tmpList.subList(0, 2);

			} else {
				//対象1の場合は全て選択
				result = tmpList;
			}

			//複数クローズの可能性があるので番号を取得する
			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

			for (int i = 0; i < result.size(); i++) {

				int index = (int)result.get(i);

				BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, index);

				//対象にダメージ
				int hp = fieldDto.getCur_hp();

				int attack = myDto.getPermanent_atk() + myDto.getTurn_atk() + myDto.getCur_atk();
				attack = attack / 2;

				attack = attack - fieldDto.getPermanent_def() - fieldDto.getTurn_def() - fieldDto.getCur_def();

				if (attack <= 0) {
					attack = 0;
				}

				hp = hp - attack;

				fieldDto.setCur_hp(hp);

				//Hpが１以下ならクローズ情報を設定
				if (fieldDto.getCur_hp() <= 0) {
					fieldDto.setClose(1);
					fieldDto.setClose_number(closeNumber);
				}

				fieldDao.update(fieldDto);

				//戻り値の作成
				HashMap<String, Object> detailMap = new HashMap<String, Object>();

				detailMap.put("playerId", enemyPlayerId);
				detailMap.put("fieldNumber", index);
				detailMap.put("hp", hp);
				retList.add(detailMap);

			}
		}

		if (retList.size() == 0) {
			return ret;
		}

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
