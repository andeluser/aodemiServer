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

//魔力弓兵見習い
public class b43 implements CardAbility {

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


//	@Override
//	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
//
//		HashMap<String, Object> ret = new HashMap<String, Object>();
//
//		DaoFactory factory = new DaoFactory();
//		BattleBaseDAO baseDao = factory.createBaseDAO();
//		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);
//		BattleControllDAO controllDao = factory.createControllDAO();
//		BattleControllDTO controllDto = controllDao.getAllValue(battleID);
//
//		BattleFieldDAO fieldDao = factory.createFieldDAO();
//
//		//自分の情報を取得
//		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);
//
//		int checkAGI = fieldDto.getPermanent_speed() + fieldDto.getTurn_speed() + fieldDto.getCur_speed();
//		int checkATK = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();
//
//		//ATKの値が0以外、AGIが5未満なら処理終了
//		if(checkATK != 0 && checkAGI < 5) {
//			return new HashMap<String, Object>();
//		}
//
//		//効果処理
//
//		String enemyPlayerId = "";
//		if (playerId.equals(controllDto.getPlayer_id_1())) {
//			enemyPlayerId = controllDto.getPlayer_id_2();
//		} else {
//			enemyPlayerId = controllDto.getPlayer_id_1();
//		}
//
//		//スキルの対象を計算
//		BattleFieldUtil battleUtil = new BattleFieldUtil();
//		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);
//
//		if (targetList.size() != 0) {
//
//			//候補から対象を決める
//			Random rand = new Random();
//			int num = rand.nextInt(targetList.size());
//			int target = (int)targetList.get(num);
//
//			//攻撃対象
//			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);
//
//			//ダメージ20
//			int attack = 20 - (fieldDto.getPermanent_def() + fieldDto.getTurn_def() + fieldDto.getCur_def());
//
//			if (attack <= 0) {
//				attack = 0;
//			}
//
//			//HP
//			int enemyHp = enemyFieldDto.getCur_hp();
//
//			//HPを計算
//			enemyHp = enemyHp - attack;
//
//			//相手のHPを設定
//			enemyFieldDto.setCur_hp(enemyHp);
//
//			//相手のHPがゼロ以下となった場合はクローズ判定を立てる
//			if (enemyHp <= 0) {
//				fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
//			}
//
//			fieldDao.update(enemyFieldDto);
//
//
//			//戻り値を設定
//			HashMap<String, Object> updateMap = new HashMap<String, Object>();
//
//			updateMap.put("fieldNumber", target);
//			updateMap.put("hp", enemyHp);
//			updateMap.put("playerId", enemyPlayerId);
//			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
//			updateList.add(updateMap);
//
//
//			//自分のATK=20/AGI=4に
//			int curATK = 20 - fieldDto.getCur_atk();
//			fieldDto.setTurn_atk(0);
//			fieldDto.setPermanent_atk(curATK);
//
//			int curAGI = 4 - fieldDto.getCur_speed();
//			fieldDto.setTurn_speed(0);
//			fieldDto.setPermanent_speed(curAGI);
//
//			fieldDao.update(fieldDto);
//
//			//戻り値を設定
//			HashMap<String, Object> detailMap = new HashMap<String, Object>();
//
//			detailMap.put("playerId", playerId);
//			detailMap.put("fieldNumber", fieldNumber);
//			detailMap.put("tupATK", fieldDto.getTurn_atk());
//			detailMap.put("upATK", fieldDto.getPermanent_atk());
//			detailMap.put("tupAGI", fieldDto.getTurn_speed());
//			detailMap.put("upAGI", fieldDto.getPermanent_speed());
//
//			ArrayList<Object> mylist = new ArrayList<Object>();
//			mylist.add(detailMap);
//
//			HashMap<String, Object> myMap = new HashMap<String, Object>();
//			myMap.put("field", mylist);
//
//			updateList.add(myMap);
//
//			HashMap<String, Object> orderMap = new HashMap<String, Object>();
//			ArrayList<Object> orderList = new ArrayList<Object>();
//
//			orderMap.put("field", updateList);
//			orderList.add(orderMap);
//
//			ret.put("updateInfo", orderList);
//		}
//
//
//		return ret;
//
//
//	}

	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleFieldDAO fieldDao = factory.createFieldDAO();
		//自分の情報を取得
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		int checkAGI = fieldDto.getPermanent_speed() + fieldDto.getTurn_speed() + fieldDto.getCur_speed();
		int checkATK = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

		//ATKの値が0以外、AGIが5未満なら処理終了
		if(checkATK != 0 && checkAGI < 5) {
			return new HashMap<String, Object>();
		}


		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);


		if (targetList.size() == 0) {
			return new HashMap();
		}

		HashMap<String, Object> targetMap = new HashMap<String, Object>();
		targetMap.put("list", targetList);
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
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();

		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		for (int i = 0; i < targetList.size(); i++) {
			HashMap<String, Object> oyaMap = (HashMap<String, Object>)targetList.get(i);
			ArrayList<Object> koList = (ArrayList<Object>)oyaMap.get("targetList");

			for (int j = 0; i < koList.size(); i++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//ダメージ20
					int attack = 20 - (enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def());

					if (attack <= 0) {
						attack = 0;
					}

					//HP
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

					//自分のATK=20/AGI=4に
					int curATK = 20 - fieldDto.getCur_atk();
					fieldDto.setTurn_atk(0);
					fieldDto.setPermanent_atk(curATK);

					int curAGI = 4 - fieldDto.getCur_speed();
					fieldDto.setTurn_speed(0);
					fieldDto.setPermanent_speed(curAGI);

					fieldDao.update(fieldDto);


					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					updateMap.put("hp", enemyHp);
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);


		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		detailMap.put("upATK", fieldDto.getPermanent_atk());
		detailMap.put("tupAGI", fieldDto.getTurn_speed());
		detailMap.put("upAGI", fieldDto.getPermanent_speed());

		ArrayList<Object> mylist = new ArrayList<Object>();
		mylist.add(detailMap);

		HashMap<String, Object> myMap = new HashMap<String, Object>();
		myMap.put("field", mylist);

		updateList.add(updateMap);
		updateList.add(myMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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

	//魔法矢･射
	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		BattleBaseDAO baseDao = factory.createBaseDAO();
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

		//スキルの対象を計算
		BattleFieldUtil battleUtil = new BattleFieldUtil();
		ArrayList<Object> targetList = battleUtil.getRangeTargetList(controllDto, playerId, fieldNumber);

		if (targetList.size() != 0) {

			//候補から対象を決める
			Random rand = new Random();
			int num = rand.nextInt(targetList.size());
			int target = (int)targetList.get(num);

			//攻撃対象
			BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, enemyPlayerId, target);

			//ダメージATK
			int ATK = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();
			int attack = ATK - (enemyFieldDto.getPermanent_def() + enemyFieldDto.getTurn_def() + enemyFieldDto.getCur_def());

			if (attack <= 0) {
				attack = 0;
			}

			//HP
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

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayerId);
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
			updateList.add(updateMap);


			//自分のATK=0/AGI+1
			int curATK = 0 - fieldDto.getCur_atk();
			fieldDto.setTurn_atk(0);
			fieldDto.setPermanent_atk(curATK);

			fieldDto.setPermanent_speed(fieldDto.getPermanent_speed() + 1);

			fieldDao.update(fieldDto);

			//戻り値を設定
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("upATK", fieldDto.getPermanent_atk());
			detailMap.put("upAGI", fieldDto.getPermanent_speed());

			ArrayList<Object> mylist = new ArrayList<Object>();
			mylist.add(detailMap);

			HashMap<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("field", mylist);

			updateList.add(myMap);

			HashMap<String, Object> orderMap = new HashMap<String, Object>();
			ArrayList<Object> orderList = new ArrayList<Object>();

			orderMap.put("field", updateList);
			orderList.add(orderMap);

			ret.put("updateInfo", orderList);
		}


		return ret;
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
