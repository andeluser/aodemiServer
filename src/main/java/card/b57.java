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

public class b57 implements CardAbility {

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

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		//SPを２消費
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
		baseDto.setSp(baseDto.getSp() - 2);
		baseDao.update(baseDto);

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

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

		ArrayList<Object> updateList = new ArrayList<Object>();
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		if (tmpList.size() != 0) {

			Random rand = new Random();
			int num = rand.nextInt(tmpList.size());

			ArrayList<BattleFieldDTO> fieldList = fieldDao.getAllList(battleID, enemyPlayerId);
			BattleFieldDTO myDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

			//どの列かを判定し処理する
			int target = (int)tmpList.get(num);

			int startRowNumber = 0;

			if (target <= 2) {
				startRowNumber = 0;
			} else if (target > 2 && target <= 5) {
				startRowNumber = 3;
			} else if (target > 5 && target <= 8) {
				startRowNumber = 6;
			}

			//複数クローズの可能性があるので番号を取得する
			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

			for (int i = 0; i < 3; i++) {
				int index  = startRowNumber + i;
				BattleFieldDTO fieldDto = fieldList.get(index);

				if (fieldDto.getCard_id() != null && !"".equals(fieldDto.getCard_id()) && fieldDto.getClose() == 0) {

					//対象にダメージ
					int hp = fieldDto.getCur_hp();

					int attack = myDto.getPermanent_atk() + myDto.getTurn_atk() + myDto.getCur_atk();
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

			updateMap.put("field", retList);
			updateList.add(updateMap);

		}

		//対象なしかつストックが３の場合はSPを２増やす
		if (tmpList.size() == 0) {

			if (baseDto.getSpecial_stock() == 3) {
				HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
				ArrayList<Object> retList2 = new ArrayList<Object>();

				baseDto.setSp(baseDto.getSp() + 2);
				baseDao.update(baseDto);

				detailMap2.put("playerId", playerId);
				detailMap2.put("SP", baseDto.getSp());
				retList2.add(detailMap2);
				updateMap.put("sp", retList2);
				updateList.add(updateMap);

			}
		}

		if (updateList.size() != 0) {
			ret.put("updateInfo", updateList);
		}

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
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleBaseDAO baseDao = factory.createBaseDAO();

		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		//ＳＰを１減らす
		baseDto.setSp(baseDto.getSp() - 1);
		baseDao.update(baseDto);

		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		BattleBaseDAO enemyBaseDao = factory.createBaseDAO();
		BattleBaseDTO enemyBaseDto = enemyBaseDao.getAllValue(battleID, enemyPlayerId);

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> orderList = new ArrayList<Object>();

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//どこの列か判定
			int start = 0;

			if (target == 1 || target == 4 || target == 7) {
				start = 1;
			} else if (target == 2 || target == 5 || target == 8) {
				start = 2;
			}

			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();

			int closeNumber = fieldDao.getCloseNumber(battleID) + 1;

			for (int index = 0; index < 3; index++) {
				//攻撃対象のHPを減らす
				BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, start + (index * 3));

				if (enemyFieldDto.getCard_id() != null && !"".equals(enemyFieldDto.getCard_id()) && enemyFieldDto.getClose() == 0) {

					//ダメージ量を計算
					int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

					int def = enemyFieldDto.getTurn_def() + enemyFieldDto.getPermanent_def() + enemyFieldDto.getCur_def();

					attack = attack - def;

					if (attack < 0) {
						attack = 0;
					}

					int enemyHp = enemyFieldDto.getCur_hp();

					//HPを計算
					enemyHp = enemyHp - attack;

					//相手のHPを設定
					enemyFieldDto.setCur_hp(enemyHp);

					//相手のHPがゼロ以下となった場合はクローズ判定を立てる
					if (enemyHp <= 0) {
						enemyFieldDto.setClose(1);
						enemyFieldDto.setClose_number(closeNumber);
					}

					fieldDao.update(enemyFieldDto);

					HashMap<String, Object> retList = new HashMap<String, Object>();

					retList.put("fieldNumber", start + (index * 3));
					retList.put("hp", enemyHp);
					retList.put("playerId", enemyPlayerId);

					updateList.add(retList);
				}

			}

			updateMap.put("field", updateList);
			orderList.add(updateMap);

		}

		//相手のストックが３の場合、SP2増やす
		if (enemyBaseDto.getSpecial_stock() == 3) {
			HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
			ArrayList<Object> retList2 = new ArrayList<Object>();
			HashMap<String, Object> updateMap2 = new HashMap<String, Object>();

			baseDto.setSp(baseDto.getSp() + 2);
			baseDao.update(baseDto);

			detailMap2.put("playerId", playerId);
			detailMap2.put("SP", baseDto.getSp());
			retList2.add(detailMap2);
			updateMap2.put("sp", retList2);
			orderList.add(updateMap2);
		}

		if (orderList.size() != 0) {
			ret.put("updateInfo", orderList);
		}

		return ret;
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
