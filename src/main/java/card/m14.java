package card;

import java.util.ArrayList;
import java.util.HashMap;

import dao.BattleBaseDAO;
import dao.BattleControllDAO;
import dto.BattleBaseDTO;
import dto.BattleControllDTO;
import factory.DaoFactory;
import util.CommonUtil;

//強盗
public class m14 implements CardAbility {

	@Override
	public HashMap<String, Object> open(String battleID, String playerId) throws Exception {
		HashMap<String, Object> ret = new HashMap<String, Object>();

		DaoFactory factory = new DaoFactory();

		BattleControllDAO controllDao = factory.createControllDAO();
		BattleControllDTO controllDto = controllDao.getAllValue(battleID);

		String enemyPlayerId = "";
		if (playerId.equals(controllDto.getPlayer_id_1())) {
			enemyPlayerId = controllDto.getPlayer_id_2();
		} else {
			enemyPlayerId = controllDto.getPlayer_id_1();
		}

		BattleBaseDAO baseDao = factory.createBaseDAO();
		BattleBaseDTO enemyBaseDto = baseDao.getAllValue(battleID, enemyPlayerId);

		HashMap<String, Object> detailMap = new HashMap<String, Object>();
		ArrayList<Object> retList = new ArrayList<Object>();

		CommonUtil util = new CommonUtil();

		//相手のゲージを２０下げる
		HashMap<String, Object> map = util.addSpecial(enemyBaseDto.getSpecial_gage(), enemyBaseDto.getSpecial_stock(), -20, 0);
		enemyBaseDto.setSpecial_gage((int)map.get("gage"));
		enemyBaseDto.setSpecial_stock((int)map.get("stock"));
		baseDao.update(enemyBaseDto);

		detailMap.put("playerId", enemyPlayerId);
		detailMap.put("gage", enemyBaseDto.getSpecial_gage());
		detailMap.put("stock", enemyBaseDto.getSpecial_stock());
		retList.add(detailMap);

		//自分のゲージを１０下げる
		BattleBaseDTO myBaseDto = baseDao.getAllValue(battleID, playerId);
		HashMap<String, Object> detailMap2 = new HashMap<String, Object>();

		HashMap<String, Object> myMap = util.addSpecial(myBaseDto.getSpecial_gage(), myBaseDto.getSpecial_stock(), -10, 0);
		myBaseDto.setSpecial_gage((int)myMap.get("gage"));
		myBaseDto.setSpecial_stock((int)myMap.get("stock"));
		baseDao.update(myBaseDto);

		detailMap2.put("playerId", playerId);
		detailMap2.put("gage", myBaseDto.getSpecial_gage());
		detailMap2.put("stock", myBaseDto.getSpecial_stock());
		retList.add(detailMap2);

		//戻り値設定
		HashMap<String, Object> updateMap = new HashMap<String, Object>();
		ArrayList<Object> updateList = new ArrayList<Object>();

		updateMap.put("special", retList);
		updateList.add(updateMap);

		ret.put("updateInfo", updateList);
		ret.put("target", new ArrayList<Object>());

		return ret;
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
