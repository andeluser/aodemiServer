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

//怒れる襲歩
public class m36 implements CardAbility {

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

		if (baseDto.getSpecial_stock() <= 3) {
			return ret;
		}

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
		ArrayList<Object> retList = new ArrayList<Object>();

		//ターン中攻撃力を３０増加、AGI２増加
		fieldDto.setTurn_atk(fieldDto.getTurn_atk() + 30);
		fieldDto.setTurn_speed(fieldDto.getTurn_speed() + 2);
		fieldDao.update(fieldDto);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		detailMap.put("tupAGI", fieldDto.getTurn_speed());
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);

		//奥義ストックを１減らす
		int stock = baseDto.getSpecial_stock() - 1;
		if (stock < 0) {
			stock = stock - 1;
		}

		baseDto.setSpecial_stock(stock);
		baseDao.update(baseDto);

		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList2 = new ArrayList<Object>();

		detailMap2.put("playerId", playerId);
		detailMap2.put("stock", baseDto.getSpecial_stock());
		retList2.add(detailMap2);
		updateMap.put("special", retList2);

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

		//自分の情報を取得
		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//ＳＰを２減らす
		baseDto.setSp(baseDto.getSp() - 2);
		baseDao.update(baseDto);

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

		if (targetList.size() != 0) {

			int targetCount = 3;

			if (targetList.size() == 2) {
				targetCount = 2;
			} else if (targetList.size() == 1) {
				targetCount = 1;
			}

			//対象を計算
			Collections.shuffle(targetList);
			List result = targetList.subList(0, targetCount);

			BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

			//対象全てを処理する
			for (int index = 0; index < result.size(); index++) {

				BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, (int)targetList.get(index));

				//ダメージ量を計算
				int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

				int def = enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def();

				attack = attack - def;

				if (attack < 0) {
					attack = 0;
				}

				//攻撃対象のHPを減らす
				int enemyHp = enemyFieldDto.getCur_hp();

				//HPを計算
				enemyHp = enemyHp - attack;

				if (enemyHp <= 0) {
					enemyFieldDto.setClose(1);
					enemyFieldDto.setClose_number(closeNumber);
				}

				//相手のHPを設定
				enemyFieldDto.setCur_hp(enemyHp);
				fieldDao.update(enemyFieldDto);

				//戻り値を設定
				HashMap<String, Object> updateMap = new HashMap<String, Object>();

				updateMap.put("fieldNumber", index);
				updateMap.put("hp", enemyHp);
				updateMap.put("playerId", enemyPlayerId);
				updateList.add(updateMap);
			}

			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);

		}

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
