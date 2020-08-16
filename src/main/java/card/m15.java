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

//呪印兵士
public class m15 implements CardAbility {

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

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		if (baseDto.getSpecial_stock() == 3) {

			return ret;
		}

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();
		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		//奥義ゲージで処理を分ける
		if (baseDto.getSpecial_stock() <= 2) {
			fieldDto.setTurn_atk(fieldDto.getTurn_atk() + 10);
			fieldDto.setTurn_speed(fieldDto.getTurn_speed() + 1);

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("tupATK", fieldDto.getTurn_atk());
			detailMap.put("tupAGI", fieldDto.getTurn_speed());
			retList.add(detailMap);

		} else if (baseDto.getSpecial_stock() >= 4) {
			fieldDto.setTurn_atk(fieldDto.getTurn_atk() - 10);
			detailMap.put("tupATK", fieldDto.getTurn_atk());

			int speed = fieldDto.getTurn_speed() + fieldDto.getPermanent_speed() + fieldDto.getCur_speed();
			if (speed != 0) {
				fieldDto.setTurn_speed(fieldDto.getTurn_speed() - 1);
				detailMap.put("tupAGI", fieldDto.getTurn_speed());
			}

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);

			retList.add(detailMap);

		}

		fieldDao.update(fieldDto);
		updateMap.put("field", retList);

		updateList.add(updateMap);

		//戻り値設定
		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList, int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ＳＰを１減らす
		baseDto.setSp(baseDto.getSp() - 1);

		//自分のゲージを20減らす
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();
		CommonUtil util = new CommonUtil();

		HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), -20, 0);
		baseDto.setSpecial_gage((int)map.get("gage"));
		baseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(baseDto);

		detailMap.put("playerId", playerId);
		detailMap.put("gage", baseDto.getSpecial_gage());
		detailMap.put("stock", baseDto.getSpecial_stock());
		retList.add(detailMap);

		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		ArrayList<Object> orderList = new ArrayList<Object>();

		if (retList.size() != 0) {
			orderMap.put("special", retList);
		}

		baseDao.update(baseDto);

		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

			//ダメージ量を計算
			int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk() + 20;

			int def = enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def();

			attack = attack - def;

			if (attack < 0) {
				attack = 0;
			}

			//攻撃対象のHPを減らす
			int enemyHp = enemyFieldDto.getCur_hp();

			//HPを計算
			enemyHp = enemyHp - attack;

			//相手のHPを設定
			enemyFieldDto.setCur_hp(enemyHp);

			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
			if (enemyHp <= 0) {
				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
			}

			fieldDao.update(enemyFieldDto);

			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayerId);

			updateList.add(updateMap);
			orderMap.put("field", updateList);

		}

		orderList.add(orderMap);
		ret.put("updateInfo", orderList);

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
