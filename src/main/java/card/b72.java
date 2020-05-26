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

			//ダメージ量を計算 防御無視
			int attack = fieldDto.getPermanent_atk() + fieldDto.getTurn_atk() + fieldDto.getCur_atk();

			if (attack <= 0) {
				attack = 0;
			}

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


			//戻り値を設定
			HashMap<String, Object> updateMap = new HashMap<String, Object>();

			updateMap.put("fieldNumber", target);
			updateMap.put("hp", enemyHp);
			updateMap.put("playerId", enemyPlayerId);
			ArrayList<HashMap<String, Object>> updateList = new ArrayList<HashMap<String, Object>>();
			updateList.add(updateMap);

			//自分のRNG=0にする
			int curRNG = 0 - fieldDto.getCur_range();
			fieldDto.setTurn_range(0);
			fieldDto.setPermanent_range(curRNG);

			fieldDao.update(fieldDto);

			//戻り値を設定
			HashMap<String, Object> detailMap = new HashMap<String, Object>();

			detailMap.put("playerId", playerId);
			detailMap.put("fieldNumber", fieldNumber);
			detailMap.put("tupRNG", fieldDto.getTurn_range());
			detailMap.put("upRNG", fieldDto.getPermanent_range());

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
