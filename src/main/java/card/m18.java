package card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;
import util.CommonUtil;

//沼地の魔物
public class m18 implements CardAbility {

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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDTO myFieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		if (baseDto.getSpecial_stock() == 2) {

			return ret;
		}

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		ArrayList<BattleFieldDTO> fieldDtoList = fieldDao.getAllList(battleID, enemyPlayerId);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();
		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		//奥義ゲージで処理を分ける
		if (baseDto.getSpecial_stock() <= 1) {

			ArrayList targetList = new ArrayList();

			//対象を計算
			for (int i = 0; i < fieldDtoList.size(); i++) {
				BattleFieldDTO dto = fieldDtoList.get(i);

				if (dto.getCard_id() != null && !"".equals(dto.getCard_id()) && dto.getClose() == 0) {
					targetList.add(dto.getField_no());
				}
			}

			if (targetList.size() == 0) {
				return ret;
			}

			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			BattleFieldDTO enemyFieldDto = fieldDtoList.get(target);

			int attack = myFieldDto.getPermanent_atk() + myFieldDto.getTurn_atk() + myFieldDto.getCur_atk();
			int def = enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def();

			attack = attack - def;

			if (attack < 0) {
				attack = 0;
			}

			int hp = enemyFieldDto.getCur_hp();
			hp = hp - attack;

			enemyFieldDto.setCur_hp(hp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (hp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			fieldDao.update(enemyFieldDto);

			detailMap.put("playerId", enemyPlayerId);
			detailMap.put("fieldNumber", target);
			detailMap.put("hp", enemyFieldDto.getCur_hp());
			retList.add(detailMap);

		} else if (baseDto.getSpecial_stock() >= 3) {
			myFieldDto.setAction(1);
			fieldDao.update(myFieldDto);

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("remove", "actionEnd");
			retList.add(detailMap);
		}

		updateMap.put("field", retList);
		updateList.add(updateMap);

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ＳＰを1減らす
		baseDto.setSp(baseDto.getSp() - 1);
		baseDao.update(baseDto);

		//自分の情報を取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> tmpList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (tmpList.size() == 0) {

			HashMap<String, Object> detailMap = new HashMap<String, Object>();
			ArrayList<Object> retList = new ArrayList<Object>();
			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			//対象が居ない場合は奥義ゲージのみ減らす
			CommonUtil util = new CommonUtil();

			HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), -20, 0);
			baseDto.setSpecial_gage((int)map.get("gage"));
			baseDto.setSpecial_stock((int)map.get("stock"));
			baseDao.update(baseDto);

			detailMap.put("playerId", playerId);
			detailMap.put("gage", baseDto.getSpecial_gage());
			detailMap.put("stock", baseDto.getSpecial_stock());
			retList.add(detailMap);

			orderMap.put("special", retList);

			orderList.add(orderMap);
			ret.put("updateInfo", orderList);

			return ret;
		}

		HashMap<String, Object> targetMap = new HashMap<String, Object>();
		targetMap.put("list", tmpList);
		targetMap.put("playerId", enemyPlayerId);

		ArrayList<Object> retTargetList = new ArrayList<Object>();
		retTargetList.add(targetMap);

		HashMap<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("selectCount", 1);
		retMap.put("targetList", retTargetList);

		ArrayList retList = new ArrayList();
		if (retMap.size() != 0) {
			retList.add(retMap);
		}

		ret.put("updateInfo", new HashMap());
		ret.put("target", retList);

		return ret;
	}

	@Override
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber)
			throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
		String enemyPlayerId = "";

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				enemyPlayerId = player1;
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//自分のATK分のダメージ
					int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();
					int def = enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def();

					attack = attack - def;

					if (attack < 0) {
						attack = 0;
					}

					int hp = enemyFieldDto.getCur_hp();
					hp = hp - attack;
					enemyFieldDto.setCur_hp(hp);

					if (hp <= 0) {
						fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
					}

					fieldDao.update(enemyFieldDto);

					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", enemyFieldDto.getCur_hp());
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);

		//奥義ゲージを２０減らす
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList2 = new ArrayList<Object>();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, enemyPlayerId);

		CommonUtil util = new CommonUtil();

		HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), -20, 0);
		baseDto.setSpecial_gage((int)map.get("gage"));
		baseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(baseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("gage", baseDto.getSpecial_gage());
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);
		updateMap.put("special", retList2);

		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
