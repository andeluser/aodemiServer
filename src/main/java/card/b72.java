package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dao.BattleFieldDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import dto.BattleFieldDTO;
import factory.DaoFactory;
import util.BattleFieldUtil;

//溶岩砲の砲兵
public class b72 implements CardAbility {

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

	//緊急装填
	@Override
	public HashMap<String, Object> start(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		int special_use = baseDto.getSpecial_use();
		int special_use_bye_skill = baseDto.getSpecial_use_bye_skill();

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//このターンに奥義使用のフラグが立っていなければ処理終了
		if(special_use != 1 && special_use_bye_skill != 1) {
			return new HashMap<String, Object>();
		}

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);

		//RNGを3にする
		int curRng = 3 - fieldDto.getCur_range();
		//ターン増加はゼロに
		fieldDto.setTurn_range(0);
		//永続増加は全体で3となるように調整
		fieldDto.setPermanent_range(curRng);

		fieldDao.update(fieldDto);

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupRNG", fieldDto.getTurn_range());
		detailMap.put("upRNG", fieldDto.getPermanent_range());
		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("field", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
	}

	@Override
	public HashMap<String, Object> startSelect(String battleID, String playerId, ArrayList<Object> targetList,
			int fieldNumber) throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	//構えろ！
	@Override
	public HashMap<String, Object> auto(String battleID, String playerId, int fieldNumber) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		int RNG = fieldDto.getCur_range() + fieldDto.getTurn_range() + fieldDto.getPermanent_range();
		//RNGが3以上であれば処理を終了する
		if(RNG >= 3) {
			return new HashMap<String, Object>();
		}

		//RNGを3にする
		int curRng = 3 - fieldDto.getCur_range();
		//ターン増加はゼロに
		fieldDto.setTurn_range(0);
		//永続増加は全体で3となるように調整
		fieldDto.setPermanent_range(curRng);

		//自分のアクション終了
		fieldDto.setAction(1);

		//DBを更新
		fieldDao.update(fieldDto);

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupRNG", fieldDto.getTurn_range());
		detailMap.put("upRNG", fieldDto.getPermanent_range());
		detailMap.put("remove", "actionEnd");

		retList.add(detailMap);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		//戻り値設定
		updateMap.put("field", retList);
		updateList.add(updateMap);

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


	//撃てー！
	@Override
	public HashMap<String, Object> action1(String battleID, String playerId, int fieldNumber) throws Exception {

		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();
		BattleFieldDAO fieldDao = factory.createFieldDAO();
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
	public HashMap<String, Object> actionSelect1(String battleID, String playerId, ArrayList<Object> targetList,
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

			for (int j = 0; j < koList.size(); j++) {
				HashMap<String, Object> koMap = (HashMap<String, Object>)koList.get(j);

				String player1 = koMap.get("playerId").toString();
				ArrayList<Integer> list = (ArrayList)koMap.get("list");

				for (int k= 0; k < list.size(); k++) {
					BattleFieldDTO enemyFieldDto = fieldDao.getAllValue(battleID, player1, list.get(k));

					//ダメージ量を計算 防御無視
					int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

					//HP
					int enemyHp = enemyFieldDto.getCur_hp();

					//HPを計算
					enemyHp = enemyHp - attack*2;

					//相手のHPを設定
					enemyFieldDto.setCur_hp(enemyHp);

					//相手のHPがゼロ以下となった場合はクローズ判定を立てる
					if (enemyHp <= 0) {
						fieldDao.setClose(battleID, enemyFieldDto.getPlayer_id(), enemyFieldDto.getField_no(), enemyFieldDto);
					}

					fieldDao.update(enemyFieldDto);

					//RNGの計算
					int curRNG = 0 - fieldDto.getCur_range();
					fieldDto.setTurn_range(0);
					fieldDto.setPermanent_range(curRNG);

					fieldDao.update(fieldDto);



					//戻り値の作成
					HashMap<String, Object> detailMap = new HashMap<String, Object>();

					detailMap.put("playerId", player1);
					detailMap.put("fieldNumber", list.get(k));
					detailMap.put("hp", enemyHp);
					retList.add(detailMap);
				}
			}
		}

		updateMap.put("field", retList);

		//自分のRNGを0にする
		HashMap<String, Object> detailMap = new HashMap<String, Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupRNG", fieldDto.getTurn_range());
		detailMap.put("upRNG", fieldDto.getPermanent_range());

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
