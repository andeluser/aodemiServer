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
import util.CommonUtil;

public class m26 implements CardAbility {

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
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		BattleFieldDAO fieldDao = factory.createFieldDAO();
		BattleFieldDTO fieldDto = fieldDao.getAllValue(battleID, playerId, fieldNumber);

		//戻り値の作成
		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);

		//ATKを調整する
		int curATK = (baseDto.getSpecial_stock() * 10) - fieldDto.getCur_atk();
		//ターン増加はゼロに
		fieldDto.setTurn_atk(0);
		//永続増加は全体でストック×１０となるように調整
		fieldDto.setPermanent_atk(curATK);

		fieldDao.update(fieldDto);

		detailMap.put("playerId", playerId);
		detailMap.put("fieldNumber", fieldNumber);
		detailMap.put("tupATK", fieldDto.getTurn_atk());
		detailMap.put("upATK", fieldDto.getPermanent_atk());
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
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		//相手プレイヤーのID取得
		String enemyPlayerId = playerId;
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		//自分のゲージを２０増やす
		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO baseDto = baseDao.getAllValue(battleID, playerId);

		CommonUtil util = new CommonUtil();

		HashMap<String, Object> map = util.addSpecial(baseDto.getSpecial_gage(), baseDto.getSpecial_stock(), 20, 0);
		baseDto.setSpecial_gage((int)map.get("gage"));
		baseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(baseDto);

		detailMap.put("playerId", playerId);
		detailMap.put("gage", baseDto.getSpecial_gage());
		detailMap.put("stock", baseDto.getSpecial_stock());
		retList.add(detailMap);

		//相手のゲージを２０増やす
		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);
		HashMap<String, Object> map2 = util.addSpecial(enemyBaseDto.getSpecial_gage(), enemyBaseDto.getSpecial_stock(), 20, 0);
		enemyBaseDto.setSpecial_gage((int)map2.get("gage"));
		enemyBaseDto.setSpecial_stock((int)map2.get("stock"));
		baseDao.update(enemyBaseDto);

		detailMap2.put("playerId", enemyPlayerId);
		detailMap2.put("gage", enemyBaseDto.getSpecial_gage());
		detailMap2.put("stock", enemyBaseDto.getSpecial_stock());
		retList.add(detailMap2);

		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("special", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

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
